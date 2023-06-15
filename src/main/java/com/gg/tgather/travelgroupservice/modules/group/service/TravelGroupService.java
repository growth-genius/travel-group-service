package com.gg.tgather.travelgroupservice.modules.group.service;

import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupModifyForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupMemberRepository;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 여행그룹 CRUD 서비스
 *
 * @author joyeji
 * @since 2023.06.04
 */
@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class TravelGroupService {

    private final TravelGroupRepository travelGroupRepository;

    private final TravelGroupMemberRepository travelGroupMemberRepository;

    /**
     * 여행그룹 생성
     *
     * @param travelGroupSaveForm travelGroup 입력폼
     * @param authentication      인증정보
     * @return travelGroupDto travelGroup 생성 결과값
     */
    public TravelGroupDto createTravelGroup(TravelGroupSaveForm travelGroupSaveForm, JwtAuthentication authentication) {
        validTravelGroup(travelGroupSaveForm.getGroupName());
        TravelGroup travelGroup = TravelGroup.from(travelGroupSaveForm);
        travelGroupRepository.save(travelGroup);
        TravelGroupMember travelGroupMember = TravelGroupMember.createTravelGroupLeader(travelGroup, authentication.accountId());
        travelGroupMemberRepository.save(travelGroupMember);
        return TravelGroupDto.from(travelGroup);
    }

    /**
     * 여행그룹명 유효여부 확인
     *
     * @param travelGroupName 여행그룹명
     */
    private void validTravelGroup(String travelGroupName) {
        travelGroupRepository.findByGroupName(travelGroupName).ifPresent(m -> {
            throw new OmittedRequireFieldException("동일한 여행그룹명이 있습니다.");
        });
    }

    /**
     * 여행그룹 검색하기
     *
     * @param travelThemes travelGroup 검색 폼
     * @return TravelGroupDto travelGroup 검색 결과
     */
    public List<TravelGroupDto> findTravelGroupByTheme(Set<TravelTheme> travelThemes) {
        List<TravelGroup> travelGroups = travelGroupRepository.searchTravelGroupAllByTravelThemes(travelThemes);
        return travelGroups.stream().map(TravelGroupDto::from).toList();
    }

    /**
     * 여행그룹 정보 수정하기
     *
     * @param travelGroupModifyForm travelGroup 수정 폼
     * @param authentication        인증정보
     * @return TravelGroupDto travelGroup 수정 결과
     */
    public TravelGroupDto modifyTravelGroup(Long travelGroupId, TravelGroupModifyForm travelGroupModifyForm, JwtAuthentication authentication) {
        TravelGroup travelGroup = travelGroupRepository.searchByTravelGroupAndLeader(travelGroupId, authentication.accountId())
            .orElseThrow(() -> new OmittedRequireFieldException("여행그룹명을 찾을 수 없습니다."));
        validTravelGroupWithoutOwn(travelGroupModifyForm.getGroupName(), travelGroup.getId());
        travelGroup.modifyTravelGroup(travelGroupModifyForm);
        return TravelGroupDto.from(travelGroup);
    }

    /**
     * 본인 그룹을 제외한 그룹명 있는지 확인
     *
     * @param travelGroupName 여행 그룹명
     * @param travelGroupId   고유 그룹 id
     */
    private void validTravelGroupWithoutOwn(String travelGroupName, Long travelGroupId) {
        travelGroupRepository.searchByTravelGroupNameWithoutOwn(travelGroupName, travelGroupId).ifPresent(m -> {
            throw new OmittedRequireFieldException("동일한 여행그룹명이 있습니다.");
        });
    }

    /**
     * 여행그룹 삭제
     *
     * @param travelGroupId  여행 그룹명
     * @param authentication 인증정보
     * @return boolean 삭제 여부
     */
    public Boolean deleteTravelGroup(Long travelGroupId, JwtAuthentication authentication) {
        TravelGroup travelGroup = travelGroupRepository.searchByTravelGroupAndLeader(travelGroupId, authentication.accountId())
            .orElseThrow(() -> new OmittedRequireFieldException("여행그룹명을 찾을 수 없습니다."));
        travelGroup.deleteTravelGroup();
        return true;
    }
}
