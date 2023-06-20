package com.gg.tgather.travelgroupservice.modules.group.dto;

import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TravelGroupMemberDto {

    /** 여행그룹 가입자 아이디 */
    private Long travelGroupMemberId;
    /** 계정정보 */
    private String accountId;
    /** 여행그룹 승인 여부 */
    private boolean approved;

    public TravelGroupMemberDto(TravelGroupMember travelGroupMember) {
        this.travelGroupMemberId = travelGroupMember.getId();
        this.accountId = travelGroupMember.getAccountId();
        this.approved = travelGroupMember.isApproved();
    }

    public static TravelGroupMemberDto from(TravelGroupMember travelGroupMember) {
        return new TravelGroupMemberDto(travelGroupMember);
    }
}
