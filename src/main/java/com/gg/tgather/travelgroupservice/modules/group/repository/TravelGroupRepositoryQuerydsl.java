package com.gg.tgather.travelgroupservice.modules.group.repository;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import java.util.List;
import java.util.Set;

public interface TravelGroupRepositoryQuerydsl {

    List<TravelGroupDto> searchTravelGroupAllByTravelThemes(Set<TravelTheme> travelThemes);
}
