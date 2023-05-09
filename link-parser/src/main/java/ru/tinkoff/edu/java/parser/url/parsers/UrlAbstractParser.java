package ru.tinkoff.edu.java.parser.url.parsers;

import java.net.URL;
import java.util.Optional;

import ru.tinkoff.edu.java.parser.url.results.UrlResult;

public abstract class UrlAbstractParser implements UrlParser {

    private UrlParser next;

    @Override
    public Optional<UrlResult> check(URL url) {
        if (url.getPort() != -1 || (!url.getProtocol().equals("http") && !url.getProtocol().equals("https"))) {
            return Optional.empty();
        }

        return parse(url);
    }

    @Override
    public final boolean isInvalidHostName(URL url, String host) {
        return !url.getHost().equalsIgnoreCase(host);
    }

    @Override
    public final Optional<UrlResult> nextOrEmpty(URL url) {
        if (next == null) {
            return Optional.empty();
        }
        return next.parse(url);
    }

    @Override
    public UrlParser setNext(UrlParser parser) {
        next = parser;
        return parser;
    }
}
