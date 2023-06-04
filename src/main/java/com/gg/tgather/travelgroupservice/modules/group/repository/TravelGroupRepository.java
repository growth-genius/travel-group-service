package com.gg.tgather.travelgroupservice.modules.group.repository;

import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelGroupRepository extends JpaRepository<TravelGroup, Long>, TravelGroupRepositoryQuerydsl {

    /**
     * 그룹명으로 검색
     *
     * @param groupName 여행그룹명
     * @return Optional<TravelGroup> 여행그룹
     */
    Optional<TravelGroup> findByGroupName(String groupName);

}
