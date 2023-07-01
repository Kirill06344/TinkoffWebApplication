package ru.tinkoff.edu.java.bot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

@Component
@RabbitListener(queues = "${app.rabbitData.queue}")
@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueListener {

    private final UpdateSender updateSender;

    @RabbitHandler
    public void receiveUpdate(LinkUpdateRequest request) {
        log.info("Update from queue received!Send it to tg ids");
        updateSender.send(request);
    }

}
