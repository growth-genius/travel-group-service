package com.gg.tgather.travelgroupservice.modules.group.repository;

import static com.gg.tgather.travelgroupservice.modules.group.entity.QTravelGroup.travelGroup;
import static com.gg.tgather.travelgroupservice.modules.group.entity.QTravelGroupMember.travelGroupMember;
import static com.querydsl.core.types.Projections.constructor;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.commonservice.jpa.Querydsl5Support;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupRole;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TravelGroupRepositoryImpl extends Querydsl5Support implements TravelGroupRepositoryQuerydsl {

    protected TravelGroupRepositoryImpl() {
        super(TravelGroup.class);
    }

    @Override
    public List<TravelGroupDto> searchTravelGroupAllByTravelThemes(Set<TravelTheme> travelThemes) {
        return select(constructor(TravelGroupDto.class, travelGroup)).from(travelGroup).where(containsTravelGroup(travelThemes)).fetch();
    }

    @Override
    public Optional<TravelGroup> searchTravelGroupAndLeader(String groupName, String accountId) {
        return Optional.ofNullable(selectFrom(travelGroup).innerJoin(travelGroup.travelGroupMemberList, travelGroupMember).fetchJoin().where(
            travelGroup.groupName.eq(groupName)
                .and(travelGroupMember.travelGroupRole.eq(TravelGroupRole.LEADER).and(travelGroupMember.accountId.eq(accountId)))).fetchFirst());
    }


    BooleanExpression containsTravelGroup(Set<TravelTheme> themes) {
        BooleanExpression contains = null;
        for (TravelTheme theme : themes) {
            if (contains == null) {
                contains = travelGroup.travelThemes.contains(theme).and(travelGroup.deleteTravelGroup.isFalse());
            } else {
                contains.and(travelGroup.travelThemes.contains(theme)).and(travelGroup.deleteTravelGroup.isFalse());
            }
        }
        return contains;
    }


}
