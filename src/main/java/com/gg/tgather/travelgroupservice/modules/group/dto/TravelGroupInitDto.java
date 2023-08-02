package com.gg.tgather.travelgroupservice.modules.group.dto;

import com.gg.tgather.commonservice.enums.EnumMapperValue;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelGroupInitDto {

    List<EnumMapperValue> travelThemes;
}
