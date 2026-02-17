package com.budget.service;

import com.budget.dto.BudgetCategoryRequest;
import com.budget.dto.BudgetItem;
import com.budget.dto.BudgetResponse;
import com.budget.dto.CreateBudgetRequest;
import com.budget.model.Budget;
import com.budget.model.BudgetCategory;
import com.budget.model.BudgetPeriodType;
import com.budget.model.CategoryType;
import com.budget.repository.BudgetRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private BudgetItemService budgetItemService;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    public void setup(){
    }

    @Test
    void findBudgetWithID_ReturnBudgetResponseWithID(){
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("February");
        budget.setBudgetPeriodType(BudgetPeriodType.MONTHLY);
        budget.setStartDate(LocalDate.of(2026, 2, 1));
        budget.setEndDate(LocalDate.of(2026, 2, 28));
        budget.setTotalPlannedAmount(BigDecimal.valueOf(1000));


        BudgetCategory category1 = new BudgetCategory();
        category1.setId(2L);
        category1.setBudget(budget);
        category1.setCategoryName("Food");
        category1.setType(CategoryType.EXPENSE);
        category1.setPlannedAmount(BigDecimal.valueOf(1000));

        List<BudgetCategory> categories = new ArrayList<>();
        categories.add(category1);
        budget.setCategories(categories);

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));

        BudgetResponse response = budgetService.findBudgetWithID(1L);
        assertEquals(1L, response.getId());
        assertEquals("February", response.getName());
        assertEquals("Food", budget.getCategories().get(0).getCategoryName());
        assertEquals(CategoryType.EXPENSE, budget.getCategories().get(0).getType());
        assertEquals(BigDecimal.valueOf(1000), budget.getCategories().get(0).getPlannedAmount());
        assertEquals(2L, budget.getCategories().get(0).getId());
        assertEquals("February", budget.getCategories().get(0).getBudget().getName());


    }

    @Test
    void findUpdatedBudgetWithID_ReturnBudgetResponseWithID(){
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("February");
        budget.setBudgetPeriodType(BudgetPeriodType.MONTHLY);
        budget.setStartDate(LocalDate.of(2026, 2, 1));
        budget.setEndDate(LocalDate.of(2026, 2, 28));
        budget.setTotalPlannedAmount(BigDecimal.valueOf(1000));
        budget.setTotalActualSpent(BigDecimal.ZERO);
        budget.setTotalActualInvested(BigDecimal.ZERO);


        BudgetCategory category1 = new BudgetCategory();
        category1.setId(2L);
        category1.setBudget(budget);
        category1.setCategoryName("Food");
        category1.setType(CategoryType.EXPENSE);
        category1.setPlannedAmount(BigDecimal.valueOf(1000));
        category1.setActualSpent(BigDecimal.ZERO);
        category1.setActualInvested(BigDecimal.ZERO);

        BudgetCategory category2 = new BudgetCategory();
        category2.setId(3L);
        category2.setBudget(budget);
        category2.setCategoryName("Stock");
        category2.setType(CategoryType.INVESTMENT);
        category2.setPlannedAmount(BigDecimal.valueOf(1000));
        category2.setActualSpent(BigDecimal.ZERO);
        category2.setActualInvested(BigDecimal.ZERO);


        List<BudgetCategory> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        budget.setCategories(categories);

        BudgetItem budgetItem1 = new BudgetItem();
        budgetItem1.setType(CategoryType.EXPENSE);
        budgetItem1.setDescription("Food");
        budgetItem1.setBudgetItemCategory("Food");
        budgetItem1.setAmount(BigDecimal.valueOf(500));
        budgetItem1.setDate(LocalDate.of(2026, 2, 1));
        budgetItem1.setCurrency("BRL");
        budgetItem1.setPaymentMethod("Credit");

        BudgetItem budgetItem2 = new BudgetItem();
        budgetItem2.setType(CategoryType.INVESTMENT);
        budgetItem2.setDescription("Stock");
        budgetItem2.setBudgetItemCategory("Stock");
        budgetItem2.setAmount(BigDecimal.valueOf(500));
        budgetItem2.setDate(LocalDate.of(2026, 2, 1));
        budgetItem2.setCurrency("BRL");
        budgetItem2.setPaymentMethod("Credit");

        List<BudgetItem> itemList = new ArrayList<>();
        itemList.add(budgetItem1);
        itemList.add(budgetItem2);


        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));

        when(budgetItemService.getBudgetItemsForSpecificBudget(budget)).thenReturn(itemList);

        when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BudgetResponse refreshedResponse = budgetService.findUpdatedBudgetWithID(1L);
        assertEquals(1L, refreshedResponse.getId());
        assertEquals(BigDecimal.valueOf(500), refreshedResponse.getTotalActualSpent());
        assertEquals(BigDecimal.valueOf(500), refreshedResponse.getTotalActualInvested());


    }


    @Test
    void saveBudget_ReturnBudgetResponse(){
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("February");
        budget.setBudgetPeriodType(BudgetPeriodType.MONTHLY);
        budget.setStartDate(LocalDate.of(2026, 2, 1));
        budget.setEndDate(LocalDate.of(2026, 2, 28));
        budget.setTotalPlannedAmount(BigDecimal.valueOf(1000));

        BudgetCategory category1 = new BudgetCategory();
        category1.setId(2L);
        category1.setBudget(budget);
        category1.setCategoryName("Food");
        category1.setType(CategoryType.EXPENSE);
        category1.setPlannedAmount(BigDecimal.valueOf(1000));
        category1.setPercentageOfTotal(BigDecimal.valueOf(50));

        List<BudgetCategory> categories = new ArrayList<>();
        categories.add(category1);
        budget.setCategories(categories);

        CreateBudgetRequest createBudget = new CreateBudgetRequest();
        createBudget.setName("February");
        createBudget.setBudgetPeriodType(BudgetPeriodType.MONTHLY);
        createBudget.setStartDate(LocalDate.of(2026, 2, 1));
        createBudget.setEndDate(LocalDate.of(2026, 2, 28));
        createBudget.setTotalPlannedAmount(BigDecimal.valueOf(1000));

        BudgetCategoryRequest createCategory1 = new BudgetCategoryRequest();
        createCategory1.setCategoryName("Food");
        createCategory1.setPlannedAmount(BigDecimal.valueOf(1000));
        createCategory1.setType(CategoryType.EXPENSE);
        createCategory1.setPlannedAmount(BigDecimal.valueOf(1000));
        createCategory1.setPercentageOfTotal(BigDecimal.valueOf(50));

        List<BudgetCategoryRequest> categoriesRequest = new ArrayList<>();
        categoriesRequest.add(createCategory1);
        createBudget.setCategories(categoriesRequest);


        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
        BudgetResponse response = budgetService.saveBudget(createBudget);

        assertEquals(1L,response.getId());
        assertEquals("February", response.getName());
        assertEquals(BigDecimal.valueOf(1000), response.getTotalPlannedAmount());
        assertEquals(BigDecimal.valueOf(50), response.getCategories().get(0).getPercentageOfTotal());
    }

    @Test
    void removeBudgetWithID_RemovesBudget(){
        long id = 1L;
        when(budgetRepository.existsById(id)).thenReturn(true);
        doNothing().when(budgetRepository).deleteById(id);

        budgetService.removeBudgetWithID(id);
        verify(budgetRepository, times(1)).existsById(id);
        verify(budgetRepository, times(1)).deleteById(id);

    }
}
