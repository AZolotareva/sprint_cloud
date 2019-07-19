package com.luxoft.training.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
@EnableBinding(ProcessorChannel.class)
public class Processor {
    @StreamListener(ProcessorChannel.WORDS)
    @SendTo(ProcessorChannel.UPPER_WORDS)
    private String process(String word) {
        System.out.println(String.format("!!!!!!! %s", word));
        return word.toUpperCase();
    }

    public static void main(String[] args) {
        SpringApplication.run(Processor.class, args);
    }
}