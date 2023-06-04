package com.gg.tgather.travelgroupservice.modules.group.dto;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TravelGroupDto {

    /** 여행그룹명 */
    private String groupName;

    /** 여행테마 */
    private Set<TravelTheme> travelThemes;

    /** 총참여자수 */
    private int totalMember;

    public TravelGroupDto(TravelGroup travelGroup) {
        copyProperties(travelGroup, this);
    }

    public static TravelGroupDto from(TravelGroup travelGroup) {
        TravelGroupDto travelGroupDTO = new TravelGroupDto();
        travelGroupDTO.setGroupName(travelGroup.getGroupName());
        travelGroupDTO.setTravelThemes(travelGroup.getTravelThemes());
        travelGroupDTO.setTotalMember(travelGroup.getTravelGroupMemberList().size());
        return travelGroupDTO;
    }
}
