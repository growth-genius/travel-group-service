package com.gg.tgather.travelgroupservice.infra.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtAuthenticationSecurityContextFactory.class)
public @interface WithMockJwtAuthentication {

    long id() default 1L;

    String email() default "leesg107@naver.com";

    String accountId() default "accountId";

    String nickname() default "관리자";
    
}
