package com.gg.tgather.travelgroupservice.modules.group.repository;

import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelGroupMemberRepository extends JpaRepository<TravelGroupMember, Long> {

    Optional<TravelGroupMember> findByAccountId(String accountId);

    List<TravelGroupMember> findByTravelGroupId(Long travelGroupId);
}
