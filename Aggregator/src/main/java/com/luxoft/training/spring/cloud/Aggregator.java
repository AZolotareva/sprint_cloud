package com.luxoft.training.spring.cloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.aggregate.AggregateApplicationBuilder;

import javax.annotation.processing.Processor;

@SpringBootApplication
public class Aggregator {
    public static void main(String[] args) {
        new AggregateApplicationBuilder()
                .from(WordNumSplitter.class)
                .via(Processor.class)
                .run(args);
    }
}