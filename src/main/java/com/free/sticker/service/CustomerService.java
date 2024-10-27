package com.free.sticker.service;

import com.free.sticker.models.Address;
import com.free.sticker.models.AddressDTO;
import com.free.sticker.models.Customer;
import com.free.sticker.models.CustomerDTO;
import com.free.sticker.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    public CustomerDTO mapToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setAddresses(customer.getAddresses().stream().map(this::mapAddressToDTO).collect(Collectors.toList()));
        return dto;
    }

    private AddressDTO mapAddressToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setLine1(address.getLine1());
        dto.setLine2(address.getLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipcode(address.getZipcode());
        return dto;
    }

    public List<CustomerDTO> mapToDTOList(List<Customer> customers) {
        return customers.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

}
