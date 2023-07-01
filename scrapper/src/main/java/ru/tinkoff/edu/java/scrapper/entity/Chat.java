package ru.tinkoff.edu.java.scrapper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Chat {
    @Id
    @Column(name = "id")
    private long id;

    @ManyToMany
    @JoinTable(
        name = "chat_link",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    private Set<Link> links = new HashSet<>();
}
