package com.example.be_fintrack.repository;

import com.example.be_fintrack.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByType(String type); // Tìm theo "income"/"expense"
}
