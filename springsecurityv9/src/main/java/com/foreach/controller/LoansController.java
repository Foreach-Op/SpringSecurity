package com.foreach.controller;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {

    @GetMapping("/myLoans")
    @PostAuthorize("hasRole('USER')")
    public String getLoansDetails(){
        return "Loans Details";
    }
}
