package ru.tinkoff.edu.java.scrapper.repository;

import ru.tinkoff.edu.java.scrapper.entity.Chat;

import java.util.Optional;

public interface ChatRepository extends Repository<Chat, Long>{

    Optional<Chat> findChatById(long id);

}
