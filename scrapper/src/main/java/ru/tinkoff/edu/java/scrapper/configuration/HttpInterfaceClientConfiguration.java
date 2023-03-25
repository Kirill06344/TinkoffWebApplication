package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import ru.tinkoff.edu.java.scrapper.clients.GitHubClient;

@Configuration
public class HttpInterfaceClientConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl("https://api.github.com/")
            .build();
    }

    @Bean
    public GitHubClient gitHubClient(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(webClient))
            .build();

        return httpServiceProxyFactory.createClient(GitHubClient.class);
    }

}
