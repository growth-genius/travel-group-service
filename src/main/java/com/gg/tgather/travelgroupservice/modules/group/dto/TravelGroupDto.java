package com.gg.tgather.travelgroupservice.modules.group.dto;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TravelGroupDto {

    /** 여행그룹 아이디 */
    private String travelGroupId;

    /** 여행그룹명 */
    private String groupName;

    /** 여행테마 */
    private Set<TravelTheme> travelThemes;

    /** 총참여자수 */
    private int totalMember;

    public static TravelGroupDto from(TravelGroup travelGroup) {
        TravelGroupDto travelGroupDTO = new TravelGroupDto();
        travelGroupDTO.travelGroupId = travelGroup.getTravelGroupId();
        travelGroupDTO.groupName = travelGroup.getGroupName();
        travelGroupDTO.travelThemes = new HashSet<>(travelGroup.getTravelThemes());
        travelGroupDTO.totalMember = travelGroup.getTravelGroupMemberList().size();
        return travelGroupDTO;
    }
}
