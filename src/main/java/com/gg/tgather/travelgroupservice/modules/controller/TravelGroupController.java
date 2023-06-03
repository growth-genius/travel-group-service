package com.gg.tgather.travelgroupservice.modules.controller;

import com.gg.tgather.commonservice.annotation.RestBaseAnnotation;
import com.gg.tgather.commonservice.utils.ApiUtil;
import com.gg.tgather.travelgroupservice.modules.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.form.TravelGroupSearchForm;
import com.gg.tgather.travelgroupservice.modules.service.TravelGroupService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestBaseAnnotation
@RequestMapping("/travel-group")
@RequiredArgsConstructor
public class TravelGroupController {

    private final TravelGroupService travelGroupService;

    @PostMapping
    public ApiUtil.ApiResult<TravelGroupDto> createTravelGroup(@RequestBody @Valid TravelGroupSaveForm travelGroupSaveForm) {
        return ApiUtil.success(travelGroupService.createTravelGroup(travelGroupSaveForm));
    }

    @GetMapping
    public ApiUtil.ApiResult<List<TravelGroupDto>> findTravelGroupByTravelThemes(@RequestBody TravelGroupSearchForm travelGroupSearchForm) {
        return ApiUtil.success(travelGroupService.findTravelGroupByTheme(travelGroupSearchForm));
    }

}
