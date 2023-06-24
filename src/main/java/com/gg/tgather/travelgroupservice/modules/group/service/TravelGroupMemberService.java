package com.gg.tgather.travelgroupservice.modules.group.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.commonservice.dto.account.AccountDto;
import com.gg.tgather.commonservice.dto.fcm.FcmMessageDto;
import com.gg.tgather.commonservice.properties.KafkaFcmTopicProperties;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.client.AccountServiceClient;
import com.gg.tgather.travelgroupservice.modules.group.dto.FcmContentDto;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupMemberDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupRole;
import com.gg.tgather.travelgroupservice.modules.group.kafka.TravelGroupKafkaProducer;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupMemberRepository;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 여행그룹 유저 참여관련 서비스
 *
 * @author joyeji
 * @since 2023.06.07
 */
@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class TravelGroupMemberService {

    private final TravelGroupRepository travelGroupRepository;
    private final TravelGroupMemberRepository travelGroupMemberRepository;
    private final TravelGroupKafkaProducer travelGroupKafkaProducer;
    private final KafkaFcmTopicProperties kafkaFcmTopicProperties;
    private final AccountServiceClient accountServiceClient;
    private final ObjectMapper objectMapper;

    /**
     * 여행그룹 가입 요청 처리
     *
     * @param travelGroupId  여행그룹Id
     * @param authentication 계정정보
     * @return boolean 여행그룹 가입 요청 결과
     */
    public TravelGroupMemberDto requestTravelGroupJoin(String travelGroupId, JwtAuthentication authentication) {
        TravelGroup travelGroup = travelGroupRepository.searchTravelGroupByIdWithLeader(travelGroupId)
            .orElseThrow(() -> new OmittedRequireFieldException("요청하신 여행그룹을 찾을 수 없습니다."));
        TravelGroupMember travelGroupMember = TravelGroupMember.joinTravelGroupMember(travelGroup, authentication.accountId(),
            validTravelGroup(travelGroup, authentication));
        travelGroupMemberRepository.save(travelGroupMember);
        return TravelGroupMemberDto.from(travelGroupMember);
    }

    /**
     * 여행그룹 유효성 검증
     * 비공개 그룹일 경우 여행그룹 리더에서 승인요청 메일 전송
     *
     * @param travelGroup    가입할 여행그룹
     * @param authentication 계정정보
     * @return 여행그룹 유효성 검증결과
     */
    private boolean validTravelGroup(TravelGroup travelGroup, JwtAuthentication authentication) {
        if (travelGroup.isDeleteTravelGroup()) {
            log.error("travelGroup is already deleted : {}", travelGroup.getGroupName());
            throw new OmittedRequireFieldException("요청하신 여행그룹을 찾을 수 없습니다.");
        }

        if (!travelGroup.isOpen()) {

            try {
                AccountDto accountDto = accountServiceClient.getAccount(authentication.accountId());
                return sendFcmMessage(travelGroup, accountDto);
            } catch (JsonProcessingException ex) {
                log.error("Failed to JsonProcessing");
                throw new OmittedRequireFieldException(ex.getMessage());
            }
        }

        List<TravelGroupMember> travelGroupMemberList = travelGroup.getTravelGroupMemberList();
        for (TravelGroupMember travelGroupMember : travelGroupMemberList) {
            if (travelGroupMember.getAccountId().equals(authentication.accountId())) {
                log.info("user already sign in : {}", travelGroup.getGroupName());
                return false;
            }
        }

        return true;
    }

    /**
     * FCM 토큰 전송 메소드
     *
     * @param travelGroup 여행그룹
     * @param accountDto  계정
     * @return boolean 여행그룹 가입 대기
     * @throws JsonProcessingException
     */
    private boolean sendFcmMessage(TravelGroup travelGroup, AccountDto accountDto) throws JsonProcessingException {
        FcmMessageDto fcmMessageDto = new FcmMessageDto();
        fcmMessageDto.setTitle(travelGroup.getGroupName() + "가입 요청");
        fcmMessageDto.setToken(accountDto.getFcmToken());
        String fcmContent = objectMapper.writeValueAsString(FcmContentDto.createFcmContentDto(travelGroup.getTravelGroupId(), travelGroup.getGroupName()));
        fcmMessageDto.setMessage(fcmContent);
        travelGroupKafkaProducer.send(kafkaFcmTopicProperties.getSendSingleFcmTopic(), fcmMessageDto);
        log.info("travelGroup is private : {}", travelGroup.getGroupName());
        return false;
    }

    /**
     * 여행그룹에서 탈퇴
     *
     * @param travelGroupId  여행그룹 아이디
     * @param memberId       사용자 아이디
     * @param authentication 계정정보
     * @return Boolean 여행그룹 탈퇴 여부
     */
    public Boolean deleteTravelGroupMember(String travelGroupId, Long memberId, JwtAuthentication authentication) {
        TravelGroup travelGroup = travelGroupRepository.findByTravelGroupId(travelGroupId)
            .orElseThrow(() -> new OmittedRequireFieldException("요청하신 여행그룹을 찾을 수 없습니다."));
        TravelGroupMember travelGroupMember = travelGroup.getTravelGroupMemberList().stream().filter(member -> member.getId().equals(memberId)).findFirst()
            .orElseThrow(() -> new OmittedRequireFieldException("요청하신 사용자를 찾을 수 없습니다."));
        boolean validLeader = validTravelGroupLeader(travelGroup, authentication);

        if (validLeader && travelGroupMember.getAccountId().equals(authentication.accountId())) {
            throw new OmittedRequireFieldException("방장은 방을 탈퇴할 수 없습니다.");
        }

        if (travelGroupMember.getAccountId().equals(authentication.accountId()) || validLeader) {
            boolean availableRemoveMember = travelGroupMember.isAvailableRemoveMember();
            if (availableRemoveMember) {
                travelGroupMemberRepository.delete(travelGroupMember);
            }
            return availableRemoveMember;
        }
        throw new OmittedRequireFieldException("탈퇴할 권한이 없습니다.");
    }

    /**
     * 여행 그룹의 방장인지 확인
     *
     * @param authentication 계정정보
     * @return boolean 방장 여부
     */
    private boolean validTravelGroupLeader(TravelGroup travelGroup, JwtAuthentication authentication) {
        TravelGroupMember travelGroupMember = travelGroupMemberRepository.findByAccountId(authentication.accountId())
            .orElseThrow(() -> new OmittedRequireFieldException("접근 권한이 없습니다."));
        return travelGroupMember.getTravelGroup().equals(travelGroup) && travelGroupMember.getTravelGroupRole().equals(TravelGroupRole.LEADER);
    }
}
