package ru.tinkoff.edu.java.parser.url.results;

import ru.tinkoff.edu.java.parser.url.results.UrlResult;

public record GitHubResult(String name, String repository) implements UrlResult {
}
