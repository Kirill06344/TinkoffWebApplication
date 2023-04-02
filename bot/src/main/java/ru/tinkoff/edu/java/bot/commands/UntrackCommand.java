package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Builder;
import ru.tinkoff.edu.java.bot.utils.MessageSupplier;

@Builder
public class UntrackCommand implements Command {
    private String command;
    private String description;
    private MessageSupplier supplier;

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(getChatId(update), "torere");
    }

    @Override
    public String command() {
        return command;
    }

    @Override
    public String description() {
        return description;
    }
}
