package com.budget.model;


import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userId;

    private String name;

    @Enumerated(EnumType.STRING)
    private BudgetPeriodType budgetPeriodType;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private BudgetStatus budgetStatus;

    private BigDecimal totalPlannedAmount;
    private BigDecimal totalActualSpent;
    private BigDecimal totalActualInvested;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetCategory> categories = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public BudgetPeriodType getBudgetPeriodType() {
        return budgetPeriodType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BudgetStatus getBudgetStatus() {
        return budgetStatus;
    }

    public BigDecimal getTotalPlannedAmount() {
        return totalPlannedAmount;
    }

    public BigDecimal getTotalActualSpent() {
        return totalActualSpent;
    }

    public BigDecimal getTotalActualInvested() {
        return totalActualInvested;
    }

    public List<BudgetCategory> getCategories() {
        return categories;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBudgetPeriodType(BudgetPeriodType budgetPeriodType) {
        this.budgetPeriodType = budgetPeriodType;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setBudgetStatus(BudgetStatus budgetStatus) {
        this.budgetStatus = budgetStatus;
    }

    public void setTotalPlannedAmount(BigDecimal totalPlannedAmount) {
        this.totalPlannedAmount = totalPlannedAmount;
    }

    public void setTotalActualSpent(BigDecimal totalActualSpent) {
        this.totalActualSpent = totalActualSpent;
    }

    public void setTotalActualInvested(BigDecimal totalActualInvested) {
        this.totalActualInvested = totalActualInvested;
    }

    public void setCategories(List<BudgetCategory> categories) {
        this.categories = categories;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addCategory(BudgetCategory budgetCategory){
        this.categories.add(budgetCategory);
        budgetCategory.setBudget(this);
    }

    public void removeCategory(BudgetCategory budgetCategory){
        this.categories.remove(budgetCategory);
        budgetCategory.setBudget(null);

    }
}
