package com.luxoft.training.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;

import java.math.BigDecimal;

@SpringBootApplication
@EnableBinding(Processor.class)
public class WordNumSplitter {
    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    private String split(String word) {
        try {
            BigDecimal num = new BigDecimal(word);
            System.out.println(word + " - is a number");
            return null;
        } catch (Exception ex) {
            System.out.println(word + " - sent");
            return word;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(WordNumSplitter.class, args);
    }
}