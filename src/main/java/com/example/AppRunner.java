package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AppRunner.class);

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    @Value("topic.exchange.name")
    private String topicExchangeName;

    public AppRunner(RabbitTemplate rabbitTemplate, Receiver receiver) {
        this.rabbitTemplate = rabbitTemplate;
        this.receiver = receiver;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Sending message ....");

        rabbitTemplate.convertAndSend(topicExchangeName, "foo.bar.baz", "Hello from RabbitMq!");
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }
}
