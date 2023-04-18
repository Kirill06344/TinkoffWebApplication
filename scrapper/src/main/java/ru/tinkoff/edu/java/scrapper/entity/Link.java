package ru.tinkoff.edu.java.scrapper.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode
public class Link {
    private Long id;
    private String url;
    private LocalDateTime checkedAt;
    private LocalDateTime updatedAt;
}
