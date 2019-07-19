package com.luxoft.training.spring.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableBinding(PublisherChannel.class)
@RestController
public class Publisher {
    @Autowired
    @Qualifier(PublisherChannel.WORDS)
    public MessageChannel words;

    @GetMapping("/publish")
    private void publish(@RequestParam String word) {
        System.out.printf("publish!");
        words.send(MessageBuilder.withPayload(word).build());
    }

    public static void main(String[] args) {
        SpringApplication.run(Publisher.class, args);
    }
}