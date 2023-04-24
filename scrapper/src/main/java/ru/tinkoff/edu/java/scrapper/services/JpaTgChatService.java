package ru.tinkoff.edu.java.scrapper.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;

@Service
@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {

    private final JpaChatRepository chatRepository;

    @Override
    public void register(long tgChatId) {
        Chat chat = new Chat().setId(tgChatId);
        chatRepository.save(chat);
    }

    @Override
    public int unregister(long tgChatId) {
        return chatRepository.deleteById(tgChatId);
    }
}
