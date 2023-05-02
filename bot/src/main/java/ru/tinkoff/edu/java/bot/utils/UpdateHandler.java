package ru.tinkoff.edu.java.bot.utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.messages.UserMessageProcessorImpl;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class UpdateHandler implements UpdatesListener{

    private final TelegramBot bot;

    private final UserMessageProcessorImpl messageProcessor;

    @Override
    public int process(List<Update> list) {
       for (Update update : list) {
          bot.execute(messageProcessor.process(update));
       }
       return CONFIRMED_UPDATES_ALL;
    }

}
