package com.example.expense.dto;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseSummaryResponse {

    private BigDecimal totalExpenses;
    private BigDecimal averageDailyExpense;
    private String highestSpentCategory;

    List<ExpenseResponse> detailedExpenses;

    public ExpenseSummaryResponse(){}

    public ExpenseSummaryResponse(BigDecimal totalExpenses, BigDecimal averageDailyExpense, String highestSpentCategory, List<ExpenseResponse> detailedExpenses){
        this.totalExpenses=totalExpenses;
        this.averageDailyExpense=averageDailyExpense;
        this.highestSpentCategory=highestSpentCategory;
        this.detailedExpenses=detailedExpenses;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getAverageDailyExpense() {
        return averageDailyExpense;
    }

    public String getHighestSpentCategory() {
        return highestSpentCategory;
    }

    public List<ExpenseResponse> getDetailedExpenses() {
        return detailedExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public void setAverageDailyExpense(BigDecimal averageDailyExpense) {
        this.averageDailyExpense = averageDailyExpense;
    }

    public void setHighestSpentCategory(String highestSpentCategory) {
        this.highestSpentCategory = highestSpentCategory;
    }

    public void setDetailedExpenses(List<ExpenseResponse> detailedExpenses) {
        this.detailedExpenses = detailedExpenses;
    }
}
