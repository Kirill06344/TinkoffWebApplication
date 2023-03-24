package ru.tinkoff.edu.java.scrapper.web.dto;

import java.util.List;

public record ListLinksResponse(
    List<LinkResponse> links,
    Integer size
) {
}
