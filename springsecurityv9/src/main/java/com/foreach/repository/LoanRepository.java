package com.foreach.repository;

import com.foreach.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends CrudRepository<Customer, Long> {

    @PreAuthorize("hasRole('USER')")
    List<Long> findByCustomerIdOrderByStartDtDesc(long customerId);
}
