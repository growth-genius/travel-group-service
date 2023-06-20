package com.gg.tgather.travelgroupservice.modules.group.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gg.tgather.travelgroupservice.infra.annotation.ServiceTest;
import com.gg.tgather.travelgroupservice.infra.container.AbstractContainerBaseTest;
import com.gg.tgather.travelgroupservice.infra.security.WithMockJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.common.AbstractJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.service.TravelGroupService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@WithMockJwtAuthentication
class TravelGroupRepositoryImplTest extends AbstractContainerBaseTest implements AbstractJwtAuthentication {

    @Autowired
    private TravelGroupRepository travelGroupRepository;

    @Autowired
    private TravelGroupService travelGroupService;

    private TravelGroupDto createTravelGroup(String travelGroupName) {
        TravelGroupSaveForm travelGroupSaveForm = TravelGroupSaveForm.createTravelGroupSaveFormForTest(travelGroupName);
        return travelGroupService.createTravelGroup(travelGroupSaveForm, getCommonAuthentication());
    }

    @Test
    @DisplayName("travelGroup 조회확인")
    void travelGroupGetTest() {
        // given
        TravelGroupDto travelGroupDto = createTravelGroup("전국여행일지");
        // when
        Optional<TravelGroup> travelGroup = travelGroupRepository.searchByTravelGroupAndLeader(travelGroupDto.getTravelGroupId(),
            getCommonAuthentication().accountId());
        assertTrue(travelGroup.isPresent());
        // then
        assertEquals(travelGroupDto.getTravelGroupId(), travelGroup.get().getId());
    }

    @Test
    @DisplayName("travelGroup 삭제 확인")
    void travelGroupDelete() {
        // given
        TravelGroupDto travelGroupDto = createTravelGroup("전국 여행 일즤");
        // when
        Optional<TravelGroup> travelGroup = travelGroupRepository.searchByTravelGroupAndLeader(travelGroupDto.getTravelGroupId(),
            getCommonAuthentication().accountId());
        assertTrue(travelGroup.isPresent());
        travelGroup.get().deleteTravelGroup();
        // then
        assertTrue(travelGroup.get().isDeleteTravelGroup());
    }

}