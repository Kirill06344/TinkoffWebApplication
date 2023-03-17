package ru.tinkoff.edu.java.parser.url.parsers;

import java.net.URL;
import java.util.Optional;
import java.util.Set;

import ru.tinkoff.edu.java.parser.url.results.UrlResult;

public class UrlProtocolParser extends UrlAbstractParser {

    private final Set<String> protocols = Set.of("http", "https");

    @Override
    public Optional<UrlResult> parse(URL url) {
        return protocols.contains(url.getProtocol()) ? nextOrEmpty(url) : Optional.empty();
    }
}
