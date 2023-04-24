package ru.tinkoff.edu.java.scrapper.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
    private Long intersectingCountField;
}
