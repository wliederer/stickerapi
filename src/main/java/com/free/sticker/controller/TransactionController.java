package com.free.sticker.controller;

import com.free.sticker.models.Transaction;
import com.free.sticker.models.TransactionDTO;
import com.free.sticker.models.TransactionPublicDTO;
import com.free.sticker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = transactionService.mapToDTOList(transactionService.getAllTransactions());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/public")
    public ResponseEntity<List<TransactionPublicDTO>> getAllPublicTransactions() {
        List<TransactionPublicDTO> publicTransactions = transactionService.mapToPublicDTOList(transactionService.getAllTransactions());
        return new ResponseEntity<>(publicTransactions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        Transaction createdTransaction = transactionService.createTransaction(transactionDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
