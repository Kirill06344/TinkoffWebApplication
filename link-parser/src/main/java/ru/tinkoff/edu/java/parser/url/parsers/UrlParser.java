package ru.tinkoff.edu.java.parser.url.parsers;

import java.net.URL;
import java.util.Optional;

import ru.tinkoff.edu.java.parser.url.results.UrlResult;

public interface UrlParser {
    Optional<UrlResult> parse(URL url);

    Optional<UrlResult> check(URL url);

    boolean isInvalidHostName(URL url, String host);

    Optional<UrlResult> nextOrEmpty(URL url);

    UrlParser setNext(UrlParser parser);
}
