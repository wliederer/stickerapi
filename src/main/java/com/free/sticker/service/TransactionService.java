package com.free.sticker.service;

import com.free.sticker.models.*;
import com.free.sticker.repository.AddressRepository;
import com.free.sticker.repository.CustomerRepository;
import com.free.sticker.repository.ProductRepository;
import com.free.sticker.repository.TransactionRepository;
import com.free.sticker.utils.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction createTransaction(TransactionDTO transactionDTO) {
        // Create a new Address from the DTO
        Address address = new Address();
        address.setLine1(transactionDTO.getAddress().getLine1());
        address.setLine2(transactionDTO.getAddress().getLine2());
        address.setCity(transactionDTO.getAddress().getCity());
        address.setState(transactionDTO.getAddress().getState());
        address.setZipcode(transactionDTO.getAddress().getZipcode());

        // Create a new Customer from the DTO
        Customer customer = new Customer();
        customer.setFirstName(transactionDTO.getCustomer().getFirstName());
        customer.setLastName(transactionDTO.getCustomer().getLastName());

        // Check if Address already exists
        Optional<Address> existingAddress = addressRepository.findByLine1AndCityAndStateAndZipcode(
                address.getLine1(), address.getCity(), address.getState(), address.getZipcode()
        );

        if (existingAddress.isPresent()) {
            address = existingAddress.get();
            address.setId(existingAddress.get().getId());
        } else {

        }
        // Check if Customer already exists
        Optional<Customer> existingCustomer = customerRepository.findByFirstNameAndLastName(
                customer.getFirstName(), customer.getLastName()
        );

        if (existingCustomer.isPresent()) {
            customer = existingCustomer.get();
            customer.setId(existingCustomer.get().getId());
        } else {
            customer = customerRepository.save(customer);
        }
        address.setCustomer(customer);
        // Set the customer in the new address
        if(!existingAddress.isPresent()) {
            address = addressRepository.save(address);
        }

        if(customer.getAddresses() == null) {
            customer.setAddresses(new ArrayList<>());
            customer.getAddresses().add(address);
        } else {
            customer.getAddresses().add(address);
        }
        address.setCustomer(customer);

        // Retrieve Product by ID
        Product product = productRepository.findById(transactionDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + transactionDTO.getProductId()));
        // Use a final variable for eventId
        final Long addressId = address.getId();
        //check if already has transaction
        List<Transaction> transactions = transactionRepository.findAll();
        boolean existingTransaction = transactions.stream()
                .anyMatch(t -> t.getProduct().getEvent().getId().equals(product.getEvent().getId())
                        && t.getAddress().getId().equals(addressId));

        if (existingTransaction) {
            throw new TransactionException("A transaction already exists for this address and event.");
        }
        //check inventory
        List<Product> products = productRepository.findAll();
        List<Product> eventProducts = products.stream()
                .filter(p -> p.getEvent().getId().equals(product.getEvent().getId())).toList();
        int productCount = eventProducts.size();
        Set<Long> eventProductIds = eventProducts.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
        List<Transaction> eventTransactions = transactions.stream()
                .filter(t -> eventProductIds.contains(t.getProduct().getId()))
                .toList();
        int transactionCount = eventTransactions.size();
        if(transactionCount >= productCount) {
            throw new TransactionException("Out of ceramics!");
        }

        // Create and save Transaction
        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setAddress(address);
        transaction.setProduct(product);
        transaction.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public TransactionDTO mapToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setTransactionDate(transaction.getTransactionDate());

        // Set CustomerDTO
        if (transaction.getCustomer() != null) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setFirstName(transaction.getCustomer().getFirstName());
            customerDTO.setLastName(transaction.getCustomer().getLastName());
            dto.setCustomer(customerDTO);
        }

        // Set AddressDTO
        if (transaction.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setLine1(transaction.getAddress().getLine1());
            addressDTO.setLine2(transaction.getAddress().getLine2());
            addressDTO.setCity(transaction.getAddress().getCity());
            addressDTO.setState(transaction.getAddress().getState());
            addressDTO.setZipcode(transaction.getAddress().getZipcode());
            dto.setAddress(addressDTO);
        }

        // Set productId (assuming you have the product's ID)
        if (transaction.getProduct() != null) {
            dto.setProductId(transaction.getProduct().getId());
        }

        return dto;
    }

    public List<TransactionDTO> mapToDTOList(List<Transaction> transactions) {
        return transactions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public TransactionPublicDTO mapToPublicDTO(Transaction transaction) {
        TransactionPublicDTO publicDTO = new TransactionPublicDTO();
        publicDTO.setId(transaction.getId());
        publicDTO.setProductId(transaction.getProduct() != null ? transaction.getProduct().getId() : null);
        publicDTO.setTransactionDate(transaction.getTransactionDate());
        // Add other non-sensitive fields as needed

        return publicDTO;
    }

    public List<TransactionPublicDTO> mapToPublicDTOList(List<Transaction> transactions) {
        return transactions.stream().map(this::mapToPublicDTO).collect(Collectors.toList());
    }
}
