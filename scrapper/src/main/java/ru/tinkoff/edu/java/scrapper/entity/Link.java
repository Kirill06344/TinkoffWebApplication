package ru.tinkoff.edu.java.scrapper.entity;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode
public class Link {
    private Long id;
    private String url;
}
