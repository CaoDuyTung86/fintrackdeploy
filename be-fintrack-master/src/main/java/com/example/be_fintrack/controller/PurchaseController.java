package com.example.be_fintrack.controller;

import com.example.be_fintrack.dto.PurchaseDTO;
import com.example.be_fintrack.entity.Purchase;
import com.example.be_fintrack.entity.User;
import com.example.be_fintrack.repository.UserRepository;
import com.example.be_fintrack.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin
public class PurchaseController {

    @Autowired
    private PurchaseService service;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Purchase> create(@RequestBody PurchaseDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id " + dto.getUserId()));

        Purchase p = new Purchase();
        p.setProductName(dto.getProductName());
        p.setPrice(dto.getPrice());
        p.setStore(dto.getStore());
        p.setNote(dto.getNote());
        p.setProductLink(dto.getProductLink());
        p.setPurchaseDate(LocalDate.parse(dto.getPurchaseDate()));
        p.setUser(user);

        return ResponseEntity.ok(service.create(p));
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Purchase> update(@PathVariable Long id, @RequestBody PurchaseDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id " + dto.getUserId()));

        Purchase p = new Purchase();
        p.setProductName(dto.getProductName());
        p.setPrice(dto.getPrice());
        p.setStore(dto.getStore());
        p.setNote(dto.getNote());
        p.setProductLink(dto.getProductLink());
        p.setPurchaseDate(LocalDate.parse(dto.getPurchaseDate()));
        p.setUser(user);

        return ResponseEntity.ok(service.update(id, p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
