package com.example.investment.service;

import com.example.investment.dto.CreateWithdrawalRequest;
import com.example.investment.dto.WithdrawalResponse;
import com.example.investment.exception.InsufficientHoldingException;
import com.example.investment.exception.WithdrawalNotFoundException;
import com.example.investment.model.AssetHolding;
import com.example.investment.model.Investment;
import com.example.investment.model.Withdrawal;
import com.example.investment.repository.WithdrawalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.example.investment.model.InvestmentType.CRYPTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class WithdrawalServiceTest {

    @Mock
    private WithdrawalRepository withdrawalRepository;

    @Mock
    private PortfolioSummaryService portfolioSummaryService;

    @InjectMocks
    private WithdrawalService withdrawalService;

    @BeforeEach
    public void setup(){
    }

    @Test
    void findAllWithdrawalsMade_ReturnList_WithAllWithdrawals(){
        Withdrawal firstWithdrawal = new Withdrawal();
        firstWithdrawal.setId(1L);
        firstWithdrawal.setInvestmentType(CRYPTO);
        firstWithdrawal.setAssetSymbol("BTC");
        firstWithdrawal.setProceeds(BigDecimal.valueOf(1000));
        firstWithdrawal.setQuantity(BigDecimal.valueOf(1));
        firstWithdrawal.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        Withdrawal secondWithdrawal = new Withdrawal();
        secondWithdrawal.setId(2L);
        secondWithdrawal.setInvestmentType(CRYPTO);
        secondWithdrawal.setAssetSymbol("BTC");
        secondWithdrawal.setProceeds(BigDecimal.valueOf(2000));
        secondWithdrawal.setQuantity(BigDecimal.valueOf(2));
        secondWithdrawal.setWithdrawalDate(LocalDate.of(2025, 11, 25));

        List<Withdrawal> withdrawalList = new ArrayList<Withdrawal>();
        withdrawalList.add(firstWithdrawal);
        withdrawalList.add(secondWithdrawal);

        when(withdrawalRepository.findAll()).thenReturn(withdrawalList);

        List<WithdrawalResponse> response = withdrawalService.findAllWithdrawalsMade();
        assertEquals(2,response.size());
    }

    @Test
    void findWithdrawalById_ReturnWithdrawalResponse_WithWithdrawal(){
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(1L);
        withdrawal.setInvestmentType(CRYPTO);
        withdrawal.setAssetSymbol("BTC");
        withdrawal.setProceeds(BigDecimal.valueOf(1000));
        withdrawal.setQuantity(BigDecimal.valueOf(1));
        withdrawal.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        when(withdrawalRepository.findById(1L)).thenReturn(Optional.of(withdrawal));
        WithdrawalResponse withdrawalResponse = withdrawalService.findWithdrawalById(1L);
        assertEquals(1L, withdrawalResponse.getId());
    }

    @Test
    void saveWithdrawal_ReturnWithdrawalResponse_AfterSavingWithdrawalRequest(){
        CreateWithdrawalRequest createWithdrawalRequest = new CreateWithdrawalRequest();
        createWithdrawalRequest.setInvestmentType(CRYPTO);
        createWithdrawalRequest.setAssetSymbol("BTC");
        createWithdrawalRequest.setProceeds(BigDecimal.valueOf(1000));
        createWithdrawalRequest.setQuantity(BigDecimal.valueOf(1));
        createWithdrawalRequest.setWithdrawalDate(LocalDate.of(2025, 10, 8));


        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setInvestmentType(CRYPTO);
        withdrawal.setAssetSymbol("BTC");
        withdrawal.setProceeds(BigDecimal.valueOf(1000));
        withdrawal.setQuantity(BigDecimal.valueOf(1));
        withdrawal.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        AssetHolding assetHolding = new AssetHolding();
        assetHolding.setInvestmentType(CRYPTO);
        assetHolding.setAssetSymbol("BTC");
        assetHolding.setTotalAmountInvested(BigDecimal.valueOf(10000));
        assetHolding.setTotalQuantity(BigDecimal.valueOf(100));
        assetHolding.setPrimaryCurrency("USD");
        assetHolding.setAssetTag("BTC|CRYPTO");

        List<AssetHolding> assetList= new ArrayList<>();
        assetList.add(assetHolding);

        when(withdrawalRepository.save(any(Withdrawal.class))).thenReturn(withdrawal);
        when(portfolioSummaryService.getAssetList()).thenReturn(assetList);
        WithdrawalResponse response = withdrawalService.saveWithdrawal(createWithdrawalRequest);

        assertNotNull(response);
        assertEquals(response.getInvestmentType(),CRYPTO);
    }

    @Test
    void saveWithdrawal_ThrowInsufficientHoldingException(){
        CreateWithdrawalRequest createWithdrawalRequest = new CreateWithdrawalRequest();
        createWithdrawalRequest.setInvestmentType(CRYPTO);
        createWithdrawalRequest.setAssetSymbol("BTC");
        createWithdrawalRequest.setProceeds(BigDecimal.valueOf(1000));
        createWithdrawalRequest.setQuantity(BigDecimal.valueOf(1));
        createWithdrawalRequest.setWithdrawalDate(LocalDate.of(2025, 10, 8));


        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setInvestmentType(CRYPTO);
        withdrawal.setAssetSymbol("BTC");
        withdrawal.setProceeds(BigDecimal.valueOf(1000));
        withdrawal.setQuantity(BigDecimal.valueOf(1));
        withdrawal.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        AssetHolding assetHolding = new AssetHolding();
        assetHolding.setInvestmentType(CRYPTO);
        assetHolding.setAssetSymbol("BTC");
        assetHolding.setTotalAmountInvested(BigDecimal.valueOf(0));
        assetHolding.setTotalQuantity(BigDecimal.valueOf(0));
        assetHolding.setPrimaryCurrency("USD");
        assetHolding.setAssetTag("BTC|CRYPTO");

        List<AssetHolding> assetList= new ArrayList<>();
        assetList.add(assetHolding);

        when(portfolioSummaryService.getAssetList()).thenReturn(assetList);

        InsufficientHoldingException exception = assertThrows(InsufficientHoldingException.class, () -> withdrawalService.saveWithdrawal(createWithdrawalRequest));
        assertEquals("Insufficient Holding Amount to Continue with Withdraw", exception.getMessage());

    }

    @Test
    void removeWithdrawalWithID_RemovesWithdrawal(){
        long id = 1L;
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(id);
        withdrawal.setInvestmentType(CRYPTO);
        withdrawal.setAssetSymbol("BTC");
        withdrawal.setProceeds(BigDecimal.valueOf(1000));
        withdrawal.setQuantity(BigDecimal.valueOf(1));
        withdrawal.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        when(withdrawalRepository.existsById(id)).thenReturn(true);
        doNothing().when(withdrawalRepository).deleteById(id);

        withdrawalService.removeWithdrawalWithID(id);
        verify(withdrawalRepository, times(1)).existsById(1L);
        verify(withdrawalRepository, times(1)).deleteById(1L);
    }

    @Test
    void removeWithdrawalWithID_ThrowWithdrawalNotFoundException_WhenUnableToFindID(){
        long id = 1L;
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(id);
        withdrawal.setInvestmentType(CRYPTO);
        withdrawal.setAssetSymbol("BTC");
        withdrawal.setProceeds(BigDecimal.valueOf(1000));
        withdrawal.setQuantity(BigDecimal.valueOf(1));
        withdrawal.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        when(withdrawalRepository.existsById(id)).thenReturn(false);

        WithdrawalNotFoundException response = assertThrows(WithdrawalNotFoundException.class, () -> withdrawalService.removeWithdrawalWithID(id));
    }

    @Test
    void editWithdrawalWithID_SavesUpdatedInvestment_ReturnUpdatedWithdrawalResponse(){
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(1L);
        withdrawal.setInvestmentType(CRYPTO);
        withdrawal.setAssetSymbol("BTC");
        withdrawal.setProceeds(BigDecimal.valueOf(1000));
        withdrawal.setQuantity(BigDecimal.valueOf(1));
        withdrawal.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        CreateWithdrawalRequest createWithdrawalRequest = new CreateWithdrawalRequest();
        createWithdrawalRequest.setInvestmentType(CRYPTO);
        createWithdrawalRequest.setAssetSymbol("BTC");
        createWithdrawalRequest.setProceeds(BigDecimal.valueOf(10000));
        createWithdrawalRequest.setQuantity(BigDecimal.valueOf(10));
        createWithdrawalRequest.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        Withdrawal updatedWithdrawal = new Withdrawal();
        updatedWithdrawal.setId(1L);
        updatedWithdrawal.setInvestmentType(CRYPTO);
        updatedWithdrawal.setAssetSymbol("BTC");
        updatedWithdrawal.setProceeds(BigDecimal.valueOf(10000));
        updatedWithdrawal.setQuantity(BigDecimal.valueOf(10));
        updatedWithdrawal.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        when(withdrawalRepository.existsById(1L)).thenReturn(true);
        when(withdrawalRepository.findById(1L)).thenReturn(Optional.of(withdrawal));
        when(withdrawalRepository.save(any(Withdrawal.class))).thenReturn(updatedWithdrawal);

        WithdrawalResponse response = withdrawalService.editWithdrawalWithID(1L, createWithdrawalRequest);
        assertEquals(1L, response.getId());
        assertEquals(CRYPTO, response.getInvestmentType());
        assertEquals("BTC", response.getAssetSymbol());
        assertEquals(BigDecimal.valueOf(10000), response.getProceeds());
    }

    @Test
    void editWithdrawalWithID_ThrowWithdrawalNotFoundException_WhenUnableToFindID(){
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(1L);
        withdrawal.setInvestmentType(CRYPTO);
        withdrawal.setAssetSymbol("BTC");
        withdrawal.setProceeds(BigDecimal.valueOf(1000));
        withdrawal.setQuantity(BigDecimal.valueOf(1));
        withdrawal.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        CreateWithdrawalRequest createWithdrawalRequest = new CreateWithdrawalRequest();
        createWithdrawalRequest.setInvestmentType(CRYPTO);
        createWithdrawalRequest.setAssetSymbol("BTC");
        createWithdrawalRequest.setProceeds(BigDecimal.valueOf(10000));
        createWithdrawalRequest.setQuantity(BigDecimal.valueOf(10));
        createWithdrawalRequest.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        Withdrawal updatedWithdrawal = new Withdrawal();
        updatedWithdrawal.setId(1L);
        updatedWithdrawal.setInvestmentType(CRYPTO);
        updatedWithdrawal.setAssetSymbol("BTC");
        updatedWithdrawal.setProceeds(BigDecimal.valueOf(10000));
        updatedWithdrawal.setQuantity(BigDecimal.valueOf(10));
        updatedWithdrawal.setWithdrawalDate(LocalDate.of(2025, 10, 8));

        when(withdrawalRepository.existsById(1L)).thenReturn(false);

        WithdrawalNotFoundException response = assertThrows(WithdrawalNotFoundException.class, () -> withdrawalService.editWithdrawalWithID(1L,createWithdrawalRequest));

    }

}
