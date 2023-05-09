package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.tinkoff.edu.java.scrapper.clients.GitHubClientImpl;
import ru.tinkoff.edu.java.scrapper.clients.StackOverflowClientImpl;

@Configuration
public class HttpInterfaceClientConfiguration {

    private final GitHubProperties gitHubProperties;

    private final StackOverflowProperties stackOverflowProperties;

    public HttpInterfaceClientConfiguration(
        GitHubProperties gitHubProperties,
        StackOverflowProperties stackOverflowProperties
    ) {
        this.gitHubProperties = gitHubProperties;
        this.stackOverflowProperties = stackOverflowProperties;
    }

    @Bean
    GitHubClientImpl gitHubClient() {
        return new GitHubClientImpl(gitHubProperties.getUrl());
    }

    @Bean
    StackOverflowClientImpl stackOverflowClient() {
        return new StackOverflowClientImpl(stackOverflowProperties.getUrl());
    }

}
