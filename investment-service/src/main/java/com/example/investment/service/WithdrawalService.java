package com.example.investment.service;

import com.example.investment.dto.CreateInvestmentRequest;
import com.example.investment.dto.CreateWithdrawalRequest;
import com.example.investment.dto.InvestmentResponse;
import com.example.investment.dto.WithdrawalResponse;
import com.example.investment.exception.InsufficientHoldingException;
import com.example.investment.model.AssetHolding;
import com.example.investment.model.Investment;
import com.example.investment.model.InvestmentType;
import com.example.investment.model.Withdrawal;
import com.example.investment.repository.WithdrawalRepository;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class WithdrawalService {

    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private PortfolioSummaryService portfolioSummaryService;

    public WithdrawalService(WithdrawalRepository withdrawalRepository){
        this.withdrawalRepository=withdrawalRepository;
    }

    public WithdrawalResponse saveWithdrawal(CreateWithdrawalRequest createWithdrawalRequest){
        if(!verifyIfAmountIsAvailable(createWithdrawalRequest)){
            throw new InsufficientHoldingException("Insufficient Holding Amount to Continue with Withdraw");
        }
        Withdrawal withdrawal = withdrawalRepository.save(mapCreateWithdrawalRequestToWithdrawal(createWithdrawalRequest));
        return mapWithdrawalToWithdrawalResponse(withdrawal);
    }

    private boolean verifyIfAmountIsAvailable(CreateWithdrawalRequest createWithdrawalRequest) {
        Map<String, AssetHolding> currentHoldingMap = portfolioSummaryService.getStringAssetHoldingMap();
        AssetHolding asset = currentHoldingMap.get(createWithdrawalRequest.getAssetSymbol());
        if(asset.getTotalAmountInvested().intValue() >= createWithdrawalRequest.getProceeds().intValue()){
            return true;
        }
        return false;

    }

    public WithdrawalResponse mapWithdrawalToWithdrawalResponse(Withdrawal withdrawal){
        WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
        withdrawalResponse.setId(withdrawal.getId());
        withdrawalResponse.setInvestmentType(withdrawal.getInvestmentType());
        withdrawalResponse.setAssetSymbol(withdrawal.getAssetSymbol());
        withdrawalResponse.setProceeds(withdrawal.getProceeds());
        withdrawalResponse.setQuantity(withdrawal.getQuantity());
        withdrawalResponse.setWithdrawalDate(withdrawal.getWithdrawalDate());
        withdrawalResponse.setFee(withdrawal.getFee());
        withdrawalResponse.setAlternateAmount(withdrawal.getAlternateAmount());
        withdrawalResponse.setAlternateCurrency(withdrawal.getAlternateCurrency());

        return withdrawalResponse;
    }

    public Withdrawal mapCreateWithdrawalRequestToWithdrawal(CreateWithdrawalRequest createWithdrawalRequest){
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setInvestmentType(createWithdrawalRequest.getInvestmentType());
        withdrawal.setAssetSymbol(createWithdrawalRequest.getAssetSymbol());
        withdrawal.setProceeds(createWithdrawalRequest.getProceeds());
        withdrawal.setQuantity(createWithdrawalRequest.getQuantity());
        withdrawal.setWithdrawalDate(createWithdrawalRequest.getWithdrawalDate());
        withdrawal.setFee(createWithdrawalRequest.getFee());
        withdrawal.setAlternateAmount(createWithdrawalRequest.getAlternateAmount());
        withdrawal.setAlternateCurrency(createWithdrawalRequest.getAlternateCurrency());

        return withdrawal;
    }


}
