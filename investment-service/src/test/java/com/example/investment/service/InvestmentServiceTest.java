package com.example.investment.service;

import com.example.investment.dto.CreateInvestmentRequest;
import com.example.investment.dto.InvestmentResponse;
import com.example.investment.model.Investment;
import com.example.investment.repository.InvestmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.investment.model.InvestmentType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvestmentServiceTest {

    @Mock
    private InvestmentRepository investmentRepository;

    @InjectMocks
    private InvestmentService investmentService;

    @BeforeEach
    public void setup(){

    }

    @Test
    void findAllInvestmentsMade_ReturnListOfInvestmentResponse(){
        Investment investment = new Investment();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.1));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        List<Investment> investmentList = Arrays.asList(investment);
        when(investmentRepository.findAll()).thenReturn(investmentList);

        List<InvestmentResponse> investmentResponses = investmentService.findAllInvestmentsMade();
        assertEquals(investmentResponses.get(0).getId(),1L);
        assertEquals(investmentResponses.get(0).getInvestmentType(),CRYPTO);
        assertEquals(investmentResponses.get(0).getAssetSymbol(),"BTC");
        assertEquals(investmentResponses.get(0).getAmountInvested(),BigDecimal.valueOf(1000));
        assertEquals(investmentResponses.get(0).getQuantity(),BigDecimal.valueOf(0.1));
        assertEquals(investmentResponses.get(0).getInvestmentDate(),
                LocalDate.of(2025, 10, 8));
        assertEquals(investmentResponses.get(0).getCurrency(),"BRL");
    }

    @Test
    void findInvestmentTransactionWithID_ReturnInvestmentResponseWithID(){
        Investment investment = new Investment();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.1));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        List<Investment> investmentList = Arrays.asList(investment);
        when(investmentRepository.findById(1L)).thenReturn(Optional.of(investment));

        InvestmentResponse investmentResponses = investmentService.findInvestmentTransactionWithID(1L);
        assertEquals(investmentResponses.getId(),1L);
        assertEquals(investmentResponses.getInvestmentType(),CRYPTO);
        assertEquals(investmentResponses.getAssetSymbol(),"BTC");
        assertEquals(investmentResponses.getAmountInvested(),BigDecimal.valueOf(1000));
        assertEquals(investmentResponses.getQuantity(),BigDecimal.valueOf(0.1));
        assertEquals(investmentResponses.getInvestmentDate(),
                LocalDate.of(2025, 10, 8));
        assertEquals(investmentResponses.getCurrency(),"BRL");
    }

    @Test
    void saveInvestment_ReturnInvestmentResponseCreated(){
        Investment investment = new Investment();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.1));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        CreateInvestmentRequest createInvestmentRequest = new CreateInvestmentRequest();
        createInvestmentRequest.setInvestmentType(CRYPTO);
        createInvestmentRequest.setAssetSymbol("BTC");
        createInvestmentRequest.setAmountInvested(BigDecimal.valueOf(1000));
        createInvestmentRequest.setQuantity(BigDecimal.valueOf(0.1));
        createInvestmentRequest.setInvestmentDate(LocalDate.of(2025, 10, 8));
        createInvestmentRequest.setCurrency("BRL");

        when(investmentRepository.save(any(Investment.class))).thenReturn(investment);
        InvestmentResponse investmentResponse = investmentService.saveInvestment(createInvestmentRequest);

        assertNotNull(investmentResponse);
        assertEquals(investmentResponse.getInvestmentType(),CRYPTO);
        assertEquals(investmentResponse.getAssetSymbol(),"BTC");
        assertEquals(investmentResponse.getAmountInvested(),BigDecimal.valueOf(1000));
        assertEquals(investmentResponse.getQuantity(),BigDecimal.valueOf(0.1));
        assertEquals(investmentResponse.getInvestmentDate(),
                LocalDate.of(2025, 10, 8));
        assertEquals(investmentResponse.getCurrency(),"BRL");
    }

    @Test
    void saveInvestment_ThrowRunTimeException_WhenFailedToSaveInvestment(){
        Investment investment = new Investment();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.1));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        CreateInvestmentRequest createInvestmentRequest = new CreateInvestmentRequest();
        createInvestmentRequest.setInvestmentType(CRYPTO);
        createInvestmentRequest.setAssetSymbol("BTC");
        createInvestmentRequest.setAmountInvested(BigDecimal.valueOf(1000));
        createInvestmentRequest.setQuantity(BigDecimal.valueOf(0.1));
        createInvestmentRequest.setInvestmentDate(LocalDate.of(2025, 10, 8));
        createInvestmentRequest.setCurrency("BRL");

        when(investmentRepository.save(any(Investment.class))).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, ()-> investmentService.saveInvestment(createInvestmentRequest));
    }

    @Test
    void removeInvestment_DeleteInvestment_WithID(){
        when(investmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(investmentRepository).deleteById(1L);

        investmentService.removeInvestment(1L);
        verify(investmentRepository, times(1)).existsById(1L);
        verify(investmentRepository, times(1)).deleteById(1L);
    }
    @Test
    void removeInvestment_ThrowRunTimeException_WhenInvestmentNotFound(){
        long id = 1L;

        when(investmentRepository.existsById(id)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->investmentService.removeInvestment(id));
        assertEquals("Investment with ID " + id + " not found", exception.getMessage());
    }

    @Test
    void removeInvestment_ThrowRunTimeException_WhenFailedToDeleteInvestment(){
        long id = 1L;

        when(investmentRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(investmentRepository).deleteById(id);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->investmentService.removeInvestment(id));
        assertEquals("Failed to delete Investment with ID " + id, exception.getMessage());
    }

    @Test
    void editInvestmentWithID_EditInvestment_ReturnInvestmentResponse(){
        Investment investment = new Investment();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.1));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        CreateInvestmentRequest createInvestmentRequest = new CreateInvestmentRequest();
        createInvestmentRequest.setInvestmentType(CRYPTO);
        createInvestmentRequest.setAssetSymbol("BTC");
        createInvestmentRequest.setAmountInvested(BigDecimal.valueOf(1000));
        createInvestmentRequest.setQuantity(BigDecimal.valueOf(0.2));
        createInvestmentRequest.setInvestmentDate(LocalDate.of(2025, 10, 8));
        createInvestmentRequest.setCurrency("BRL");

        Investment investmentUpdated = new Investment();
        investmentUpdated.setId(1L);
        investmentUpdated.setInvestmentType(CRYPTO);
        investmentUpdated.setAssetSymbol("BTC");
        investmentUpdated.setAmountInvested(BigDecimal.valueOf(1000));
        investmentUpdated.setQuantity(BigDecimal.valueOf(0.2));
        investmentUpdated.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investmentUpdated.setCurrency("BRL");

        when(investmentRepository.findById(1L)).thenReturn(Optional.of(investment));
        when(investmentRepository.save(any(Investment.class))).thenReturn(investmentUpdated);

        InvestmentResponse investmentResponse = investmentService.
                editInvestmentById(1L, createInvestmentRequest);

        assertEquals(investmentResponse.getInvestmentType(),CRYPTO);
        assertEquals(investmentResponse.getAssetSymbol(),"BTC");
        assertEquals(investmentResponse.getAmountInvested(),BigDecimal.valueOf(1000));
        assertEquals(investmentResponse.getQuantity(),BigDecimal.valueOf(0.2));
        assertEquals(investmentResponse.getInvestmentDate(),
                LocalDate.of(2025, 10, 8));
        assertEquals(investmentResponse.getCurrency(),"BRL");
    }

    @Test
    void editInvestmentById_ThrowRunTimeException_WhenFailedToEditInvestment(){
        Investment investment = new Investment();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.1));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        CreateInvestmentRequest createInvestmentRequest = new CreateInvestmentRequest();
        createInvestmentRequest.setInvestmentType(CRYPTO);
        createInvestmentRequest.setAssetSymbol("BTC");
        createInvestmentRequest.setAmountInvested(BigDecimal.valueOf(1000));
        createInvestmentRequest.setQuantity(BigDecimal.valueOf(0.2));
        createInvestmentRequest.setInvestmentDate(LocalDate.of(2025, 10, 8));
        createInvestmentRequest.setCurrency("BRL");

        Investment investmentUpdated = new Investment();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.2));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        when(investmentRepository.findById(1L)).thenReturn(Optional.of(investment));
        doThrow(new RuntimeException("Database error"))
                .when(investmentRepository).save(investmentUpdated);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->investmentService.editInvestmentById(1L,createInvestmentRequest));
        assertEquals("Failed to Edit Investment", exception.getMessage());
    }
}
