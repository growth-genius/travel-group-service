package com.gg.tgather.travelgroupservice.modules.group.service;

import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupModifyForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSearchForm;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@BaseServiceAnnotation
@RequiredArgsConstructor
public class TravelGroupService {

    private final TravelGroupRepository travelGroupRepository;

    /**
     * TravelGroup 생성
     *
     * @param travelGroupSaveForm travelGroup 입력폼
     * @return travelGroupDto travelGroup 생성 결과값
     */
    public TravelGroupDto createTravelGroup(TravelGroupSaveForm travelGroupSaveForm) {
        validTravelGroup(travelGroupSaveForm);

        TravelGroup travelGroup = TravelGroup.from(travelGroupSaveForm);
        travelGroupRepository.save(travelGroup);
        return TravelGroupDto.from(travelGroup);
    }

    private void validTravelGroup(TravelGroupSaveForm travelGroupSaveForm) {
        travelGroupRepository.findByGroupName(travelGroupSaveForm.getGroupName()).ifPresent(m -> {
            throw new OmittedRequireFieldException("동일한 travelGroup명이 있습니다.");
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
     * @return TravelGroupDto travelGroup 수정 결과
     */
    public TravelGroupDto modifyTravelGroup(TravelGroupModifyForm travelGroupModifyForm) {
        TravelGroup travelGroup = travelGroupRepository.findByGroupName(travelGroupModifyForm.getGroupName())
            .orElseThrow(() -> new OmittedRequireFieldException("travelGroup을 찾을 수 없습니다."));

        travelGroup.modifyTravelGroup(travelGroupModifyForm);
        return TravelGroupDto.from(travelGroup);
    }
}
