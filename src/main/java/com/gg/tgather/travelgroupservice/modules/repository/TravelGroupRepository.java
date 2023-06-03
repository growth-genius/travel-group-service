package com.gg.tgather.travelgroupservice.modules.repository;

import com.gg.tgather.travelgroupservice.modules.entity.TravelGroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelGroupRepository extends JpaRepository<TravelGroup, Long>, TravelGroupRepositoryQuerydsl {

    Optional<TravelGroup> findByGroupName(String groupName);

}
