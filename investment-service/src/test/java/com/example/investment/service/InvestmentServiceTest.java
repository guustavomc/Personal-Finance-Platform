package com.example.investment.service;

import com.example.investment.dto.CreateInvestmentRequest;
import com.example.investment.dto.InvestmentResponse;
import com.example.investment.exception.InvestmentNotFoundException;
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
    void findInvestmentTransactionWithInvestmentType_ReturnInvestmentResponseList(){
        Investment investment = new Investment();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.1));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        List<Investment> investmentList = Arrays.asList(investment);
        when(investmentRepository.findByInvestmentType("CRYPTO")).thenReturn(investmentList);

        List<InvestmentResponse> response = investmentService.findInvestmentsWithInvestmentType("CRYPTO");
        assertEquals(1, response.size());
        assertEquals("CRYPTO", response.get(0).getInvestmentType().toString());
    }

    @Test
    void findInvestmentTransactionWithInvestmentType_ThrowInvestmentNotFoundException(){
        Investment investment = new Investment();
        investment.setId(1L);
        investment.setInvestmentType(CRYPTO);
        investment.setAssetSymbol("BTC");
        investment.setAmountInvested(BigDecimal.valueOf(1000));
        investment.setQuantity(BigDecimal.valueOf(0.1));
        investment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        investment.setCurrency("BRL");

        List<Investment> investmentList = Arrays.asList(investment);
        when(investmentRepository.findByInvestmentType("CRYPTO")).thenThrow(InvestmentNotFoundException.class);

        assertThrows(InvestmentNotFoundException.class, () -> investmentService.findInvestmentsWithInvestmentType("CRYPTO"));

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
    void removeInvestment_ThrowInvestmentNotFoundException_WhenInvestmentNotFound(){
        long id = 1L;

        when(investmentRepository.existsById(id)).thenReturn(false);

        InvestmentNotFoundException exception = assertThrows(InvestmentNotFoundException.class,
                ()->investmentService.removeInvestment(id));
        assertEquals("Failed to find investment with id 1", exception.getMessage());
    }

    @Test
    void removeInvestment_ThrowRunTimeException_WhenFailedToDeleteInvestment(){
        long id = 1L;

        when(investmentRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("")).when(investmentRepository).deleteById(id);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->investmentService.removeInvestment(id));
        assertNotNull(exception);    
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
        when(investmentRepository.existsById(1L)).thenReturn(true);
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
    void editInvestmentById_ThrowInvestmentNotFoundException_WhenFailedToFindInvestment(){
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

        when(investmentRepository.existsById(1L)).thenReturn(false);

        InvestmentNotFoundException exception = assertThrows(InvestmentNotFoundException.class,
                ()->investmentService.editInvestmentById(1L, createInvestmentRequest));
        assertEquals("Failed to find investment with id 1", exception.getMessage());
    }

    @Test
    void editInvestmentById_ThrowRunTimeException_WhenFailedToEditInvestment() {
        // Given
        Investment existingInvestment = new Investment();
        existingInvestment.setId(1L);
        existingInvestment.setInvestmentType(CRYPTO);
        existingInvestment.setAssetSymbol("BTC");
        existingInvestment.setAmountInvested(BigDecimal.valueOf(1000));
        existingInvestment.setQuantity(BigDecimal.valueOf(0.1));
        existingInvestment.setInvestmentDate(LocalDate.of(2025, 10, 8));
        existingInvestment.setCurrency("BRL");

        CreateInvestmentRequest request = new CreateInvestmentRequest();
        request.setInvestmentType(CRYPTO);
        request.setAssetSymbol("BTC");
        request.setAmountInvested(BigDecimal.valueOf(1000));
        request.setQuantity(BigDecimal.valueOf(0.2));
        request.setInvestmentDate(LocalDate.of(2025, 10, 8));
        request.setCurrency("BRL");

        // When
        when(investmentRepository.existsById(1L)).thenReturn(true); // Critical!
        when(investmentRepository.findById(1L)).thenReturn(Optional.of(existingInvestment));
        when(investmentRepository.save(any(Investment.class))).thenThrow(new RuntimeException(""));

        // Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            investmentService.editInvestmentById(1L, request)
        );

        assertNotNull(exception);    
    }
}
