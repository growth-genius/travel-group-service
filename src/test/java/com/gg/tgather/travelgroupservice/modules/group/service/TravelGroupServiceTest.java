package com.gg.tgather.travelgroupservice.modules.group.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.travelgroupservice.infra.annotation.ServiceTest;
import com.gg.tgather.travelgroupservice.infra.container.AbstractContainerBaseTest;
import com.gg.tgather.travelgroupservice.infra.security.WithMockJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.common.AbstractJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupModifyForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@WithMockJwtAuthentication
class TravelGroupServiceTest extends AbstractContainerBaseTest implements AbstractJwtAuthentication {

    @Autowired
    private TravelGroupService travelGroupService;

    @Autowired
    private TravelGroupRepository travelGroupRepository;

    private TravelGroupDto createTravelGroupWithTest(String travelGroupName) {
        TravelGroupSaveForm travelGroupSaveForm = TravelGroupSaveForm.createTravelGroupSaveFormForTest(travelGroupName);
        return travelGroupService.createTravelGroup(travelGroupSaveForm, getCommonAuthentication());
    }

    @Test
    @DisplayName("travel group 생성 확인")
    void createTravelGroup() {
        String travelGroupName = "전국 여행일지";
        TravelGroupSaveForm travelGroupSaveForm = TravelGroupSaveForm.createTravelGroupSaveFormForTest(travelGroupName);
        TravelGroupDto travelGroupDto = travelGroupService.createTravelGroup(travelGroupSaveForm, getCommonAuthentication());
        assertEquals(travelGroupName, travelGroupDto.getGroupName());
    }

    @Test
    @DisplayName("travel group 수정 확인")
    void modifyTravelGroup() {
        String groupName = "전국 맛집탐방";
        TravelGroupDto createTravelGroup = createTravelGroupWithTest(groupName);
        TravelGroupModifyForm travelGroupModifyForm = new TravelGroupModifyForm();
        travelGroupModifyForm.setGroupName(groupName);
        travelGroupModifyForm.setTravelThemes(Set.of(TravelTheme.FOOD));
        travelGroupModifyForm.setStartDate("2023-10-01T09:45:00.000+02:00");
        TravelGroupDto travelGroupDto = travelGroupService.modifyTravelGroup(createTravelGroup.getTravelGroupId(), travelGroupModifyForm,
            getCommonAuthentication());
        assertTrue(travelGroupDto.getTravelThemes().contains(TravelTheme.FOOD));
    }

    @Test
    @DisplayName("travel group 삭제 확인")
    void deleteTravelGroup() {
        TravelGroupDto travelGroupDto = createTravelGroupWithTest("TravelGroup");
        Boolean deleted = travelGroupService.deleteTravelGroup(travelGroupDto.getTravelGroupId(), getCommonAuthentication());
        TravelGroup travelGroup = travelGroupRepository.findByGroupName("TravelGroup").orElseThrow();
        assertTrue(travelGroup.isDeleteTravelGroup());
        assertTrue(deleted);
    }


    @Test
    @DisplayName("travel group 조회 확인")
    void searchTravelGroup() {
        // given
        TravelGroupDto travelGroupDto = createTravelGroupWithTest("TravelGroup");
        // when
        List<TravelGroupDto> travelGroupByTheme = travelGroupService.findTravelGroupByTheme(travelGroupDto.getTravelThemes());
        // then
        assertEquals(1, travelGroupByTheme.size());
        assertEquals("TravelGroup", travelGroupByTheme.get(0).getGroupName());
        assertEquals(Set.of(TravelTheme.ACTIVITY), travelGroupByTheme.get(0).getTravelThemes());
    }

}