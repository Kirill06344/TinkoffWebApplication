package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import ru.tinkoff.edu.java.bot.utils.MessageSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@SuperBuilder
public class HelpCommand extends AbstractCommand {

    private MessageSender sender;

    private List<? extends Command> commands;

    @Override
    public SendMessage handle(Update update) {
        Map<String, Object> scopes = new HashMap<>();
        scopes.put("commands", commands);
        return sender.send(getChatId(update), "responses/help.mustache", scopes);
    }

    @Autowired
    public void setCommands(List<? extends Command> commands) {
        this.commands = commands;
    }
}
