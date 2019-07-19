package com.luxoft.training.spring.cloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProcessingRest {
    private final String CARD_SERVICE_IS_NOT_AVAILABLE = "CARD_SERVICE_IS_NOT_AVAILABLE";
    @Autowired
    private ProcessingRepository repo;

    @Autowired
    private AccountServiceClient accountServiceClient;

    @Autowired
    private CardServiceClient cardServiceClient;

    @PreAuthorize("hasAuthority('PROCESSING')")
    @RequestMapping("/issue/{accountId}")
    public String issueNewCard(@PathVariable Integer accountId) {
        final String card = cardServiceClient.createCard();
        if (card==null) {
            return CARD_SERVICE_IS_NOT_AVAILABLE;
        }
        ProcessingEntity pe = new ProcessingEntity();
        pe.setCard(card);
        pe.setAccountId(accountId);
        repo.save(pe);
        return card;
    }

    @PreAuthorize("hasAuthority('PROCESSING')")
    @RequestMapping("/checkout/{card}")
    public boolean checkout(@PathVariable String card, @RequestParam BigDecimal sum) {
        ProcessingEntity pe = repo.findByCard(card);
        if (pe == null) {
            return false;
        }
        return accountServiceClient.checkout(pe.getAccountId(), sum);
    }

    @PreAuthorize("hasAuthority('PROCESSING')")
    @RequestMapping("/get")
    public Map<Integer, String> getByAccount(@RequestParam("account_id") List<Integer> accountIdList) {
        List<ProcessingEntity> list = repo.findByAccountIdIn(accountIdList);
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (ProcessingEntity pe: list) {
            map.put(pe.getAccountId(), pe.getCard());
        }
        return map;
    }

    @PreAuthorize("hasAuthority('PROCESSING')")
    @HystrixCommand(fallbackMethod = "testFallback")
    @RequestMapping("/test")
    public String testHystrix(Boolean fail) {
        if (Boolean.TRUE.equals(fail)) {
            throw new RuntimeException();
        }
        return "OK";
    }

    public String testFallback(Boolean fail){
        return "FAILED";
    }
}
