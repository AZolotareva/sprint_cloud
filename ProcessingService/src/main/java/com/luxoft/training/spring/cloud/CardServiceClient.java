package com.luxoft.training.spring.cloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

@Component
public class CardServiceClient {
    @Autowired
    @Qualifier("OAuth2CardService")
    private OAuth2RestTemplate oAuth2CardService;

    @HystrixCommand(fallbackMethod = "createCardFallback")
    public String createCard() {
        return oAuth2CardService.getForObject("http://CardService/create", String.class);
    }

    private String createCardFallback() {
        return null;
    }
}