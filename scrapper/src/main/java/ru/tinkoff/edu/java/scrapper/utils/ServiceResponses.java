package ru.tinkoff.edu.java.scrapper.utils;

import java.util.Map;

public class ServiceResponses {

    private static final Map<String, String> githubResponses = Map.of(
        "count", "Oooooh..someone open an issue or PR at this repository...",
        "other", "Hey! There is a new commit at this repository..."
    );

    private static final Map<String, String> stackOverFlowResponses = Map.of(
        "count", "Oooooh..someone left answer on this question...",
        "other", "Hey! There is a new modification in this question..."
    );

    public static String getGithubResponse(String key) {
        return githubResponses.get(key);
    }

    public static String getStackOverflowResponses(String key) {
        return stackOverFlowResponses.get(key);
    }

}
