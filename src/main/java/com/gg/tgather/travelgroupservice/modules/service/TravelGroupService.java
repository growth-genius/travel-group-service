package com.gg.tgather.travelgroupservice.modules.service;

import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.travelgroupservice.modules.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.form.TravelGroupSearchForm;
import com.gg.tgather.travelgroupservice.modules.repository.TravelGroupRepository;
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
     * @param travelGroupSearchForm
     * @return
     */
    public List<TravelGroupDto> findTravelGroupByTheme(TravelGroupSearchForm travelGroupSearchForm) {
        return travelGroupRepository.searchTravelGroupAllByTravelThemes(travelGroupSearchForm.getTravelThemes());
    }

}
