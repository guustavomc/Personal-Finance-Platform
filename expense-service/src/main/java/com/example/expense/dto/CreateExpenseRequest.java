package com.example.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateExpenseRequest {

    private String description;

    private String category;

    private BigDecimal value;

    private LocalDate date;

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }
    
    public BigDecimal getValue() {
        return value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
