package com.gg.tgather.travelgroupservice.modules.group.controller;

import static com.gg.tgather.commonservice.utils.ApiUtil.success;

import com.gg.tgather.commonservice.annotation.RestBaseAnnotation;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.commonservice.utils.ApiUtil.ApiResult;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupRegisterInitDto;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupModifyForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
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

/**
 * 여행그룹 CRUD Restful API
 *
 * @author joyeji
 * @since 2023.06.04
 */
@RestBaseAnnotation
@RequiredArgsConstructor
public class TravelGroupController {

    private final TravelGroupService travelGroupService;

    /**
     * 여행 그룹 생성 전 초기 조회
     *
     * @return TravelGroupRegisterInitDto
     */
    @GetMapping("/register/init")
    public ApiResult<TravelGroupRegisterInitDto> getRegisterInit() {
        return success(travelGroupService.findRegisterInitData(), "조회되었습니다.");
    }

    /**
     * 여행 그룹 생성 API
     *
     * @param travelGroupSaveForm 여행 그룹 생성 폼
     * @param authentication      계정 인증
     * @return TravelGroupDto 여행그룹 생성 결과
     */
    @PostMapping("/group")
    public ApiResult<TravelGroupDto> createTravelGroup(@RequestBody @Valid TravelGroupSaveForm travelGroupSaveForm,
        @AuthenticationPrincipal JwtAuthentication authentication) {
        return success(travelGroupService.createTravelGroup(travelGroupSaveForm, authentication));
    }

    /**
     * 여행 그룹 수정 API
     *
     * @param travelGroupModifyForm 여행 그룹 수정 폼
     * @param authentication        계정 인증
     * @return TravelGroupDto 여행그룹 수정 결과
     */
    @PatchMapping("/{travelGroupId}")
    public ApiResult<TravelGroupDto> modifyTravelGroup(@PathVariable String travelGroupId, @RequestBody @Valid TravelGroupModifyForm travelGroupModifyForm,
        @AuthenticationPrincipal JwtAuthentication authentication) {
        return success(travelGroupService.modifyTravelGroup(travelGroupId, travelGroupModifyForm, authentication));
    }

    /**
     * 단일 여행 그룹 조회 API
     * 여행그룹의 디테일한 정보를 얻기 위한 목적으로 생성된 API
     *
     * @param authentication 계정 인증
     * @return TravelGroupDto 여행그룹 단일 조회 결과
     */
    @GetMapping("/{travelGroupId}")
    public ApiResult<TravelGroupDto> findTravelGroup(@PathVariable String travelGroupId, @AuthenticationPrincipal JwtAuthentication authentication) {
        return success(travelGroupService.findTravelGroup(travelGroupId, authentication.accountId()));
    }

    /**
     * 로그인 사용자의 여행그룹 조회
     *
     * @param authentication 로그인 사용자 정보
     * @return List<TravelGroupDto> 조회된 여행 그룹 목록
     */
    @GetMapping("/own")
    public ApiResult<List<TravelGroupDto>> findTravelGroupByOwn(@AuthenticationPrincipal JwtAuthentication authentication) {
        return success(travelGroupService.findAllTravelGroupByOwn(authentication.accountId()));
    }


    /**
     * 여행그룹 삭제 API
     *
     * @param travelGroupId  여행그룹 아이디
     * @param authentication 계정 인증
     * @return Boolean 여행그룹 삭제 결과
     */
    @DeleteMapping("/{travelGroupId}")
    public ApiResult<Boolean> deleteTravelGroup(@PathVariable String travelGroupId, @AuthenticationPrincipal JwtAuthentication authentication) {
        return success(travelGroupService.deleteTravelGroup(travelGroupId, authentication));
    }

}
