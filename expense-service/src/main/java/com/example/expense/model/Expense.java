package com.example.expense.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import jakarta.persistence.*;


@Entity
public class Expense {

    @Id 
    @GeneratedValue
    private long id;

    private String description;

    private String category;

    private BigDecimal value;

    public Expense(){}

    public Expense(String description, String category, BigDecimal value){
        this.description=description;
        this.category=category;
        this.value=value;
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

    public BigDecimal getValue() {
        return value;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }



}
