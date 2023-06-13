package com.gg.tgather.travelgroupservice.modules.group.form;

import com.gg.tgather.commonservice.enums.TravelTheme;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class TravelGroupSaveForm {

    @NotEmpty(message = "여행 만남명이 누락되었습니다.")
    private String groupName;

    @NotEmpty(message = "여행 테마를 하나 이상 선택 해 주세요.")
    private Set<TravelTheme> travelThemes;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "모임 시작일자를 선택해 주세요.")
    private String startDate;

    /** 여행그룹 공개 여부 확인 */
    private boolean open = true;

    /** 참여자 수 제한 여부 확인 */
    private boolean limitedParticipant = true;

    /** 참여자 제한 수 */
    private int limitParticipantCount = 999;

    /** 테스트용도 생성자 */
    private TravelGroupSaveForm(String travelGroupName, Set<TravelTheme> travelThemes, String startDate) {
        this.groupName = travelGroupName;
        this.travelThemes = travelThemes;
        this.startDate = startDate;
    }

    /** 테스트 용도 생성자 */
    private TravelGroupSaveForm(String travelGroupName, Set<TravelTheme> travelThemes, String startDate, boolean open) {
        this.groupName = travelGroupName;
        this.travelThemes = travelThemes;
        this.startDate = startDate;
        this.open = open;
    }

    /** 테스트용도 */
    public static TravelGroupSaveForm createTravelGroupSaveFormForTest(String travelGroupName) {
        return new TravelGroupSaveForm(travelGroupName, Set.of(TravelTheme.ACTIVITY), "2023-10-01T09:45:00.000+02:00");
    }

    /** 테스트 용도 */
    public static TravelGroupSaveForm createPrivateGroupForTest(String travelGroupName) {
        return new TravelGroupSaveForm(travelGroupName, Set.of(TravelTheme.ACTIVITY), "2023-10-01T09:45:00.000+02:00", false);
    }
}
