package com.gg.tgather.travelgroupservice.modules.group.dto;

import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupRole;
import com.gg.tgather.travelgroupservice.modules.group.vo.TravelGroupSearchVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TravelGroupMemberDto {

    /** 여행그룹 가입자 아이디 */
    private String travelGroupMemberId;
    /** 계정정보 */
    private String accountId;
    /** 여행그룹 승인 여부 */
    private boolean approved;
    /** 권한 */
    private TravelGroupRole travelGroupRole;

    public TravelGroupMemberDto(TravelGroupMember travelGroupMember) {
        this.travelGroupMemberId = travelGroupMember.getTravelGroupMemberId();
        this.accountId = travelGroupMember.getAccountId();
        this.approved = travelGroupMember.isApproved();
    }

    public static TravelGroupMemberDto from(TravelGroupMember travelGroupMember) {
        return new TravelGroupMemberDto(travelGroupMember);
    }

    public static TravelGroupMemberDto from(TravelGroupSearchVo member) {
        TravelGroupMemberDto memberDto = new TravelGroupMemberDto();
        memberDto.travelGroupMemberId = member.getTravelGroupMemberId();
        memberDto.travelGroupRole = member.getTravelGroupRole();
        memberDto.accountId = member.getAccountId();
        memberDto.approved = member.isApproved();
        return memberDto;
    }
}
