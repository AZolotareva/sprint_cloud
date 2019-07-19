package com.luxoft.training.spring.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardRest {
    @Autowired
    private CardNumberGenerator generator;

    @PreAuthorize("hasAuthority('CARD_WRITE')")
    @RequestMapping("create")
    public String createNewCard() {
        return generator.generate();
    }
}
