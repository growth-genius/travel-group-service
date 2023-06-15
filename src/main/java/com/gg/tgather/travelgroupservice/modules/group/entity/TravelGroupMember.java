package com.gg.tgather.travelgroupservice.modules.group.entity;

import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
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
import java.util.UUID;
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

    @Column(unique = true)
    private String groupMemberId;

    /* 여행 그룹 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_group_id")
    private TravelGroup travelGroup;
    /** 계정 아이디 */
    private String accountId;
    /* 여행 그룹 권한 */
    @Enumerated(EnumType.STRING)
    private TravelGroupRole travelGroupRole;
    /** 유저 승인 여부 */
    private boolean approved;

    private TravelGroupMember(TravelGroup travelGroup, String accountId, TravelGroupRole travelGroupRole) {
        this.groupMemberId = UUID.randomUUID().toString();
        this.travelGroup = travelGroup;
        this.accountId = accountId;
        this.travelGroupRole = travelGroupRole;
        this.approved = TravelGroupRole.LEADER.equals(travelGroupRole) || travelGroup.isOpen();
    }

    private TravelGroupMember(TravelGroup travelGroup, String accountId, TravelGroupRole travelGroupRole, boolean approved) {
        this.groupMemberId = UUID.randomUUID().toString();
        this.travelGroup = travelGroup;
        this.accountId = accountId;
        this.travelGroupRole = travelGroupRole;
        this.approved = approved;
    }

    /**
     * 여행그룹 멤버 리더 추가
     *
     * @param travelGroup 여행그룹명
     * @param accountId   계정 아이디
     * @return 여행그룹 리더
     */
    private static TravelGroupMember of(TravelGroup travelGroup, String accountId) {
        return new TravelGroupMember(travelGroup, accountId, TravelGroupRole.LEADER);
    }

    private static TravelGroupMember of(TravelGroup travelGroup, String accountId, boolean approved) {
        return new TravelGroupMember(travelGroup, accountId, TravelGroupRole.USER, approved);
    }

    /**
     * 여행그룹 방장 추가
     *
     * @param travelGroup 여행그룹
     * @param accountId   계정정보
     * @return TravelGroupMember 여행그룹 가입 신청 방장
     */
    public static TravelGroupMember createTravelGroupLeader(TravelGroup travelGroup, String accountId) {
        TravelGroupMember travelGroupMember = of(travelGroup, accountId);
        travelGroup.getTravelGroupMemberList().add(travelGroupMember);
        return travelGroupMember;
    }

    /**
     * 여행그룹 유저 추가
     *
     * @param travelGroup 여행그룹
     * @param accountId   계정정보
     * @param approved    가입승인여부
     * @return TravelGroupMember 여행그룹에 가입 신청 유저
     */
    public static TravelGroupMember joinTravelGroupMember(TravelGroup travelGroup, String accountId, boolean approved) {
        TravelGroupMember travelGroupMember = of(travelGroup, accountId, approved);
        if (!travelGroupMember.addMember()) {
            throw new OmittedRequireFieldException("여행그룹에 참여할 수 없습니다.");
        }
        return travelGroupMember;
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
