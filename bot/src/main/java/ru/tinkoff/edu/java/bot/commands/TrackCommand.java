package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Builder;
import ru.tinkoff.edu.java.bot.service.ScrapperService;
import ru.tinkoff.edu.java.bot.utils.MessageSupplier;
import ru.tinkoff.edu.java.bot.utils.TrackingCommandValidator;

import java.util.Map;
import java.util.Optional;

@Builder
public class TrackCommand implements Command {

    private String command;

    private String description;

    private MessageSupplier supplier;

    private ScrapperService service;

    private TrackingCommandValidator validator;


    @Override
    public SendMessage handle(Update update) {
        Optional<String> link = validator.getLink(update);
        if (link.isEmpty()) {
            return new SendMessage(getChatId(update),
                    supplier.convertTemplate("invalid.mustache", Map.of("command", command)))
                    .parseMode(ParseMode.HTML);
        }

        var response = service.trackLink(getChatId(update), link.get());
        if (response.isPresent()) {
            return new SendMessage(getChatId(update),
                    supplier.convertTemplate("success_tracking.mustache", Map.of("link", response.get().link()))
            ).parseMode(ParseMode.HTML);
        }

        return new SendMessage(getChatId(update), supplier.convertTemplate("defects.mustache", Map.of()))
                .parseMode(ParseMode.HTML);
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
