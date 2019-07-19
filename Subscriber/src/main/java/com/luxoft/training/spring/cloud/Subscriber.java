package com.luxoft.training.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@EnableBinding(SubscriberChannel.class)
@RestController
public class Subscriber {
    private Map<String, AtomicInteger> wordMap = new ConcurrentHashMap<>();

    @GetMapping("/words")
    public Map<String, AtomicInteger> getWords() {
        return wordMap;
    }

    @StreamListener(SubscriberChannel.UPPER_WORDS)
    public void countWords(String s) {
        if (wordMap.containsKey(s)) {
            wordMap.get(s).incrementAndGet();
        } else {
            wordMap.put(s, new AtomicInteger(1));
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Subscriber.class, args);
    }
}