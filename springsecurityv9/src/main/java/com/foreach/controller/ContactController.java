package com.foreach.controller;

import com.foreach.model.Customer;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContactController {

    @GetMapping("/contact")
    @PreFilter("filterObject.email != 'Test")
    @PostFilter("filterObject.email != 'Test")
    public String saveContactInquiryDetails(@RequestBody List<Customer> customers){
        return "Contact Details";
    }
}
