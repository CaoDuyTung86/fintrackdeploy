package com.example.be_fintrack.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
public class StatisticsController {

    @Autowired
    private EntityManager em;

    /**
     * API 1: Thống kê tổng thu và chi theo từng tháng trong năm
     * GET /api/statistics/monthly?userId=1&year=2025
     */
    @GetMapping("/monthly")
    public Map<String, Object> getMonthlyStats(@RequestParam Long userId, @RequestParam int year) {
        String jpql = """
            SELECT MONTH(t.date),
                   SUM(CASE WHEN t.type = 'income' THEN t.amount ELSE 0 END),
                   SUM(CASE WHEN t.type = 'expense' THEN t.amount ELSE 0 END)
            FROM Transaction t
            WHERE t.user.id = :userId AND YEAR(t.date) = :year
            GROUP BY MONTH(t.date)
            ORDER BY MONTH(t.date)
        """;

        Query query = em.createQuery(jpql);
        query.setParameter("userId", userId);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();

        List<Map<String, Object>> monthlyStats = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> item = new HashMap<>();
            item.put("month", row[0]);
            item.put("totalIncome", row[1]);
            item.put("totalExpense", row[2]);
            monthlyStats.add(item);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("monthly", monthlyStats);
        return response;
    }

    /**
     * API 2: Thống kê tổng thu và chi theo từng danh mục
     * GET /api/statistics/by-category?userId=1&year=2025
     */
    @GetMapping("/by-category")
    public Map<String, Object> getStatsByCategory(@RequestParam Long userId, @RequestParam int year) {
        String jpql = """
            SELECT t.category.name,
                   SUM(CASE WHEN t.type = 'income' THEN t.amount ELSE 0 END),
                   SUM(CASE WHEN t.type = 'expense' THEN t.amount ELSE 0 END)
            FROM Transaction t
            WHERE t.user.id = :userId AND YEAR(t.createdAt) = :year
            GROUP BY t.category.name
            ORDER BY t.category.name
        """;

        Query query = em.createQuery(jpql);
        query.setParameter("userId", userId);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();

        List<Map<String, Object>> categoryStats = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> item = new HashMap<>();
            item.put("category", row[0]);
            item.put("totalIncome", row[1]);
            item.put("totalExpense", row[2]);
            categoryStats.add(item);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("byCategory", categoryStats);
        return response;
    }

    @GetMapping("/check-limit")
    public ResponseEntity<?> checkSpendingLimit(
            @RequestParam Long userId,
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam double limit
    ) {
        String jpql = """
        SELECT SUM(t.amount)
        FROM Transaction t
        WHERE t.user.id = :userId
        AND t.type = 'expense'
        AND YEAR(t.createdAt) = :year
        AND MONTH(t.createdAt) = :month
    """;

        Query query = em.createQuery(jpql);
        query.setParameter("userId", userId);
        query.setParameter("year", year);
        query.setParameter("month", month);

        Double totalExpense = (Double) query.getSingleResult();
        boolean isExceeded = totalExpense != null && totalExpense > limit;

        Map<String, Object> result = new HashMap<>();
        result.put("totalExpense", totalExpense != null ? totalExpense : 0);
        result.put("limit", limit);
        result.put("exceeded", isExceeded);

        return ResponseEntity.ok(result);
    }

}
