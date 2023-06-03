package com.gg.tgather.travelgroupservice.modules.dto;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.travelgroupservice.modules.entity.TravelGroup;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TravelGroupDto {

    private String groupName;

    private Set<TravelTheme> travelThemes;

    private int totalMember;

    public TravelGroupDto( TravelGroup travelGroup ) {
        copyProperties( travelGroup, this );
    }

    public static TravelGroupDto from ( TravelGroup travelGroup ) {
        TravelGroupDto travelGroupDTO = new TravelGroupDto();
        travelGroupDTO.setGroupName( travelGroup.getGroupName() );
        travelGroupDTO.setTravelThemes( travelGroup.getTravelThemes() );
        travelGroupDTO.setTotalMember( travelGroup.getTravelGroupMemberList().size() );
        return travelGroupDTO;
    }
}
