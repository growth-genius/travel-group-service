package com.gg.tgather.travelgroupservice.modules.group.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelGroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_group_member_id")
    private Long id;
    /* 여행 그룹 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_group_id")
    private TravelGroup travelGroup;

    private String accountId;
    /* 여행 그룹 권한 */
    @Enumerated(EnumType.STRING)
    private TravelGroupRole travelGroupRole;

    private boolean approved;

    private TravelGroupMember(TravelGroup travelGroup, String accountId, TravelGroupRole travelGroupRole) {
        this.travelGroup = travelGroup;
        this.accountId = accountId;
        this.travelGroupRole = travelGroupRole;
        this.approved = TravelGroupRole.LEADER.equals(travelGroupRole) || travelGroup.isOpen();
    }

    static TravelGroupMember of(TravelGroup travelGroup, String accountId, TravelGroupRole travelGroupRole) {
        return new TravelGroupMember(travelGroup, accountId, travelGroupRole);
    }

    public static TravelGroupMember createTravelGroupLeader(TravelGroup travelGroup, String accountId) {
        return of(travelGroup, accountId, TravelGroupRole.LEADER);
    }

    public boolean addMember() {
        if (this.travelGroup.plusParticipant()) {
            this.travelGroup.getTravelGroupMemberList().add(this);
            return true;
        }
        return false;
    }

    public boolean removeMember() {
        if (this.travelGroup.minusParticipant()) {
            this.travelGroup.getTravelGroupMemberList().remove(this);
            return true;
        }
        return false;
    }

}
