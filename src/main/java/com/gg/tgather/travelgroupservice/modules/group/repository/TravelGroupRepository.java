package com.gg.tgather.travelgroupservice.modules.group.repository;

import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 여행그룹 CRUD 수행하기 위한 저장소 인터페이스
 *
 * @author joyeji
 * @since 2023.06.04
 */
public interface TravelGroupRepository extends JpaRepository<TravelGroup, Long>, TravelGroupRepositoryQuerydsl {

    /**
     * 그룹명으로 검색
     *
     * @param groupName 여행그룹명
     * @return Optional<TravelGroup> 여행그룹
     */
    Optional<TravelGroup> findByGroupName(String groupName);

    Optional<TravelGroup> findByTravelGroupId(String travelGroupId);
}
