package com.example.expense.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Id;
import jakarta.persistence.*;


@Entity
public class Expense {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    private String category;

    private BigDecimal valueSpent;

    private LocalDate date;

    public Expense(){}

    public Expense(String description, String category, BigDecimal valueSpent, LocalDate date){
        this.description=description;
        this.category=category;
        this.valueSpent=valueSpent;
        this.date=date;
    }


    public long getId() {
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

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description=description;
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
