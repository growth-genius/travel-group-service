package com.gg.tgather.travelgroupservice.modules.group.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.commonservice.security.JwtAuthenticationToken;
import com.gg.tgather.travelgroupservice.infra.annotation.ServiceTest;
import com.gg.tgather.travelgroupservice.infra.container.AbstractContainerBaseTest;
import com.gg.tgather.travelgroupservice.infra.security.WithMockJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.service.TravelGroupService;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ServiceTest
@WithMockJwtAuthentication
class TravelGroupRepositoryImplTest extends AbstractContainerBaseTest {

    @Autowired
    private TravelGroupRepository travelGroupRepository;

    @Autowired
    private TravelGroupService travelGroupService;

    private void createTravelGroup() {
        TravelGroupSaveForm travelGroupSaveForm = new TravelGroupSaveForm();
        String travelGroupName = "전국 여행일지";
        travelGroupSaveForm.setGroupName(travelGroupName);
        travelGroupSaveForm.setTravelThemes(Set.of(TravelTheme.ACTIVITY));
        travelGroupSaveForm.setStartDate("2023-10-01T09:45:00.000+02:00");
        travelGroupService.createTravelGroup(travelGroupSaveForm, getAuthentication());
    }

    private JwtAuthentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return (JwtAuthentication) authenticationToken.getPrincipal();
    }

    @Test
    @DisplayName("travelGroup 조회확인")
    void test_case_1() {
        // given
        String travelGroupName = "전국 여행일지";
        createTravelGroup();
        // when
        TravelGroup travelGroup = travelGroupRepository.searchTravelGroupAndLeader(travelGroupName, getAuthentication().accountId()).orElseThrow();
        // then
        assertEquals(travelGroupName, travelGroup.getGroupName());
    }

    @Test
    @DisplayName("travelGroup 삭제 확인")
    void travelGroupDelete() {
        // given
        String travelGroupName = "전국 여행일지";
        createTravelGroup();
        // when
        TravelGroup travelGroup = travelGroupRepository.searchTravelGroupAndLeader(travelGroupName, getAuthentication().accountId()).orElseThrow();
        travelGroup.deleteTravelGroup();
        // then
        assertTrue(travelGroup.isDeleteTravelGroup());
    }

}