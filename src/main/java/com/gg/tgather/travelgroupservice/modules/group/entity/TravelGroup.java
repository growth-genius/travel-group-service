package com.gg.tgather.travelgroupservice.modules.group.entity;

import static com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup.ParticipantCount.MAX_PARTICIPANT_COUNT;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.commonservice.jpa.UpdatedEntity;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupModifyForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@NamedEntityGraph(name = "TravelGroup.withTravelThemes", attributeNodes = {@NamedAttributeNode("travelThemes")})
public class TravelGroup extends UpdatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 여행 만남 이름 */
    @Column(unique = true)
    private String groupName;

    /* 여행 테마 (복수 선택 가능 )*/
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "travel_themes", joinColumns = @JoinColumn(name = "travel_group_id"))
    private Set<TravelTheme> travelThemes;

    /** 만남 참여자 수 */
    private long participantCount = 1;

    /** 공개 여부 */
    private boolean open = true;

    /** 참여자 수 제한 **/
    private long limitParticipantCount;

    /** 여행 시작일 */
    private String startDate;

    /* 권한 그룹 소속 유저들 */
    @OneToMany(mappedBy = "travelGroup", fetch = FetchType.LAZY)
    private final List<TravelGroupMember> travelGroupMemberList = new ArrayList<>();

    /** 삭제 여부 */
    private boolean deleteTravelGroup;

    /** 삭제 시점 */
    private LocalDateTime deleteTravelGroupAt;

    public TravelGroup(TravelGroupSaveForm travelGroupSaveForm) {
        this.groupName = travelGroupSaveForm.getGroupName();
        this.travelThemes = travelGroupSaveForm.getTravelThemes();
        this.startDate = travelGroupSaveForm.getStartDate();
        this.open = travelGroupSaveForm.isOpen();
        this.limitParticipantCount =
            travelGroupSaveForm.isLimitedParticipant() ? travelGroupSaveForm.getLimitParticipantCount() : MAX_PARTICIPANT_COUNT.getCount();
    }

    public static TravelGroup from(TravelGroupSaveForm travelGroupSaveForm) {
        return new TravelGroup(travelGroupSaveForm);
    }

    public boolean plusParticipant() {
        if (this.limitParticipantCount + 1 == this.participantCount) {
            return false;
        }
        this.participantCount++;
        return true;
    }

    public boolean minusParticipant() {
        if (this.participantCount > 1) {
            this.participantCount--;
            return true;
        }
        return false;
    }

    public void modifyTravelGroup(TravelGroupModifyForm travelGroupModifyForm) {
        this.travelThemes = travelGroupModifyForm.getTravelThemes();
        this.startDate = travelGroupModifyForm.getStartDate();
        this.open = travelGroupModifyForm.isOpen();
        this.limitParticipantCount =
            travelGroupModifyForm.isLimitedParticipant() ? travelGroupModifyForm.getLimitParticipantCount() : MAX_PARTICIPANT_COUNT.getCount();
    }

    public void deleteTravelGroup() {
        this.deleteTravelGroup = true;
        this.deleteTravelGroupAt = LocalDateTime.now();
    }

    enum ParticipantCount {
        MAX_PARTICIPANT_COUNT(999);

        private final int count;

        ParticipantCount(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }
}