package com.example.be_fintrack.service;

import com.example.be_fintrack.entity.Transaction;
import com.example.be_fintrack.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction create(Transaction t) {
        return transactionRepository.save(t);
    }

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction update(Long id, Transaction t) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Cập nhật từng trường
        existing.setAmount(t.getAmount());
        existing.setType(t.getType());
        existing.setDate(t.getDate());
        existing.setDescription(t.getDescription());

        return transactionRepository.save(existing);
    }


    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }


    public List<Transaction> getByType(String type) {
        return transactionRepository.findByType(type);
    }
}
