package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Builder;
import ru.tinkoff.edu.java.bot.dto.ListLinksResponse;
import ru.tinkoff.edu.java.bot.service.ScrapperService;
import ru.tinkoff.edu.java.bot.utils.MessageSupplier;

import java.util.HashMap;
import java.util.Map;

@Builder
public class ListCommand implements Command{

    private String command;
    private String description;
    private MessageSupplier supplier;
    private ScrapperService service;


    @Override
    public SendMessage handle(Update update) {
        var response = service.getAllLinks(getChatId(update));
        Map<String, Object> scopes = new HashMap<>();
        if (response.isPresent()) {
            String path = "empty.mustache";
            if (response.get().size() != 0) {
                scopes.put("links", response.get().links());
                scopes.put("size", response.get().size());
                path = "links.mustache";
            }
            return new SendMessage(getChatId(update), supplier.convertTemplate(path, scopes))
                    .disableWebPagePreview(true)
                    .parseMode(ParseMode.HTML);
        }

       return new SendMessage(getChatId(update), supplier.convertTemplate("defects.mustache", scopes))
               .parseMode(ParseMode.HTML);
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
