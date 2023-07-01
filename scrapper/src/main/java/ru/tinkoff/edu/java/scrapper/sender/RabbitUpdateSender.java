package ru.tinkoff.edu.java.scrapper.sender;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateRequest;

@RequiredArgsConstructor
public class RabbitUpdateSender implements UpdateSender {

    private final ScrapperQueueProducer producer;

    @Override
    public void send(LinkUpdateRequest request) {
        producer.send(request);
    }
}
