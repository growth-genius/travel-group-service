package com.gg.tgather.travelgroupservice.modules.group.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gg.tgather.travelgroupservice.infra.annotation.ServiceTest;
import com.gg.tgather.travelgroupservice.infra.container.AbstractContainerBaseTest;
import com.gg.tgather.travelgroupservice.infra.security.WithMockJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.common.AbstractJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupMemberDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupMemberRepository;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@WithMockJwtAuthentication
class TravelGroupMemberServiceTest extends AbstractContainerBaseTest implements AbstractJwtAuthentication {

    @Autowired
    private TravelGroupMemberService travelGroupMemberService;

    @Autowired
    private TravelGroupService travelGroupService;

    @Autowired
    private TravelGroupRepository travelGroupRepository;

    @Autowired
    private TravelGroupMemberRepository travelGroupMemberRepository;


    /**
     * 공개 여행그룹 저장
     *
     * @return 여행그룹 엔티티
     */
    private TravelGroup createPublicTravelGroup() {
        TravelGroupSaveForm travelGroupSaveForm = TravelGroupSaveForm.createTravelGroupSaveFormForTest("Travel");
        travelGroupSaveForm.setOpen(true);
        TravelGroup travelGroup = TravelGroup.from(travelGroupSaveForm);
        travelGroupRepository.save(travelGroup);
        TravelGroupMember travelGroupMember = TravelGroupMember.createTravelGroupLeader(travelGroup, "TEST-ACCOUNT-ID");
        travelGroupMemberRepository.save(travelGroupMember);
        return travelGroup;
    }


    /**
     * 여행그룹 멤버 추가
     *
     * @param travelGroup 여행그룹
     */
    private TravelGroupMember travelGroupAddMember(TravelGroup travelGroup) {
        TravelGroupMember travelGroupMember = TravelGroupMember.joinTravelGroupMember(travelGroup, "Member", true);
        travelGroupMemberRepository.save(travelGroupMember);
        assertEquals(travelGroup, travelGroupMember.getTravelGroup());
        return travelGroupMember;
    }

    @Test
    @DisplayName("여행그룹 가입시 승인처리")
    void whenRequestTravelGroupJoin_thenSuccess() {
        // given
        TravelGroup travelGroup = createPublicTravelGroup();
        // when
        TravelGroupMemberDto travelGroupMemberDto = travelGroupMemberService.requestTravelGroupJoin(travelGroup.getId(), getCommonAuthentication());
        Optional<TravelGroup> validTravelGroup = travelGroupRepository.findById(travelGroup.getId());
        assertTrue(validTravelGroup.isPresent());
        // then
        assertTrue(travelGroupMemberDto.isApproved());
        assertNotNull(travelGroupMemberDto.getTravelGroupMemberId());
        assertEquals(2, validTravelGroup.get().getTravelGroupMemberList().size());
    }


    @Test
    @DisplayName("여행그룹에서 방장이 사용자를 탈퇴 요청시 성공")
    void whenWithdrawMemberByLeader_thenSuccess() {
        // given
        TravelGroupSaveForm travelGroupSaveForm = TravelGroupSaveForm.createTravelGroupSaveFormForTest("Travel");
        Long travelGroupId = travelGroupService.createTravelGroup(travelGroupSaveForm, getCommonAuthentication()).getTravelGroupId();
        Optional<TravelGroup> optionalTravelGroup = travelGroupRepository.findById(travelGroupId);
        assertTrue(optionalTravelGroup.isPresent());
        TravelGroupMember travelGroupMember = travelGroupAddMember(optionalTravelGroup.get());
        // when
        Boolean disableMember = travelGroupMemberService.deleteTravelGroupMember(travelGroupId, travelGroupMember.getId(), getCommonAuthentication());
        // then
        assertTrue(disableMember);
    }


}