package com.gg.tgather.travelgroupservice.modules.group.form;

import com.gg.tgather.commonservice.enums.TravelTheme;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;

@Data
public class TravelGroupModifyForm {

    @NotEmpty(message = "여행 그룹명이 누락되었습니다.")
    private String groupName;

    @NotEmpty(message = "여행 테마를 하나 이상 선택 해주세요.")
    private Set<TravelTheme> travelThemes;

    @NotNull(message = "모임 시작일자를 선택해 주세요.")
    private String startDate;

    /** 여행그룹 공개 여부 확인 */
    private boolean open;

    /** 참여자 수 제한 여부 확인 */
    private boolean limitedParticipant;

    /** 참여자 제한 수 */
    private int limitParticipantCount;
}
