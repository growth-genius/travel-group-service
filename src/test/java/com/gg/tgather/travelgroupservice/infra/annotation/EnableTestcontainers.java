package com.gg.tgather.travelgroupservice.infra.annotation;

import com.gg.tgather.travelgroupservice.infra.container.AbstractKafkaContainerBaseTest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.test.context.ContextConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(initializers = AbstractKafkaContainerBaseTest.class)
public @interface EnableTestcontainers {}
