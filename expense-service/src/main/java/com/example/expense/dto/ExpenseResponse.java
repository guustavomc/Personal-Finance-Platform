package com.example.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponse {

    private Long id;

    private String description;

    private String category;

    private BigDecimal valueSpent;

    private LocalDate date;

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getValueSpent() {
        return valueSpent;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setValueSpent(BigDecimal valueSpent) {
        this.valueSpent = valueSpent;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    
}
