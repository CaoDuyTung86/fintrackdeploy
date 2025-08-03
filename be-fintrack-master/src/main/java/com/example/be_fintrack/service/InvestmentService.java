package com.example.be_fintrack.service;

import com.example.be_fintrack.entity.Investment;
import com.example.be_fintrack.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository repo;

    public Investment create(Investment i) {
        return repo.save(i);
    }

    public List<Investment> getByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Investment update(Long id, Investment updated) {
        Investment i = repo.findById(id).orElseThrow();
        i.setAssetName(updated.getAssetName());
        i.setAssetType(updated.getAssetType());
        i.setQuantity(updated.getQuantity());
        i.setPurchasePrice(updated.getPurchasePrice());
        i.setCurrentPrice(updated.getCurrentPrice());
        i.setPurchaseDate(updated.getPurchaseDate());
        return repo.save(i);
    }
}
