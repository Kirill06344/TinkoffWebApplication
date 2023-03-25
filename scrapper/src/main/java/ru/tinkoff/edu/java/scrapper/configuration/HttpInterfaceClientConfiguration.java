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

    @Bean("gitHub")
    public WebClient gitHubWebClient() {
        return WebClient.builder()
            .baseUrl("https://api.github.com/")
            .build();
    }

    @Bean("stackOverflow")
    public WebClient stackOverflowWebClient() {
        return WebClient.builder()
            .baseUrl("https://api.stackexchange.com/2.3")
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
