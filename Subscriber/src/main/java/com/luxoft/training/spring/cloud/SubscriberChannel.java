package com.luxoft.training.spring.cloud;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface SubscriberChannel {
    String UPPER_WORDS = "upperWords";


    @Input(UPPER_WORDS)
    SubscribableChannel upperWords();
}
