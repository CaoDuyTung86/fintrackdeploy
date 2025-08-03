package com.example.be_fintrack.controller;

import com.example.be_fintrack.entity.Category;
import com.example.be_fintrack.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category c) {
        return ResponseEntity.ok(service.create(c));
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Category>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(service.getByType(type));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
