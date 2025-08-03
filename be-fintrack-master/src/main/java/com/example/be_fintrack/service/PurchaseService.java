package com.example.be_fintrack.service;

import com.example.be_fintrack.entity.Purchase;
import com.example.be_fintrack.entity.Transaction;
import com.example.be_fintrack.repository.PurchaseRepository;
import com.example.be_fintrack.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private PurchaseRepository repo;

    public Purchase create(Purchase p) {
        Purchase savedPurchase = repo.save(p);

        // Sau khi mua sắm được lưu, tạo giao dịch CHI gắn với user
        Transaction t = Transaction.builder()
                .type("CHI")
                .category("Mua sắm")
                .amount(p.getPrice())
                .description("Mua: " + p.getProductName())
                .date(p.getPurchaseDate())
                .user(p.getUser()) // Gán user cho transaction
                .build();

        transactionRepo.save(t);

        return savedPurchase;
    }

    public List<Purchase> getByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public Purchase update(Long id, Purchase newP) {
        Purchase p = repo.findById(id).orElseThrow();
        p.setProductName(newP.getProductName());
        p.setPrice(newP.getPrice());
        p.setStore(newP.getStore());
        p.setNote(newP.getNote());
        p.setProductLink(newP.getProductLink());
        p.setPurchaseDate(newP.getPurchaseDate());
        p.setUser(newP.getUser()); // Đảm bảo user được cập nhật (nếu cần)
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
