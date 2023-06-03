package com.gg.tgather.travelgroupservice.modules.repository;

import static com.gg.tgather.travelgroupservice.modules.entity.QTravelGroup.travelGroup;
import static com.querydsl.core.types.Projections.constructor;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.commonservice.jpa.Querydsl5Support;
import com.gg.tgather.travelgroupservice.modules.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.entity.TravelGroup;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import java.util.Set;

public class TravelGroupRepositoryImpl extends Querydsl5Support implements TravelGroupRepositoryQuerydsl {

    protected TravelGroupRepositoryImpl() {
        super(TravelGroup.class);
    }


    @Override
    public List<TravelGroupDto> searchTravelGroupAllByTravelThemes(Set<TravelTheme> travelThemes) {
        return select(constructor(TravelGroupDto.class, travelGroup)).from(travelGroup).where(containsTravelGroup(travelThemes)).fetch();
    }

    BooleanExpression containsTravelGroup(Set<TravelTheme> themes) {
        BooleanExpression contains = null;
        for (TravelTheme theme : themes) {
            if (contains == null) {
                contains = travelGroup.travelThemes.contains(theme);
            } else {
                contains.and(travelGroup.travelThemes.contains(theme));
            }
        }
        return contains;
    }


}
