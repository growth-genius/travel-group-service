package com.gg.tgather.travelgroupservice.modules.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.commonservice.security.JwtAuthenticationToken;
import com.gg.tgather.travelgroupservice.infra.annotation.ServiceTest;
import com.gg.tgather.travelgroupservice.infra.container.AbstractContainerBaseTest;
import com.gg.tgather.travelgroupservice.infra.security.WithMockJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupModifyForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import com.gg.tgather.travelgroupservice.modules.group.service.TravelGroupService;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ServiceTest
@WithMockJwtAuthentication
class TravelGroupServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private TravelGroupService travelGroupService;

    @Autowired
    private TravelGroupRepository travelGroupRepository;

    private JwtAuthentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return (JwtAuthentication) authenticationToken.getPrincipal();
    }

    private void createTravelGroupWithTest(String travelGroupName) {
        TravelGroupSaveForm travelGroupSaveForm = new TravelGroupSaveForm();
        travelGroupSaveForm.setGroupName(travelGroupName);
        travelGroupSaveForm.setTravelThemes(Set.of(TravelTheme.ACTIVITY));
        travelGroupSaveForm.setStartDate("2023-10-01T09:45:00.000+02:00");
        travelGroupService.createTravelGroup(travelGroupSaveForm, getAuthentication());
    }

    @Test
    @DisplayName("travel group 생성 확인")
    void createTravelGroup() {
        TravelGroupSaveForm travelGroupSaveForm = new TravelGroupSaveForm();
        String travelGroupName = "전국 여행일지";
        travelGroupSaveForm.setGroupName(travelGroupName);
        travelGroupSaveForm.setTravelThemes(Set.of(TravelTheme.ACTIVITY));
        travelGroupSaveForm.setStartDate("2023-10-01T09:45:00.000+02:00");

        TravelGroupDto travelGroupDto = travelGroupService.createTravelGroup(travelGroupSaveForm, getAuthentication());
        assertEquals(travelGroupName, travelGroupDto.getGroupName());
    }

    @Test
    @DisplayName("travel group 수정 확인")
    void modifyTravelGroup() {
        String groupName = "전국 여행일지";
        createTravelGroupWithTest(groupName);
        TravelGroupModifyForm travelGroupModifyForm = new TravelGroupModifyForm();
        travelGroupModifyForm.setGroupName(groupName);
        travelGroupModifyForm.setTravelThemes(Set.of(TravelTheme.FOOD));
        travelGroupModifyForm.setStartDate("2023-10-01T09:45:00.000+02:00");
        TravelGroupDto travelGroupDto = travelGroupService.modifyTravelGroup(travelGroupModifyForm, getAuthentication());
        assertTrue(travelGroupDto.getTravelThemes().contains(TravelTheme.FOOD));
    }

    @Test
    @DisplayName("travel group 삭제 확인")
    void deleteTravelGroup() {
        String groupName = "전국 여행일지";
        createTravelGroupWithTest(groupName);
        Boolean deleted = travelGroupService.deleteTravelGroup(groupName, getAuthentication());
        TravelGroup travelGroup = travelGroupRepository.findByGroupName(groupName).orElseThrow();
        assertTrue(travelGroup.isDeleteTravelGroup());
        assertTrue(deleted);
    }

}