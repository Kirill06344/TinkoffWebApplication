package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.experimental.SuperBuilder;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.utils.MessageSender;
import ru.tinkoff.edu.java.bot.utils.TrackingCommandValidator;

import java.util.Map;
import java.util.Optional;

@SuperBuilder
public class TrackCommand extends AbstractCommand {

    private MessageSender sender;

    private ScrapperClient client;

    private TrackingCommandValidator validator;

    @Override
    public SendMessage handle(Update update) {
        Optional<String> link = validator.getLink(update);
        if (link.isEmpty()) {
            return sender.send(getChatId(update), "responses/invalid.mustache", Map.of());
        }

        var response = client.trackLink(getChatId(update), link.get());

        if (response.isPresent()) {
            return sender.send(
                getChatId(update),
                "responses/success_tracking.mustache",
                Map.of("link", response.get().link())
            );
        }

        return sender.send(getChatId(update), "responses/defects.mustache", Map.of());
    }

    @Override
    public boolean supports(Update update) {
        return validator.supports(update, command);
    }

}
