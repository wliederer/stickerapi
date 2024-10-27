package com.free.sticker.repository;

import com.free.sticker.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);

}
