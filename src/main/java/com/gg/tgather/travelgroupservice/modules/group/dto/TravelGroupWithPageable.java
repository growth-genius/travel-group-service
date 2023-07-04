package com.gg.tgather.travelgroupservice.modules.group.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TravelGroupWithPageable {

    private List<TravelGroupDto> travelGroupDtoList;

    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public TravelGroupWithPageable(List<TravelGroupDto> travelGroupDtoList, int pageNo, int pageSize, long totalElements, int totalPages, boolean last) {
        this.travelGroupDtoList = travelGroupDtoList;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public static TravelGroupWithPageable of(List<TravelGroupDto> travelGroupDtoList, int pageNo, int pageSize, long totalElements, int totalPages,
        boolean last) {
        return new TravelGroupWithPageable(travelGroupDtoList, pageNo, pageSize, totalElements, totalPages, last);
    }
}
