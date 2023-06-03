package com.gg.tgather.travelgroupservice.modules.group;

import com.gg.tgather.commonservice.annotation.RestBaseAnnotation;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.commonservice.utils.ApiUtil;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupModifyForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSearchForm;
import com.gg.tgather.travelgroupservice.modules.group.service.TravelGroupService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestBaseAnnotation
@RequestMapping("/travel-group")
@RequiredArgsConstructor
public class TravelGroupController {

    private final TravelGroupService travelGroupService;

    @PostMapping
    public ApiUtil.ApiResult<TravelGroupDto> createTravelGroup(@RequestBody @Valid TravelGroupSaveForm travelGroupSaveForm,
        @AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiUtil.success(travelGroupService.createTravelGroup(travelGroupSaveForm, authentication));
    }

    @PatchMapping
    public ApiUtil.ApiResult<TravelGroupDto> modifyTravelGroup(@RequestBody @Valid TravelGroupModifyForm travelGroupModifyForm,
        @AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiUtil.success(travelGroupService.modifyTravelGroup(travelGroupModifyForm, authentication));
    }

    @GetMapping
    public ApiUtil.ApiResult<List<TravelGroupDto>> findTravelGroupByTravelThemes(@RequestBody TravelGroupSearchForm travelGroupSearchForm,
        @AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiUtil.success(travelGroupService.findTravelGroupByTheme(travelGroupSearchForm));
    }

    @DeleteMapping("/{travelGroupName}")
    public ApiUtil.ApiResult<Boolean> deleteTravelGroup(@PathVariable String travelGroupName, @AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiUtil.success(travelGroupService.deleteTravelGroup(travelGroupName, authentication));
    }

}
