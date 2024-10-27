package com.free.sticker.controller;

import com.free.sticker.models.Customer;
import com.free.sticker.models.CustomerDTO;
import com.free.sticker.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.mapToDTOList(customerService.getAllCustomers());
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

}
