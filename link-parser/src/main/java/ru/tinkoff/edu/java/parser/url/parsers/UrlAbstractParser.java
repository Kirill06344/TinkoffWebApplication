package ru.tinkoff.edu.java.parser.url.parsers;

import java.net.URL;
import java.util.Optional;

import ru.tinkoff.edu.java.parser.url.results.UrlResult;


public abstract class UrlAbstractParser {

    private UrlAbstractParser next;

    public static UrlAbstractParser link(UrlAbstractParser first, UrlAbstractParser... other) {
        UrlAbstractParser head = first;
        for (var next : other) {
            head.next = next;
            head = next;
        }
        return first;
    }

    public Optional<UrlResult> check(URL url) {
        if (url.getPort() != -1 || (!url.getProtocol().equals("http") && !url.getProtocol().equals("https"))) {
            return Optional.empty();
        }
        return nextOrEmpty(url);
    }

    protected abstract Optional<UrlResult> parse(URL url);

    protected final boolean isInvalidHostName(URL url, String host) {
        return !url.getHost().equalsIgnoreCase(host);
    }

    protected final Optional<UrlResult> nextOrEmpty(URL url) {
        if (next == null) {
            return Optional.empty();
        }
        return next.parse(url);
    }
}
