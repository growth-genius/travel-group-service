package com.gg.tgather.travelgroupservice.modules.group.repository;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TravelGroupRepositoryQuerydsl {

    List<TravelGroupDto> searchTravelGroupAllByTravelThemes(Set<TravelTheme> travelThemes);

    Optional<TravelGroup> searchByTravelGroupAndLeader(String groupName, String accountId);
}
