package com.gg.tgather.travelgroupservice.modules.group.kafka;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import com.gg.tgather.commonservice.dto.account.AccountDto;
import com.gg.tgather.travelgroupservice.infra.annotation.EnableTestcontainers;
import com.gg.tgather.travelgroupservice.infra.annotation.ServiceTest;
import com.gg.tgather.travelgroupservice.infra.security.WithMockJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.client.AccountServiceClient;
import com.gg.tgather.travelgroupservice.modules.common.AbstractJwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupMemberDto;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupMemberRepository;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import com.gg.tgather.travelgroupservice.modules.group.service.TravelGroupMemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;

@Slf4j
@ServiceTest
@WithMockJwtAuthentication
@EnableTestcontainers
@ExtendWith(MockitoExtension.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class TravelGroupKafkaTest implements AbstractJwtAuthentication {


    @Autowired
    private TravelGroupMemberRepository travelGroupMemberRepository;

    @Autowired
    private TravelGroupRepository travelGroupRepository;

    @MockBean
    private AccountServiceClient accountServiceClient;

    @Autowired
    private TravelGroupMemberService travelGroupMemberService;

    @Test
    @DisplayName("여행그룹 가입시 비공개 그룹일 경우, 방장 승인 메일 전송 성공")
    void whenRequestTravelGroupJoin_thenSendToMailLeader() {
        // given
        TravelGroup travelGroup = createPrivateTravelGroup();
        when(accountServiceClient.getAccount(getCommonAuthentication().accountId())).thenReturn(defaultAccount());
        // when
        TravelGroupMemberDto travelGroupMemberDto = travelGroupMemberService.requestTravelGroupJoin(travelGroup.getTravelGroupId(), getCommonAuthentication());
        // then
        assertFalse(travelGroupMemberDto.isApproved());
    }

    private AccountDto defaultAccount() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(getCommonAuthentication().accountId());
        return accountDto;
    }

    private TravelGroup createPrivateTravelGroup() {
        TravelGroupSaveForm travelGroupSaveForm = TravelGroupSaveForm.createTravelGroupSaveFormForTest("TravelTogether");
        travelGroupSaveForm.setOpen(false);
        TravelGroup travelGroup = TravelGroup.from(travelGroupSaveForm);
        travelGroupRepository.save(travelGroup);
        TravelGroupMember travelGroupMember = TravelGroupMember.createTravelGroupLeader(travelGroup, "TEST-ACCOUNT-ID");
        travelGroupMemberRepository.save(travelGroupMember);
        return travelGroup;
    }

}
