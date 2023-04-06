package ru.tinkoff.edu.java.bot.commands;

import java.util.Map;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.utils.MessageSender;

@SuperBuilder
@Slf4j
public class StartCommand extends AbstractCommand{

    private ScrapperClient client;

    private MessageSender sender;

    @Override
    public SendMessage handle(Update update) {
        var response = client.registerChat(getChatId(update));

        if (response.isEmpty()) {
            log.info("Fail to register id {}", getChatId(update));
            return sender.send(getChatId(update), "responses/fail_register.mustache", Map.of());
        }

        log.info("User with {} succesfully registered", response.get());
        return sender.send(getChatId(update), "responses/success_register.mustache", Map.of());
    }
}
