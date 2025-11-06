package com.example.expense.service;

import com.example.expense.dto.ExpenseResponse;
import com.example.expense.dto.ExpenseSummaryResponse;
import com.example.expense.exception.ExpenseNotFoundException;
import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseReportServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseReportService expenseReportService;

    @BeforeEach
    public void setup() {

    }

    @Test
    void findExpenseSummaryByMonth_ReturnExpenseSummaryResponse_FromSpecificMonth(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        Expense expenseTest2 = new Expense();
        expenseTest2.setId(2L);
        expenseTest2.setCategory("Food");
        expenseTest2.setDate(LocalDate.of(2025, 6, 18));
        expenseTest2.setDescription("Dinner with the gf");
        expenseTest2.setValueSpent(BigDecimal.valueOf(200));

        List<Expense> expenses = new ArrayList<>();

        expenses.add(expenseTest1);
        expenses.add(expenseTest2);

        LocalDate initialDate = LocalDate.of(2025,6,1);
        LocalDate endDate = LocalDate.of(2025,6,30);

        when(expenseRepository.findByDateBetween(initialDate,endDate)).thenReturn(expenses);
        ExpenseSummaryResponse expenseSummaryResponse = expenseReportService.findExpenseSummaryByMonth(2025,6);
        assertEquals(expenseSummaryResponse.getTotalExpenses(),BigDecimal.valueOf(225.5));
    }

    @Test
    void findExpenseSummaryByYear_ReturnExpenseSummaryResponse_FromSpecificYear(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        Expense expenseTest2 = new Expense();
        expenseTest2.setId(2L);
        expenseTest2.setCategory("Food");
        expenseTest2.setDate(LocalDate.of(2025, 1, 18));
        expenseTest2.setDescription("Dinner with the gf");
        expenseTest2.setValueSpent(BigDecimal.valueOf(200));

        List<Expense> expenses = new ArrayList<>();

        expenses.add(expenseTest1);
        expenses.add(expenseTest2);

        LocalDate initialDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);

        when(expenseRepository.findByDateBetween(initialDate,endDate)).thenReturn(expenses);
        ExpenseSummaryResponse expenseSummaryResponse = expenseReportService.findExpenseSummaryByYear(2025);
        assertEquals(expenseSummaryResponse.getTotalExpenses(),BigDecimal.valueOf(225.5));
    }

    @Test
    void findExpensesByMonth_ReturnExpenseResponseList_FromSpecificMonth(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        Expense expenseTest2 = new Expense();
        expenseTest2.setId(2L);
        expenseTest2.setCategory("Food");
        expenseTest2.setDate(LocalDate.of(2025, 6, 8));
        expenseTest2.setDescription("Dinner with the gf");
        expenseTest2.setValueSpent(BigDecimal.valueOf(200));

        List<Expense> expenses = new ArrayList<>();
        expenses.add(expenseTest1);
        expenses.add(expenseTest2);

        LocalDate initialDate = LocalDate.of(2025,6,1);
        LocalDate endDate = LocalDate.of(2025,6,30);

        when(expenseRepository.findByDateBetween(initialDate,endDate)).thenReturn(expenses);
        List<ExpenseResponse> expenseResponsesList = expenseReportService.findExpensesByMonth(2025, 6);

        assertEquals(2, expenseResponsesList.size());
        assertEquals(LocalDate.of(2025, 6, 18), expenseResponsesList.get(0).getDate());
        assertEquals(LocalDate.of(2025, 6, 8), expenseResponsesList.get(1).getDate());
    }

    @Test
    void findExpensesByMonth_ThrowExpenseNotFoundException_WhenFailedToFindFromSpecificMonth(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        Expense expenseTest2 = new Expense();
        expenseTest2.setId(2L);
        expenseTest2.setCategory("Food");
        expenseTest2.setDate(LocalDate.of(2025, 6, 8));
        expenseTest2.setDescription("Dinner with the gf");
        expenseTest2.setValueSpent(BigDecimal.valueOf(200));

        List<Expense> expenses = new ArrayList<>();
        expenses.add(expenseTest1);
        expenses.add(expenseTest2);

        LocalDate initialDate = LocalDate.of(2025,6,1);
        LocalDate endDate = LocalDate.of(2025,6,30);

        when(expenseRepository.findByDateBetween(initialDate,endDate)).thenThrow(new ExpenseNotFoundException("Failed to find expenses from Year 2025, Month 6"));

        RuntimeException exception = assertThrows(ExpenseNotFoundException.class, () -> expenseReportService.findExpensesByMonth(2025,6));
        assertEquals("Failed to find expenses from Year 2025, Month 6",exception.getMessage());
    }

    @Test
    void findExpensesByYear_ReturnExpenseResponseList_FromSpecificYear(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        Expense expenseTest2 = new Expense();
        expenseTest2.setId(2L);
        expenseTest2.setCategory("Food");
        expenseTest2.setDate(LocalDate.of(2025, 6, 8));
        expenseTest2.setDescription("Dinner with the gf");
        expenseTest2.setValueSpent(BigDecimal.valueOf(200));

        List<Expense> expenses = new ArrayList<>();
        expenses.add(expenseTest1);
        expenses.add(expenseTest2);

        LocalDate startDate = LocalDate.of(2025, 01,1);
        LocalDate endDate = LocalDate.of(2025, 12, startDate.lengthOfMonth());

        when(expenseRepository.findByDateBetween(startDate,endDate)).thenReturn(expenses);
        List<ExpenseResponse> expenseResponsesList = expenseReportService.findExpensesByYear(2025);

        assertEquals(2, expenseResponsesList.size());
        assertEquals(LocalDate.of(2025, 6, 18), expenseResponsesList.get(0).getDate());
        assertEquals(LocalDate.of(2025, 6, 8), expenseResponsesList.get(1).getDate());
    }

    @Test
    void findExpensesByYear_ThrowExpenseNotFoundException_WhenFailedToFindFromSpecificMonth(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        Expense expenseTest2 = new Expense();
        expenseTest2.setId(2L);
        expenseTest2.setCategory("Food");
        expenseTest2.setDate(LocalDate.of(2025, 6, 8));
        expenseTest2.setDescription("Dinner with the gf");
        expenseTest2.setValueSpent(BigDecimal.valueOf(200));

        List<Expense> expenses = new ArrayList<>();
        expenses.add(expenseTest1);
        expenses.add(expenseTest2);

        LocalDate startDate = LocalDate.of(2025, 01,1);
        LocalDate endDate = LocalDate.of(2025, 12, startDate.lengthOfMonth());

        when(expenseRepository.findByDateBetween(startDate,endDate)).thenThrow(new ExpenseNotFoundException("Failed to find expenses from Year 2025"));

        RuntimeException exception = assertThrows(ExpenseNotFoundException.class, () -> expenseReportService.findExpensesByYear(2025));
        assertEquals("Failed to find expenses from Year 2025",exception.getMessage());
    }
}
