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
import jakarta.validation.constraints.NotNull;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 여행그룹 CRUD Restful API
 *
 * @author joyeji
 * @since 2023.06.04
 */
@RestBaseAnnotation
@RequestMapping("/travel-group")
@RequiredArgsConstructor
public class TravelGroupController {

    private final TravelGroupService travelGroupService;

    /**
     * 여행 그룹 생성 API
     *
     * @param travelGroupSaveForm 여행 그룹 생성 폼
     * @param authentication      계정 인증
     * @return TravelGroupDto 여행그룹 생성 결과
     */
    @PostMapping
    public ApiUtil.ApiResult<TravelGroupDto> createTravelGroup(@RequestBody @Valid TravelGroupSaveForm travelGroupSaveForm,
        @AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiUtil.success(travelGroupService.createTravelGroup(travelGroupSaveForm, authentication));
    }

    /**
     * 여행 그룹 수정 API
     *
     * @param travelGroupModifyForm 여행 그룹 수정 폼
     * @param authentication        계정 인증
     * @return TravelGroupDto 여행그룹 수정 결과
     */
    @PatchMapping("/{travelGroupId}")
    public ApiUtil.ApiResult<TravelGroupDto> modifyTravelGroup(@PathVariable @NotNull Long travelGroupId,
        @RequestBody @Valid TravelGroupModifyForm travelGroupModifyForm, @AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiUtil.success(travelGroupService.modifyTravelGroup(travelGroupId, travelGroupModifyForm, authentication));
    }

    /**
     * 여행테마로 그룹 찾기 API
     *
     * @param travelGroupSearchForm 여행 그룹 검색 폼
     * @param authentication        계정 인증
     * @return List<TravelGroupDto> 조건에 부합한 여행그룹들
     */
    @GetMapping
    public ApiUtil.ApiResult<List<TravelGroupDto>> findTravelGroupByTravelThemes(@RequestParam TravelGroupSearchForm travelGroupSearchForm,
        @AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiUtil.success(travelGroupService.findTravelGroupByTheme(travelGroupSearchForm));
    }

    /**
     * 여행그룹 삭제 API
     *
     * @param travelGroupId  여행그룹명
     * @param authentication 계정 인증
     * @return Boolean 여행그룹 삭제 결과
     */
    @DeleteMapping("/{travelGroupId}")
    public ApiUtil.ApiResult<Boolean> deleteTravelGroup(@PathVariable Long travelGroupId, @AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiUtil.success(travelGroupService.deleteTravelGroup(travelGroupId, authentication));
    }

}
