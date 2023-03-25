package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import ru.tinkoff.edu.java.scrapper.clients.GitHubClient;
import ru.tinkoff.edu.java.scrapper.clients.StackOverflowClient;

@Configuration
public class HttpInterfaceClientConfiguration {

    StackOverflowProperties stackProperties;

    GitHubProperties githubProperties;
    public HttpInterfaceClientConfiguration(StackOverflowProperties stackProperties, GitHubProperties gitHubProperties) {
        this.stackProperties= stackProperties;
        this.githubProperties = gitHubProperties;
    }
    @Bean("gitHub")
    public WebClient gitHubWebClient() {
        return WebClient.builder()
            .baseUrl(githubProperties.getUrl())
            .build();
    }

    @Bean("stackOverflow")
    public WebClient stackOverflowWebClient() {
        return WebClient.builder()
            .baseUrl(stackProperties.getUrl())
            .build();
    }

    @Bean
    public GitHubClient gitHubClient(@Qualifier("gitHub") WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(webClient))
            .build();

        return httpServiceProxyFactory.createClient(GitHubClient.class);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(@Qualifier("stackOverflow") WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(webClient))
            .build();

        return httpServiceProxyFactory.createClient(StackOverflowClient.class);
    }

}
