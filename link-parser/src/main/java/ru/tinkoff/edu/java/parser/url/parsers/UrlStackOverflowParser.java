package ru.tinkoff.edu.java.parser.url.parsers;

import java.net.URL;
import java.util.Optional;

import ru.tinkoff.edu.java.parser.url.results.StackOverflowResult;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;

public class UrlStackOverflowParser extends UrlAbstractParser {

    private final String HOST_NAME = "stackoverflow.com";

    @Override
    public Optional<UrlResult> parse(URL url) {
        if (isInvalidHostName(url, HOST_NAME)) {
            return nextOrEmpty(url);
        }

        String[] data = url.getPath().split("/");
        if (data.length >= 3 && data[1].equals("questions")) {
            return Optional.of(new StackOverflowResult(data[2]));
        }

        return nextOrEmpty(url);
    }
}
