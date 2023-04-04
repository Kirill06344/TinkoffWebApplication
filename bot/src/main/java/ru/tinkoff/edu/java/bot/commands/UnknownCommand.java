package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.utils.MessageSupplier;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class UnknownCommand implements Message {

    private final MessageSupplier supplier;
    @Override
    public SendMessage handle(Update update) {
        log.info("unknown");
        Map<String, Object> scopes = new HashMap<>();
        scopes.put("unknown", update.message().text());
        return new SendMessage(getChatId(update), supplier.convertTemplate("unknown.mustache", scopes))
                .parseMode(ParseMode.HTML);
    }

}