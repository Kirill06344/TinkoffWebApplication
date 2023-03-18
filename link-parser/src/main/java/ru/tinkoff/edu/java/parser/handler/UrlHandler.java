package ru.tinkoff.edu.java.parser.handler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import ru.tinkoff.edu.java.parser.url.parsers.UrlGitHubParser;
import ru.tinkoff.edu.java.parser.url.parsers.UrlParser;
import ru.tinkoff.edu.java.parser.url.parsers.UrlStackOverflowParser;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;

public class UrlHandler {

    private final UrlParser parser;
    public UrlHandler() {
        parser = new UrlGitHubParser()
                .setNext(new UrlStackOverflowParser());
    }

    public Optional<UrlResult> getParseResult(String path) {
        try {
            URL url = new URL(path);
            return parser.check(url);
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }
}
