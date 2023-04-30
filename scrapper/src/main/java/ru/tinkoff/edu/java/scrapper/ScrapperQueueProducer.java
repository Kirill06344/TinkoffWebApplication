package ru.tinkoff.edu.java.scrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueProducer {

    private final RabbitTemplate rabbitTemplate;

    private final ApplicationConfig config;

    public void send(LinkUpdateRequest request) {
        log.info("Send message about link update using RabbitMQ!");
        rabbitTemplate.convertAndSend(config.rabbitData().exchange(), config.rabbitData().root(), request);
    }

}
