package com.gg.tgather.travelgroupservice.modules.group.service;

import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupMemberDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import com.gg.tgather.travelgroupservice.modules.group.enums.GroupJoinStatus;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 여행그룹 참여자 관리 서비스
 *
 * @author joyeji
 * @since 2023.06.04
 */
@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class TravelGroupJoinService {

    private final TravelGroupRepository travelGroupRepository;

    /**
     * 여행그룹 가입 요청 확인
     *
     * @param travelGroupId  여행그룹 명
     * @param authentication 계정정보
     * @return List<TravelGroupMemberDto> 여행그룹 가입 신청자
     */
    public List<TravelGroupMemberDto> getTravelGroupMembersRequest(Long travelGroupId, GroupJoinStatus status, JwtAuthentication authentication) {
        TravelGroup travelGroup = travelGroupRepository.searchByTravelGroupAndLeader(travelGroupId, authentication.accountId())
            .orElseThrow(() -> new OmittedRequireFieldException("여행그룹의 권한이 없습니다."));

        return switch (status) {
            case NO_APPROVED ->
                travelGroup.getTravelGroupMemberList().stream().filter(travelGroupMember -> !travelGroupMember.isApproved()).map(TravelGroupMemberDto::from)
                    .toList();
            case APPROVED -> travelGroup.getTravelGroupMemberList().stream().filter(TravelGroupMember::isApproved).map(TravelGroupMemberDto::from).toList();
            default -> travelGroup.getTravelGroupMemberList().stream().map(TravelGroupMemberDto::from).toList();
        };
    }

}
