package com.gg.tgather.travelgroupservice.modules.form;

import com.gg.tgather.commonservice.enums.TravelTheme;
import java.util.Set;
import lombok.Data;

@Data
public class TravelGroupSearchForm {

    private Set<TravelTheme> travelThemes;

}
