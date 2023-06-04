package com.gg.tgather.travelgroupservice.modules.group.service;

import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupModifyForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSearchForm;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupMemberRepository;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class TravelGroupService {

    private final TravelGroupRepository travelGroupRepository;

    private final TravelGroupMemberRepository travelGroupMemberRepository;

    /**
     * TravelGroup 생성
     *
     * @param travelGroupSaveForm travelGroup 입력폼
     * @param authentication
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

    private void validTravelGroup(String travelGroupName) {
        travelGroupRepository.findByGroupName(travelGroupName).ifPresent(m -> {
            throw new OmittedRequireFieldException("동일한 여행그룹명이 있습니다.");
        });
    }

    /**
     * TravelGroup 검색하기
     *
     * @param travelGroupSearchForm travelGroup 검색 폼
     * @return TravelGroupDto travelGroup 검색 결과
     */
    public List<TravelGroupDto> findTravelGroupByTheme(TravelGroupSearchForm travelGroupSearchForm) {
        return travelGroupRepository.searchTravelGroupAllByTravelThemes(travelGroupSearchForm.getTravelThemes());
    }

    /**
     * TravelGroup 정보 수정하기
     *
     * @param travelGroupModifyForm travelGroup 수정 폼
     * @param authentication
     * @return TravelGroupDto travelGroup 수정 결과
     */
    public TravelGroupDto modifyTravelGroup(TravelGroupModifyForm travelGroupModifyForm, JwtAuthentication authentication) {
        TravelGroup travelGroup = travelGroupRepository.searchByTravelGroupAndLeader(travelGroupModifyForm.getGroupName(), authentication.accountId())
            .orElseThrow(() -> new OmittedRequireFieldException("여행그룹명을 찾을 수 없습니다."));
        validTravelGroupWithoutOwn(travelGroupModifyForm.getGroupName(), travelGroup.getId());
        travelGroup.modifyTravelGroup(travelGroupModifyForm);
        return TravelGroupDto.from(travelGroup);
    }

    private void validTravelGroupWithoutOwn(String travelGroupName, Long travelGroupId) {
        travelGroupRepository.searchByTravelGroupNameWithoutOwn(travelGroupName, travelGroupId).ifPresent(m -> {
            throw new OmittedRequireFieldException("동일한 여행그룹명이 있습니다.");
        });
    }

    public Boolean deleteTravelGroup(String travelGroupName, JwtAuthentication authentication) {
        TravelGroup travelGroup = travelGroupRepository.searchByTravelGroupAndLeader(travelGroupName, authentication.accountId())
            .orElseThrow(() -> new OmittedRequireFieldException("여행그룹명을 찾을 수 없습니다."));
        travelGroup.deleteTravelGroup();
        return true;
    }
}