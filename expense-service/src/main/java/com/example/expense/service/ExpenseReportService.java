package com.example.expense.service;

import com.example.expense.dto.ExpenseResponse;
import com.example.expense.dto.ExpenseSummaryResponse;
import com.example.expense.exception.ExpenseNotFoundException;
import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseReportService {

    private ExpenseRepository expenseRepository;

    public ExpenseReportService(ExpenseRepository expenseRepository){
        this.expenseRepository=expenseRepository;
    }

    public ExpenseSummaryResponse findExpenseSummaryByMonth(int year, int month){
        List<ExpenseResponse> expensesFromGivenMonth = findExpensesByMonth(year,month);

        BigDecimal totalSpentInMonth = findTotalSpentByMonth(year, month);

        BigDecimal averageDailyExpenses = findAverageDailyExpensesFromMonth(year, month, totalSpentInMonth);

        Map<String, BigDecimal> totalPerCategory = findTotalPerCategory(expensesFromGivenMonth);

        String highestSpentCategory= findHighestSpentCategory(totalPerCategory);

        ExpenseSummaryResponse summaryResponse = new ExpenseSummaryResponse(
                totalSpentInMonth,
                averageDailyExpenses,
                highestSpentCategory,
                totalPerCategory,
                expensesFromGivenMonth);

        return summaryResponse;
    }

    public ExpenseSummaryResponse findExpenseSummaryByYear(int year){
        List<ExpenseResponse> expensesFromGivenYear = findExpensesByYear(year);

        BigDecimal totalSpentInYear = findTotalSpentByYear(year);

        BigDecimal averageDailyExpenses = findAverageDailyExpensesFromYear(year, totalSpentInYear);

        Map<String, BigDecimal> totalPerCategory = findTotalPerCategory(expensesFromGivenYear);

        String highestSpentCategory= findHighestSpentCategory(totalPerCategory);

        ExpenseSummaryResponse summaryResponse = new ExpenseSummaryResponse(
                totalSpentInYear,
                averageDailyExpenses,
                highestSpentCategory,
                totalPerCategory,
                expensesFromGivenYear);
        return summaryResponse;
    }

    private BigDecimal findTotalSpentByMonth(int year, int month){
        LocalDate startDate = LocalDate.of(year, month,1);
        LocalDate endDate = LocalDate.of(year, month, getDaysInMonth(year, month));
        BigDecimal totalSpent = expenseRepository.sumValueSpentByDateBetween(startDate, endDate);
        if (totalSpent != null){
            return  totalSpent;
        }
        else{
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal findTotalSpentByYear(int year){
        LocalDate startDate = LocalDate.of(year, 01,1);
        LocalDate endDate = LocalDate.of(year, 12, getDaysInMonth(year, 12));
        BigDecimal totalSpent = expenseRepository.sumValueSpentByDateBetween(startDate, endDate);
        if (totalSpent != null){
            return  totalSpent;
        }
        else{
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal findAverageDailyExpensesFromMonth(int year, int month, BigDecimal totalExpensesFromMonth) {
        BigDecimal averageDailyExpense= totalExpensesFromMonth.divide(BigDecimal.valueOf(getDaysInMonth(year, month)), 2, RoundingMode.HALF_UP);
        return  averageDailyExpense;
    }

    private BigDecimal findAverageDailyExpensesFromYear(int year, BigDecimal totalExpensesFromMonth) {
        BigDecimal averageDailyExpense= totalExpensesFromMonth.divide(BigDecimal.valueOf(getDaysInYear(year)), 2, RoundingMode.HALF_UP);
        return  averageDailyExpense;
    }

    private Map<String, BigDecimal> findTotalPerCategory(List<ExpenseResponse> expensesFromGivenMonth) {
        Map<String, BigDecimal> totalPerCategory = expensesFromGivenMonth.stream()
                .collect(Collectors.groupingBy(
                        ExpenseResponse::getCategory,
                        Collectors.mapping(ExpenseResponse::getValueSpent,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
        return totalPerCategory;
    }

    private String findHighestSpentCategory(Map<String, BigDecimal> totalPerCategory) {
        String highestSpentCategory = totalPerCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        return highestSpentCategory;
    }

    public List<ExpenseResponse> findExpensesByMonth(int year, int month){
        LocalDate startDate = LocalDate.of(year, month,1);
        LocalDate endDate = LocalDate.of(year, month, getDaysInMonth(year, month));
        try {
            return findVerifiedExpensesBetweenDates(startDate, endDate);
        }
        catch (Exception e){
            throw new ExpenseNotFoundException(String.format("Failed to find expenses from Year %d, Month %d", year, month));
        }
    }

    public List<ExpenseResponse> findExpensesByYear(int year){
        LocalDate startDate = LocalDate.of(year, 01,1);
        LocalDate endDate = LocalDate.of(year, 12, getDaysInMonth(year, 12));
        try {
            return findVerifiedExpensesBetweenDates(startDate, endDate);
        }
        catch (Exception e){
            throw new ExpenseNotFoundException(String.format("Failed to find expenses from Year %d", year));

        }
    }

    private List<ExpenseResponse> findVerifiedExpensesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByDateBetween(startDate, endDate).stream().map(expense -> mapExpenseToExpenseResponse(expense)).toList();
    }

    public int getDaysInMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    public int getDaysInYear(int year) {
        return Year.of(year).length();
    }

    public ExpenseResponse mapExpenseToExpenseResponse(Expense expense){
        ExpenseResponse response = new ExpenseResponse();

        response.setId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setCategory(expense.getCategory());
        response.setValueSpent(expense.getValueSpent());
        response.setDate(expense.getDate());
        response.setPaymentMethod(expense.getPaymentMethod());
        response.setTotalPurchaseValue(expense.getTotalPurchaseValue());
        response.setNumberOfInstallments(expense.getNumberOfInstallments());
        response.setCurrentInstallment(expense.getCurrentInstallment());
        response.setCurrency(expense.getCurrency());
        return response;
    }
}
