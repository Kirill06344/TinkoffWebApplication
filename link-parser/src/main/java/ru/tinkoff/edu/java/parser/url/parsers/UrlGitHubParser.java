package ru.tinkoff.edu.java.parser.url.parsers;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

import ru.tinkoff.edu.java.parser.url.results.GitHubResult;
import ru.tinkoff.edu.java.parser.url.UrlHostCheck;
import ru.tinkoff.edu.java.parser.url.results.UrlResult;

public class UrlGitHubParser extends UrlIntermediateParser implements UrlHostCheck {

    private final String HOST_NAME = "github.com";

    @Override
    public Optional<UrlResult> parse(URL url) {
        if (!check(url, HOST_NAME)) {
            return nextOrEmpty(url);
        }

        String[] data = url.getPath().split("/");
        if (data.length != 3) {
            return nextOrEmpty(url);
        }

        return Optional.of(new GitHubResult(data[1], data[2]));
    }
}
