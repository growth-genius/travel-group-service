package com.gg.tgather.travelgroupservice.modules.group.form;

import com.gg.tgather.commonservice.enums.TravelTheme;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TravelGroupSearchForm {

    private Set<TravelTheme> travelThemes;

}
