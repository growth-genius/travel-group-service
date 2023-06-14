package com.gg.tgather.travelgroupservice.modules.group;

import static com.gg.tgather.travelgroupservice.modules.group.enums.GroupJoinStatus.NO_APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gg.tgather.travelgroupservice.infra.annotation.MockMvcTest;
import com.gg.tgather.travelgroupservice.infra.container.AbstractContainerBaseTest;
import com.gg.tgather.travelgroupservice.infra.security.WithMockJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.common.AbstractJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupMemberRepository;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@MockMvcTest
@WithMockJwtAuthentication
class TravelGroupJoinControllerTest extends AbstractContainerBaseTest implements AbstractJwtAuthentication {

    @Autowired
    private TravelGroupRepository travelGroupRepository;

    @Autowired
    private TravelGroupMemberRepository travelGroupMemberRepository;

    @Autowired
    private MockMvc mockMvc;

    private TravelGroup savePrivateTravelGroupTest() {
        TravelGroup travelGroup = saveTravelGroup();
        Optional<TravelGroup> opTravelGroup = travelGroupRepository.findById(travelGroup.getId());
        assertTrue(opTravelGroup.isPresent());
        TravelGroupMember travelGroupLeader = saveTravelGroupLeader(travelGroup);
        assertEquals(travelGroup, travelGroupLeader.getTravelGroup());
        TravelGroupMember travelGroupMember = TravelGroupMember.joinTravelGroupMember(travelGroup, "Member", false);
        travelGroupMemberRepository.save(travelGroupMember);
        assertEquals(travelGroup, travelGroupMember.getTravelGroup());
        return travelGroup;
    }

    @Test
    @DisplayName("비공개 그룹 승인 요청 멤버들 확인")
    void test_case_1() throws Exception {
        // given
        TravelGroup travelGroup = savePrivateTravelGroupTest();
        mockMvc.perform(
                get("/travel-group/group-id/{travelGroupId}/status/{status}/members", travelGroup.getId(), NO_APPROVED).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.response").isArray()).andExpect(jsonPath("$.response[0].accountId").value("Member"))
            .andExpect(jsonPath("$.response[0].approved").isBoolean()).andExpect(jsonPath("$.response[0].approved").value(false));
    }

    @NotNull
    private TravelGroupMember saveTravelGroupLeader(TravelGroup travelGroup) {
        TravelGroupMember travelGroupLeader = TravelGroupMember.createTravelGroupLeader(travelGroup, getCommonAuthentication().accountId());
        travelGroupMemberRepository.save(travelGroupLeader);
        return travelGroupLeader;
    }

    @NotNull
    private TravelGroup saveTravelGroup() {
        TravelGroupSaveForm travelGroupSaveForm = TravelGroupSaveForm.createPrivateGroupForTest("Travel");
        TravelGroup travelGroup = TravelGroup.from(travelGroupSaveForm);
        travelGroupRepository.save(travelGroup);
        return travelGroup;
    }
}