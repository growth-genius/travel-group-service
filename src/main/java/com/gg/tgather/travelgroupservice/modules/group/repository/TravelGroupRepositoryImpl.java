package com.gg.tgather.travelgroupservice.modules.group.repository;

import static com.gg.tgather.travelgroupservice.modules.group.entity.QTravelGroup.travelGroup;
import static com.gg.tgather.travelgroupservice.modules.group.entity.QTravelGroupMember.travelGroupMember;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.commonservice.jpa.Querydsl5Support;
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
    public List<TravelGroup> searchTravelGroupAllByTravelThemes(Set<TravelTheme> travelThemes) {
        return selectFrom(travelGroup).where(containsTravelGroup(travelThemes)).fetch();
    }

    @Override
    public Optional<TravelGroup> searchByTravelGroupAndLeader(Long travelGroupId, String accountId) {
        return Optional.ofNullable(selectFrom(travelGroup).innerJoin(travelGroupMember).on(travelGroupMember.travelGroup.eq(travelGroup)).fetchJoin().where(
                travelGroup.id.eq(travelGroupId).and(travelGroupMember.accountId.eq(accountId)).and(travelGroupMember.travelGroupRole.eq(TravelGroupRole.LEADER)))
            .fetchOne());
    }

    @Override
    public Optional<TravelGroup> searchByTravelGroupNameWithoutOwn(String travelGroupName, Long travelGroupId) {
        return Optional.ofNullable(selectFrom(travelGroup).innerJoin(travelGroup.travelGroupMemberList, travelGroupMember).fetchJoin()
            .where(travelGroup.groupName.eq(travelGroupName).and(travelGroup.id.ne(travelGroupId))).fetchOne());
    }

    @Override
    public Optional<TravelGroup> searchTravelGroupByIdWithLeader(Long groupId) {
        return Optional.ofNullable(selectFrom(travelGroup).innerJoin(travelGroup.travelGroupMemberList, travelGroupMember).fetchJoin()
            .where(travelGroup.id.eq(groupId).and(travelGroupMember.travelGroupRole.eq(TravelGroupRole.LEADER))).fetchOne());
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
