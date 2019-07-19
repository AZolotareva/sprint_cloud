package com.luxoft.training.spring.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class AccountRest {
    @Autowired AccountDAO dao;
    @Autowired AccountRepository repo;
    @Autowired RemoteEventPublisher eventPublisher;


//    @PreAuthorize("hasAuthority('ACCOUNT_WRITE')")
    @RequestMapping("/create")
    public void createDao(@RequestParam("client_id") Integer clientId){
        dao.create(clientId);
    }

//    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    @RequestMapping("/fund/{id}")
    public boolean fund(@PathVariable Integer id, @RequestParam BigDecimal sum) {
        try {
            return dao.addBalance(id, sum.abs());
        } finally {
            eventPublisher.publishEvent(new FundEvent("AccountService", "HistoryService", sum));
        }
    }

//    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    @RequestMapping("/checkout/{id}")
    public boolean checkout(@PathVariable Integer id, @RequestParam BigDecimal sum) {
        BigDecimal negativeSum = sum.abs().negate();
        try {
            return dao.addBalance(id, negativeSum);
        } finally {
            eventPublisher.publishEvent(new WithdrawEvent("AccountService", "HistoryService", negativeSum));
        }
    }

//    @PreAuthorize("hasAuthority('ACCOUNT_READ')")
    @RequestMapping("/get/{clientId}")
    public List<? extends Account> getByClient(@PathVariable Integer clientId) {
        return repo.findByClientId(clientId);
    }
}
