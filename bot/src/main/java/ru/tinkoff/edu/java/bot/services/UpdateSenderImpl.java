package ru.tinkoff.edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.utils.MessageSender;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateSenderImpl implements UpdateSender {

    private final MessageSender sender;

    private final TelegramBot bot;

    @Override
    public void send(LinkUpdateRequest request) {
        for (long id : request.tgChatIds()) {
            log.info(String.valueOf(id));
            bot.execute(sender.send(id, "responses/update_link.mustache",
                    Map.of("msg", request.description(),
                            "link", request.url())));
        }
    }
}
