package com.example.expense.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ExpenseSummaryResponse {

    private BigDecimal totalExpenses;
    private BigDecimal averageDailyExpense;
    private String highestSpentCategory;

    private Map<String, BigDecimal> totalPerCategory;
    private List<ExpenseResponse> detailedExpenses;

    public ExpenseSummaryResponse(){}

    public ExpenseSummaryResponse(BigDecimal totalExpenses, BigDecimal averageDailyExpense, String highestSpentCategory,Map<String, BigDecimal> totalPerCategory ,List<ExpenseResponse> detailedExpenses){
        this.totalExpenses=totalExpenses;
        this.averageDailyExpense=averageDailyExpense;
        this.highestSpentCategory=highestSpentCategory;
        this.totalPerCategory=totalPerCategory;
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

    public Map<String, BigDecimal> getTotalPerCategory() {
        return totalPerCategory;
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

    public void setTotalPerCategory(Map<String, BigDecimal> totalPerCategory) {
        this.totalPerCategory = totalPerCategory;
    }
    public void setDetailedExpenses(List<ExpenseResponse> detailedExpenses) {
        this.detailedExpenses = detailedExpenses;
    }
}
