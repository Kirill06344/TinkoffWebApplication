package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tinkoff.edu.java.scrapper.entity.Chat;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    @Modifying
    @Query("delete from Chat c where c.id = :id")
    int deleteById(@Param("id")long id);
}
