package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.sender.ScrapperQueueProducer;
import ru.tinkoff.edu.java.scrapper.sender.RabbitUpdateSender;
import ru.tinkoff.edu.java.scrapper.sender.UpdateSender;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableRabbit
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "true")
public class RabbitMQConfiguration {

    private final ApplicationConfig config;
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost");
        cachingConnectionFactory.setUsername(config.rabbitData().username());
        cachingConnectionFactory.setPassword(config.rabbitData().password());
        return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(config.rabbitData().queue())
                .deadLetterExchange(config.rabbitData().queue() + ".dlx")
                .build();
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(config.rabbitData().exchange(), true, false);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(config.rabbitData().root());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }


    @Bean
    UpdateSender sender(ScrapperQueueProducer producer) {
        return new RabbitUpdateSender(producer);
    }

}
