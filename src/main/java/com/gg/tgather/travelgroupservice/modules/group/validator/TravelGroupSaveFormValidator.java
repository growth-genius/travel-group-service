package com.gg.tgather.travelgroupservice.modules.group.validator;


import com.gg.tgather.commonservice.utils.TimeUtil;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class TravelGroupSaveFormValidator implements Validator {

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return TravelGroupSaveForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        TravelGroupSaveForm travelGroupSaveForm = (TravelGroupSaveForm) target;
        if (LocalDate.now().isBefore(TimeUtil.formatDateTime(travelGroupSaveForm.getStartDate()))) {
            errors.rejectValue("travelGroupStartedAt", "wrong.value", "현재날짜보다 travelGroup 시작일자가 앞설 수 없습니다. 시작일자를 다시 설정해주세요.");
        }
    }
}
