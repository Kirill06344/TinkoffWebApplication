package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Builder;
import ru.tinkoff.edu.java.bot.clients.ScrapperClient;
import ru.tinkoff.edu.java.bot.utils.MessageSender;

import java.util.HashMap;
import java.util.Map;

@Builder
public class ListCommand implements Command{

    private String command;
    private String description;

    private MessageSender sender;

    private ScrapperClient client;


    @Override
    public SendMessage handle(Update update) {
        var response = client.getAllLinks(getChatId(update));
        Map<String, Object> scopes = new HashMap<>();
        if (response.isPresent()) {
            String path = "empty.mustache";
            if (response.get().size() != 0) {
                scopes.put("links", response.get().links());
                scopes.put("size", response.get().size());
                path = "links.mustache";
            }

            return sender.send(getChatId(update), path, scopes);
        }

        return sender.send(getChatId(update),"defects.mustache", scopes);
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
