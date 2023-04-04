package ru.tinkoff.edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import ru.tinkoff.edu.java.bot.utils.MessageSender;
import ru.tinkoff.edu.java.bot.utils.MessageSenderHTML;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Builder
public class HelpCommand implements Command {

    private String command;
    private String description;

    private MessageSender sender;

    private List<? extends Command> commands;

    @Override
    public SendMessage handle(Update update) {
        System.out.println(commands);
        Map<String, Object> scopes = new HashMap<>();
        scopes.put("commands", commands);
        return sender.send(getChatId(update),"help.mustache", scopes);
    }

    @Override
    public String command() {
        return command;
    }

    @Override
    public String description() {
        return description;
    }

    @Autowired
    public void setCommands(List<? extends Command> commands) {
        this.commands = commands;
    }
}
