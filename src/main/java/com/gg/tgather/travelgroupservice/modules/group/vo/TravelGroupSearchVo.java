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

    private String travelGroupMemberId;

    private String accountId;

    private TravelGroupRole travelGroupRole;

    private boolean approved;

}
