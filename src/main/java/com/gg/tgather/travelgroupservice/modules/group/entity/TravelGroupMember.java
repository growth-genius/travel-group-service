package com.gg.tgather.travelgroupservice.modules.group.entity;

import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupJoinForm;
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
    private Long id;

    @Column(unique = true)
    private String travelGroupMemberId;

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
    /** 닉네임 */
    @Column(unique = true)
    private String nickname;
    /** 프로필 이미지 */
    private String profileImage;

    private TravelGroupMember(TravelGroup travelGroup, String accountId, TravelGroupRole travelGroupRole, String nickname, String profileImage) {
        this.travelGroupMemberId = UUID.randomUUID().toString();
        this.travelGroup = travelGroup;
        this.accountId = accountId;
        this.travelGroupRole = travelGroupRole;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.approved = TravelGroupRole.LEADER.equals(travelGroupRole) || travelGroup.isOpen();
    }

    private TravelGroupMember(TravelGroup travelGroup, String accountId, TravelGroupRole travelGroupRole, boolean approved, String nickname,
        String profileImage) {
        this.travelGroupMemberId = UUID.randomUUID().toString();
        this.travelGroup = travelGroup;
        this.accountId = accountId;
        this.travelGroupRole = travelGroupRole;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.approved = approved;
    }

    /**
     * 여행그룹 유저 추가
     *
     * @param travelGroup 여행그룹
     * @param accountId   계정정보
     * @param approved    가입승인여부
     * @return TravelGroupMember 여행그룹에 가입 신청 유저
     */
    public static TravelGroupMember joinTravelGroupMember(TravelGroup travelGroup, String accountId, boolean approved,
        TravelGroupJoinForm travelGroupJoinForm) {
        TravelGroupMember travelGroupMember = of(travelGroup, accountId, approved, travelGroupJoinForm.getNickname(), travelGroupJoinForm.getProfileImage());
        if (!travelGroupMember.addMember()) {
            throw new OmittedRequireFieldException("여행그룹에 참여할 수 없습니다.");
        }
        return travelGroupMember;
    }

    private static TravelGroupMember of(TravelGroup travelGroup, String accountId, boolean approved, String nickname, String profileImage) {
        return new TravelGroupMember(travelGroup, accountId, TravelGroupRole.USER, approved, nickname, profileImage);
    }

    /**
     * 여행그룹 방장 추가
     *
     * @param travelGroup 여행그룹
     * @param accountId   계정정보
     * @return TravelGroupMember 여행그룹 가입 신청 방장
     */
    public static TravelGroupMember createTravelGroupLeader(TravelGroup travelGroup, String accountId, String nickname, String profileImage) {
        TravelGroupMember travelGroupMember = of(travelGroup, accountId, nickname, profileImage);
        travelGroup.getTravelGroupMemberList().add(travelGroupMember);
        return travelGroupMember;
    }

    /**
     * 여행그룹 멤버 리더 추가
     *
     * @param travelGroup 여행그룹명
     * @param accountId   계정 아이디
     * @return 여행그룹 리더
     */
    private static TravelGroupMember of(TravelGroup travelGroup, String accountId, String nickname, String profileImage) {
        return new TravelGroupMember(travelGroup, accountId, TravelGroupRole.LEADER, nickname, profileImage);
    }

    public boolean addMember() {
        if (this.travelGroup.plusParticipant()) {
            this.travelGroup.getTravelGroupMemberList().add(this);
            return true;
        }
        return false;
    }

    public boolean isAvailableRemoveMember() {
        if (this.travelGroup.minusParticipant()) {
            this.travelGroup.getTravelGroupMemberList().remove(this);
            return true;
        }
        return false;
    }

}
