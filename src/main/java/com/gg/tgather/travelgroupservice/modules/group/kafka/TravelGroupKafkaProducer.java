package com.gg.tgather.travelgroupservice.modules.group.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.commonservice.dto.mail.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class TravelGroupKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    /**
     * 카프카 토픽 전송
     *
     * @param kafkaTopic   카프카 토픽 명
     * @param emailMessage fcmMessage 객체
     * @throws JsonProcessingException 객체 -> string 변환 실패
     */
    public void send(String kafkaTopic, EmailMessage emailMessage) throws JsonProcessingException {
        log.info("kafkaFCM.kafkaTopic :: {}", kafkaTopic);
        String emailMessageJsonString = objectMapper.writeValueAsString(emailMessage);
        kafkaTemplate.send(kafkaTopic, emailMessageJsonString);
        log.info("Kafka Producer send data from the TravelGroup MicroService : ");
    }

}
