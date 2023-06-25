package com.gg.tgather.travelgroupservice.modules.group.controller;

import static com.gg.tgather.commonservice.utils.ApiUtil.success;

import com.gg.tgather.commonservice.annotation.RestBaseAnnotation;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.commonservice.utils.ApiUtil.ApiResult;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupMemberDto;
import com.gg.tgather.travelgroupservice.modules.group.enums.GroupJoinStatus;
import com.gg.tgather.travelgroupservice.modules.group.service.TravelGroupJoinService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestBaseAnnotation
@RequestMapping("/travel-group")
@RequiredArgsConstructor
public class TravelGroupJoinController {

    private final TravelGroupJoinService travelGroupJoinService;

    /**
     * 여행그룹에 가입신청한 유저들 전체보기
     *
     * @param travelGroupId  여행그룹 아이디
     * @param authentication 계정정보
     * @return
     */
    @GetMapping("/group-id/{travelGroupId}/status/{status}/members")
    public ApiResult<List<TravelGroupMemberDto>> getTravelGroupMembersRequest(@PathVariable String travelGroupId, @PathVariable GroupJoinStatus status,
        @AuthenticationPrincipal JwtAuthentication authentication) {
        return success(travelGroupJoinService.getTravelGroupMembersRequest(travelGroupId, status, authentication));
    }

}
