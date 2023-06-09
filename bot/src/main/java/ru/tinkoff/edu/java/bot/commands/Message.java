package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Message {
    SendMessage handle(Update update);

    default long getChatId(Update update) {
        return update.message().chat().id();
    }

}
