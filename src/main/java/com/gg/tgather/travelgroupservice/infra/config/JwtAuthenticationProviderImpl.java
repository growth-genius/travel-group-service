package com.gg.tgather.travelgroupservice.infra.config;

import com.gg.tgather.commonservice.dto.account.AccountDto;
import com.gg.tgather.commonservice.security.CredentialInfo;
import com.gg.tgather.commonservice.security.JwtAuthenticationProvider;
import com.gg.tgather.travelgroupservice.modules.client.AccountServiceClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationProviderImpl implements JwtAuthenticationProvider {

    private final AccountServiceClient accountServiceClient;

    @Override
    public AccountDto getAccountDto(String principal, CredentialInfo credential) {
        return accountServiceClient.getAccount(principal);
    }

}
