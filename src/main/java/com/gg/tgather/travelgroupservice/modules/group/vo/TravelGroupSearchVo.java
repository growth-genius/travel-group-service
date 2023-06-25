package com.gg.tgather.travelgroupservice.modules.group.vo;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupRole;
import lombok.Data;

@Data
public class TravelGroupSearchVo {

    /** 여행그룹 아이디 */
    private String travelGroupId;

    /** 여행그룹명 */
    private String groupName;

    /** 여행테마 */
    private TravelTheme travelTheme;

    /** 여행 그룹 멤버 아이디 */
    private String travelGroupMemberId;

    /** 사용자 아이디 */
    private String accountId;

    /** 사용자 권한 */
    private TravelGroupRole travelGroupRole;

    /** 승인 여부 */
    private boolean approved;

}
