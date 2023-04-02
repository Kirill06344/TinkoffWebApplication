package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;

public interface Command extends Message {

    String command();

    String description();

    default boolean supports(Update update) {
        return update != null &&
                update.message() != null
                && update.message().text().equals(command());
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
