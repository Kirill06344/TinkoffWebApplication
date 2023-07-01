package ru.tinkoff.edu.java.bot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.commands.Message;

@Component
@RabbitListener(queues = "changeLink.dlq")
@Slf4j
public class ScrapperDlqListener {

    @RabbitHandler
    public void receiveUpdate(Message message) {
        log.info("Received failed message: {}", message.toString());
    }
}
