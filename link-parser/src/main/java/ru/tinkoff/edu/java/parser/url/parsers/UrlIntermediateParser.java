package ru.tinkoff.edu.java.parser.url.parsers;

import java.net.URL;
import java.util.Optional;

import ru.tinkoff.edu.java.parser.url.results.UrlResult;


public abstract class UrlIntermediateParser {

    private UrlIntermediateParser next;

    public static UrlIntermediateParser link(UrlIntermediateParser first, UrlIntermediateParser... other) {
        UrlIntermediateParser head = first;
        for (var next : other) {
            head.next = next;
            head = next;
        }
        return first;
    }

    public abstract Optional<UrlResult> parse(URL url);

    protected Optional<UrlResult> nextOrEmpty(URL url) {
        if (next == null) {
            return Optional.empty();
        }
        return next.parse(url);
    }
}
