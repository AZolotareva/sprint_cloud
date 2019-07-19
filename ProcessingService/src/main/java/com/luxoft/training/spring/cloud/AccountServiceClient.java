package com.luxoft.training.spring.cloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountServiceClient {
    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public boolean checkout(Integer accountId, BigDecimal sum) {
        String token = ((OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getTokenValue();
        return checkout(token, accountId, sum);
    }

    @HystrixCommand(fallbackMethod = "checkoutFallback")
    private boolean checkout(String token, Integer accountId, BigDecimal sum) {
        oAuth2RestTemplate.getOAuth2ClientContext().setAccessToken(new DefaultOAuth2AccessToken(token));
        String url = "http://AccountService/checkout/" + accountId.toString() + "?sum=" + sum.toPlainString();
        return oAuth2RestTemplate.getForObject(url, Boolean.class);
    }

    private boolean checkoutFallback(String token, Integer accountId, BigDecimal sum) {
        return false;
    }
}
