package ru.tinkoff.edu.java.scrapper.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LinkUpdaterScheduler {

    @Scheduled(fixedRateString = "${app.scheduler.interval}")
    public void update() {
      log.info("Some update...");
    }

}
