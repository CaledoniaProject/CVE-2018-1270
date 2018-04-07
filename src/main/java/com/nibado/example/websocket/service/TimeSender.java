package com.nibado.example.websocket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class TimeSender {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    @Autowired
    private SimpMessagingTemplate broker;

    @Autowired
    public TimeSender(final SimpMessagingTemplate broker) {
        this.broker = broker;
    }

    @Scheduled(fixedRate = 5000)
    public void run() {
        String time = LocalTime.now().format(TIME_FORMAT);

        log.info("Time broadcast: {}", time);
        broker.convertAndSend("/topic/greetings", new Greeting("Current time is " + time));
    }
}
