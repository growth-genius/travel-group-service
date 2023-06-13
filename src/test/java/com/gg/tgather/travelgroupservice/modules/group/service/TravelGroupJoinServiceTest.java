package com.gg.tgather.travelgroupservice.modules.group.service;

import static com.gg.tgather.travelgroupservice.modules.group.enums.GroupJoinStatus.APPROVED;
import static com.gg.tgather.travelgroupservice.modules.group.enums.GroupJoinStatus.NO_APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.commonservice.security.JwtAuthenticationToken;
import com.gg.tgather.travelgroupservice.infra.annotation.ServiceTest;
import com.gg.tgather.travelgroupservice.infra.container.AbstractContainerBaseTest;
import com.gg.tgather.travelgroupservice.infra.security.WithMockJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupMemberDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupMemberRepository;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@ServiceTest
@WithMockJwtAuthentication
class TravelGroupJoinServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private TravelGroupJoinService travelGroupJoinService;

    @Autowired
    private TravelGroupRepository travelGroupRepository;

    @Autowired
    private TravelGroupMemberRepository travelGroupMemberRepository;

    private JwtAuthentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return (JwtAuthentication) authenticationToken.getPrincipal();
    }

    @Test
    @DisplayName("비공개 여행그룹 신청자 확인")
    void privateTravelGroupTest() {
        // given
        TravelGroup travelGroup = savePrivateTravelGroup();
        travelGroupAddLeader(travelGroup);
        travelGroupAddMember(travelGroup, false);
        // when
        List<TravelGroupMemberDto> travelGroupMembersRequest = travelGroupJoinService.getTravelGroupMembersRequest(travelGroup.getId(), NO_APPROVED,
            getAuthentication());
        // then
        assertEquals(1, travelGroupMembersRequest.size());
        assertEquals("Member", travelGroupMembersRequest.get(0).getAccountId());
    }

    @Test
    @DisplayName("여행그룹 활동중인 유저 확인")
    void activityMemberTest() {
        // given
        TravelGroup travelGroup = savePublicTravelGroup();
        travelGroupAddLeader(travelGroup);
        travelGroupAddMember(travelGroup, true);
        // when
        List<TravelGroupMemberDto> travelGroupMembersRequest = travelGroupJoinService.getTravelGroupMembersRequest(travelGroup.getId(), APPROVED,
            getAuthentication());
        // then
        assertEquals(2, travelGroupMembersRequest.size());
        assertEquals("Member", travelGroupMembersRequest.get(1).getAccountId());
    }

    private void travelGroupAddMember(TravelGroup travelGroup, boolean approved) {
        TravelGroupMember travelGroupMember = TravelGroupMember.joinTravelGroupMember(travelGroup, "Member", approved);
        travelGroupMemberRepository.save(travelGroupMember);
        assertEquals(travelGroup, travelGroupMember.getTravelGroup());
    }

    private void travelGroupAddLeader(TravelGroup travelGroup) {
        Optional<TravelGroup> opTravelGroup = travelGroupRepository.findById(travelGroup.getId());
        assertTrue(opTravelGroup.isPresent());
        TravelGroupMember travelGroupLeader = saveTravelGroupLeader(travelGroup);
        assertEquals(travelGroup, travelGroupLeader.getTravelGroup());
    }


    @NotNull
    private TravelGroupMember saveTravelGroupLeader(TravelGroup travelGroup) {
        TravelGroupMember travelGroupLeader = TravelGroupMember.createTravelGroupLeader(travelGroup, getAuthentication().accountId());
        travelGroupMemberRepository.save(travelGroupLeader);
        return travelGroupLeader;
    }

    @NotNull
    private TravelGroup savePublicTravelGroup() {
        TravelGroupSaveForm travelGroupSaveForm = TravelGroupSaveForm.createTravelGroupSaveFormForTest("Travel");
        TravelGroup travelGroup = TravelGroup.from(travelGroupSaveForm);
        travelGroupRepository.save(travelGroup);
        return travelGroup;
    }

    @NotNull
    private TravelGroup savePrivateTravelGroup() {
        TravelGroupSaveForm travelGroupSaveForm = TravelGroupSaveForm.createPrivateGroupForTest("Travel");
        TravelGroup travelGroup = TravelGroup.from(travelGroupSaveForm);
        travelGroupRepository.save(travelGroup);
        return travelGroup;
    }
}