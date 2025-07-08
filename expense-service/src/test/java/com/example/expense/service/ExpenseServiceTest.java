package com.example.expense.service;

import com.example.expense.dto.CreateExpenseRequest;
import com.example.expense.dto.ExpenseResponse;
import com.example.expense.exception.ExpenseNotFoundException;
import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @BeforeEach
    public void setup() {

    }

    @Test
    void findAllExpenses_ReturnListOfExpenseResponses(){
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setCategory("Food");
        expense.setDate(LocalDate.of(2025, 6, 18));
        expense.setDescription("Lunch");
        expense.setValueSpent(BigDecimal.valueOf(25.50));

        List<Expense> expenses = Arrays.asList(expense);
        when(expenseRepository.findAll()).thenReturn(expenses);

        List<ExpenseResponse> expenseResponses = expenseService.findAllExpenses();
        assertEquals(1, expenseResponses.size());
        assertEquals("Food", expenseResponses.get(0).getCategory());
        assertEquals(LocalDate.of(2025, 6, 18), expenseResponses.get(0).getDate());
        assertEquals("Lunch", expenseResponses.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(25.50), expenseResponses.get(0).getValueSpent());


    }

    @Test
    void saveExpense_ReturnExpenseResponseCreated(){
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setCategory("Food");
        expense.setDate(LocalDate.of(2025, 6, 18));
        expense.setDescription("Lunch");
        expense.setValueSpent(BigDecimal.valueOf(25.50));

        CreateExpenseRequest createExpenseRequest = new CreateExpenseRequest();
        createExpenseRequest.setCategory("Food");
        createExpenseRequest.setDate(LocalDate.of(2025, 6, 18));
        createExpenseRequest.setDescription("Lunch");
        createExpenseRequest.setValueSpent(BigDecimal.valueOf(25.50));

        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        ExpenseResponse result = expenseService.saveExpense(createExpenseRequest);

        assertNotNull(result);
        assertEquals("Food", result.getCategory());
        assertEquals(LocalDate.of(2025, 6, 18), result.getDate());
        assertEquals("Lunch", result.getDescription());
        assertEquals(BigDecimal.valueOf(25.50), result.getValueSpent());

    }

    @Test
    void saveExpense_ThrowRuntimeException_WhenFailedToSaveExpense(){
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setCategory("Food");
        expense.setDate(LocalDate.of(2025, 6, 18));
        expense.setDescription("Lunch");
        expense.setValueSpent(BigDecimal.valueOf(25.50));

        CreateExpenseRequest createExpenseRequest = new CreateExpenseRequest();
        createExpenseRequest.setCategory("Food");
        createExpenseRequest.setDate(LocalDate.of(2025, 6, 18));
        createExpenseRequest.setDescription("Lunch");
        createExpenseRequest.setValueSpent(BigDecimal.valueOf(25.50));

        when(expenseRepository.save(any(Expense.class))).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class,() -> expenseService.saveExpense(createExpenseRequest));

    }

    @Test
    void findExpenseById_ReturnExpenseResponse_WithTheID(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        List<Expense> expenses = new ArrayList<>();
        expenses.add(expenseTest1);

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expenseTest1));
        ExpenseResponse expenseResponseWithRequestedID = expenseService.findExpenseById(1L);

        assertEquals("Food",expenseResponseWithRequestedID.getCategory());
        assertEquals("Lunch",expenseResponseWithRequestedID.getDescription());
        assertEquals(1L,expenseResponseWithRequestedID.getId());


    }

    @Test
    void findExpenseById_ThrowExpenseNotFoundException_WhenIDNotFound(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));


        List<Expense> expenses = new ArrayList<>();
        expenses.add(expenseTest1);

        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(ExpenseNotFoundException.class,() -> expenseService.findExpenseById(1L));
        assertEquals("Expense with ID 1 not found", exception.getMessage());
    }

    @Test
    void findExpensesByCategory_ReturnExpenseResponseList_WithTheCategory(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        Expense expenseTest2 = new Expense();
        expenseTest2.setId(2L);
        expenseTest2.setCategory("Food");
        expenseTest2.setDate(LocalDate.of(2024, 6, 18));
        expenseTest2.setDescription("Dinner with the gf");
        expenseTest2.setValueSpent(BigDecimal.valueOf(200));


        List<Expense> expenses = new ArrayList<>();
        expenses.add(expenseTest1);
        expenses.add(expenseTest2);

        when(expenseRepository.findByCategory("Food")).thenReturn(expenses);
        List<ExpenseResponse> expenseResponsesList = expenseService.findExpensesByCategory("Food");

        assertEquals(2, expenseResponsesList.size());
        assertEquals("Food", expenseResponsesList.get(0).getCategory());
    }

    @Test
    void findExpensesByCategory_ThrowExpenseNotFoundException_WhenFailedToFindCategory(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        Expense expenseTest2 = new Expense();
        expenseTest2.setId(2L);
        expenseTest2.setCategory("Food");
        expenseTest2.setDate(LocalDate.of(2024, 6, 18));
        expenseTest2.setDescription("Dinner with the gf");
        expenseTest2.setValueSpent(BigDecimal.valueOf(200));


        List<Expense> expenses = new ArrayList<>();
        expenses.add(expenseTest1);
        expenses.add(expenseTest2);

        when(expenseRepository.findByCategory("Food")).thenThrow(new ExpenseNotFoundException("Failed to find expenses with category Food"));
        RuntimeException exception = assertThrows(ExpenseNotFoundException.class, () -> expenseService.findExpensesByCategory("Food"));
        assertEquals("Failed to find expenses with category Food",exception.getMessage());
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
        List<ExpenseResponse> expenseResponsesList = expenseService.findExpensesByMonth(2025, 6);

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

        RuntimeException exception = assertThrows(ExpenseNotFoundException.class, () -> expenseService.findExpensesByMonth(2025,6));
        assertEquals("Failed to find expenses from Year 2025, Month 6",exception.getMessage());
    }

    @Test
    void removeExpense_DeleteExpense_WithTheID(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));


        when(expenseRepository.existsById(1L)).thenReturn(true);

        expenseService.removeExpense(1L);
        verify(expenseRepository).existsById(1L);
        verify(expenseRepository).deleteById(1L);
    }
    @Test
    void removeExpense_ThrowExpenseNotFoundException_WhenFailedToFindSpecificID(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        when(expenseRepository.existsById(1L)).thenReturn(false);
        RuntimeException exception = assertThrows(ExpenseNotFoundException.class, () -> expenseService.removeExpense(1L));
        assertEquals("Expense with ID 1 not found", exception.getMessage());
    }

    @Test
    void removeExpense_ThrowRuntimeException_WhenFailedToDeleteExpense(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        when(expenseRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException()).when(expenseRepository).deleteById(1L);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> expenseService.removeExpense(1L));
        assertEquals("Failed to delete Expense with ID 1", exception.getMessage());
    }

    @Test
    void editExpenseById_EditExpense_WithTheID(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        CreateExpenseRequest createExpenseRequest = new CreateExpenseRequest();
        createExpenseRequest.setCategory("Food");
        createExpenseRequest.setDate(LocalDate.of(2025, 6, 18));
        createExpenseRequest.setDescription("Lunch");
        createExpenseRequest.setValueSpent(BigDecimal.valueOf(25.50));

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expenseTest1));

        ExpenseResponse result = expenseService.editExpenseById(1L, createExpenseRequest);

        assertNotNull(result);
        assertEquals("Food", result.getCategory());
    }

    @Test
    void editExpenseById_ThrowExpenseNotFoundException_WhenFailedToFindSpecificID(){
        Expense expenseTest1 = new Expense();
        expenseTest1.setId(1L);
        expenseTest1.setCategory("Food");
        expenseTest1.setDate(LocalDate.of(2025, 6, 18));
        expenseTest1.setDescription("Lunch");
        expenseTest1.setValueSpent(BigDecimal.valueOf(25.50));

        CreateExpenseRequest createExpenseRequest = new CreateExpenseRequest();
        createExpenseRequest.setCategory("Food");
        createExpenseRequest.setDate(LocalDate.of(2025, 6, 18));
        createExpenseRequest.setDescription("Lunch");
        createExpenseRequest.setValueSpent(BigDecimal.valueOf(25.50));

        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(ExpenseNotFoundException.class, () -> expenseService.editExpenseById(1L, createExpenseRequest));
        assertEquals("Expense with ID 1 not found", exception.getMessage());
    }
}
