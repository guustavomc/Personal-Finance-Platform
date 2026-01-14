package com.example.expense.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.expense.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByDateBetween(LocalDate start, LocalDate end);

    List<Expense> findByCategory(String category);

    List<Expense> findByPurchaseId(String purchaseId);

    @Query("SELECT SUM(e.valueSpent) FROM Expense e WHERE e.date BETWEEN :start AND :end")
    BigDecimal sumValueSpentByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
