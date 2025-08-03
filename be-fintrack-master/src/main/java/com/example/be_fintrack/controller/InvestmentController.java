package com.example.be_fintrack.controller;

import com.example.be_fintrack.entity.Investment;
import com.example.be_fintrack.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/investments")
@CrossOrigin
public class InvestmentController {

    @Autowired
    private InvestmentService service;

    @PostMapping
    public ResponseEntity<Investment> create(@RequestBody Investment i) {
        return ResponseEntity.ok(service.create(i));
    }

    @GetMapping
    public ResponseEntity<List<Investment>> getByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Investment> update(@PathVariable Long id, @RequestBody Investment i) {
        return ResponseEntity.ok(service.update(id, i));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
