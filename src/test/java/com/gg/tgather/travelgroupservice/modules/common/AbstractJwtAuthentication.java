package com.gg.tgather.travelgroupservice.modules.common;

import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.commonservice.security.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface AbstractJwtAuthentication {

    private JwtAuthentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return (JwtAuthentication) authenticationToken.getPrincipal();
    }

    default JwtAuthentication getCommonAuthentication() {
        return getAuthentication();
    }
}
