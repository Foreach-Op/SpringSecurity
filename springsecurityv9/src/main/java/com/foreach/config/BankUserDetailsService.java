package com.foreach.config;

import com.foreach.model.Customer;
import com.foreach.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
//    public BankUserDetailsService(CustomerRepository customerRepository){
//        this.customerRepository = customerRepository;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found with mail "+username));
        List<SimpleGrantedAuthority> authorities = customer.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName())).toList();
        return new User(customer.getEmail(), customer.getPwd(), authorities);
    }
}
