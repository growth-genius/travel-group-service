package com.gg.tgather.travelgroupservice.infra.properties;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("swagger")
public class CustomProperties {

    private List<String> servers;

}
