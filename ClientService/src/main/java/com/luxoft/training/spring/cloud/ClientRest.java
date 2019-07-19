package com.luxoft.training.spring.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientRest {
    @Autowired
    ClientDAO dao;

    @Autowired
    ClientRepository repo;

    @PreAuthorize("hasAuthority('CLIENT_WRITE')")
    @RequestMapping("/create")
    public Client create(@RequestParam String name) {
        return dao.create(name);
    }

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @RequestMapping("/update/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestParam String name) {
        if (dao.update(id, name)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('CLIENT_WRITE')")
    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        repo.delete(id);
    }

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @RequestMapping("/get")
    public List<? extends Client> get() {
        return repo.findAll();
    }

    @PreAuthorize("hasAuthority('CLIENT_READ')")
    @RequestMapping("/get/(id>")
    public Client get(@PathVariable Integer id) {
        return repo.findOne(id);
    }
}
