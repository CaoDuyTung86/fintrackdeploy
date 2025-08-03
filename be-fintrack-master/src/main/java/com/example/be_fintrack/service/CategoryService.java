package com.example.be_fintrack.service;

import com.example.be_fintrack.entity.Category;
import com.example.be_fintrack.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    public Category create(Category c) {
        return repo.save(c);
    }

    public List<Category> getAll() {
        return repo.findAll();
    }

    public List<Category> getByType(String type) {
        return repo.findByType(type);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
