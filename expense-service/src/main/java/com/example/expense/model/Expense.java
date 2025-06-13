package com.example.expense.model;

import java.math.BigDecimal;

public class Expense {

    private long id;

    private String Description;

    private String category;


    private BigDecimal value;


    public long getId() {
        return id;
    }

    public String getDescription() {
        return Description;
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
        Description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }



}
