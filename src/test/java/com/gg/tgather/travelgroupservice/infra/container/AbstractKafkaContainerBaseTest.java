package com.gg.tgather.travelgroupservice.infra.container;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@DirtiesContext
public class AbstractKafkaContainerBaseTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {


    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3")).withEmbeddedZookeeper();
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres");


    static {
        Startables.deepStart(postgres, kafka).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of("spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers()).applyTo(applicationContext.getEnvironment());
    }
}
