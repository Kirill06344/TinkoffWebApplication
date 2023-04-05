package ru.tinkoff.edu.java.bot.commands;

import java.util.Map;
import java.util.Optional;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Builder;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.utils.MessageSender;
import ru.tinkoff.edu.java.bot.utils.TrackingCommandValidator;

@Builder
public class UntrackCommand implements Command {
    private String command;
    private String description;

    private MessageSender sender;

    private ScrapperClient client;

    private TrackingCommandValidator validator;

    @Override
    public SendMessage handle(Update update) {
        Optional<String> link = validator.getLink(update);
        if (link.isEmpty()) {
            return sender.send(getChatId(update), "invalid.mustache", Map.of());
        }

        var response = client.untrackLink(getChatId(update), link.get());

        if (response.isPresent()) {
            return sender.send(getChatId(update),
                    "success_untracking.mustache",
                    Map.of("link", response.get().link()));
        }

        return sender.send(getChatId(update), "defects.mustache", Map.of());
    }

    @Override
    public boolean supports(Update update) {
        return validator.supports(update, command);
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
