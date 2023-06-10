package com.gg.tgather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ConfigurationPropertiesScan
public class TravelGroupServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelGroupServiceApplication.class, args);
    }

}
