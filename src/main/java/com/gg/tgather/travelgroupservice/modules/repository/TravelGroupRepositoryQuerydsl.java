package com.gg.tgather.travelgroupservice.modules.repository;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.travelgroupservice.modules.dto.TravelGroupDto;
import java.util.List;
import java.util.Set;

public interface TravelGroupRepositoryQuerydsl {

    List<TravelGroupDto> searchTravelGroupAllByTravelThemes(Set<TravelTheme> travelThemes);
}
