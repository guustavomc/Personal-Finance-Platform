package com.example.investment.service;

import com.example.investment.dto.CreateWithdrawalRequest;
import com.example.investment.dto.WithdrawalResponse;
import com.example.investment.exception.InsufficientHoldingException;
import com.example.investment.exception.WithdrawalNotFoundException;
import com.example.investment.model.AssetHolding;
import com.example.investment.model.Withdrawal;
import com.example.investment.repository.WithdrawalRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class WithdrawalService {

    private WithdrawalRepository withdrawalRepository;

    private PortfolioSummaryService portfolioSummaryService;

    public WithdrawalService(WithdrawalRepository withdrawalRepository, PortfolioSummaryService portfolioSummaryService){
        this.withdrawalRepository=withdrawalRepository;
        this.portfolioSummaryService=portfolioSummaryService;
    }

    public List<WithdrawalResponse> findAllWithdrawalsMade(){
        return withdrawalRepository.findAll().stream().map(withdrawal -> mapWithdrawalToWithdrawalResponse(withdrawal)).toList();
    }

    public WithdrawalResponse findWithdrawalById(Long id){
        return mapWithdrawalToWithdrawalResponse(findVerifiedWithdrawalById(id));

    }

    private Withdrawal findVerifiedWithdrawalById(Long id){
        return  withdrawalRepository.findById(id).orElseThrow(() -> new WithdrawalNotFoundException(String.format("Failed to find withdrawal with id %d",id)));
    }

    public WithdrawalResponse saveWithdrawal(CreateWithdrawalRequest createWithdrawalRequest){
        if(!verifyIfAmountIsAvailable(createWithdrawalRequest)){
            throw new InsufficientHoldingException("Insufficient Holding Amount to Continue with Withdraw");
        }
        Withdrawal withdrawal = withdrawalRepository.save(mapCreateWithdrawalRequestToWithdrawal(createWithdrawalRequest));
        return mapWithdrawalToWithdrawalResponse(withdrawal);
    }

    public void removeWithdrawalWithID(long id){
        if(!withdrawalRepository.existsById(id)){
            throw new WithdrawalNotFoundException(String.format("Failed to find withdrawal with id %d",id));
        }
        else {
            removeVerifiedWithdrawalWithID(id);
        }
    }

    public void removeVerifiedWithdrawalWithID(long id){
        withdrawalRepository.deleteById(id);
    }

    public WithdrawalResponse editWithdrawalWithID(long id, CreateWithdrawalRequest createWithdrawalRequest){
        if(!withdrawalRepository.existsById(id)){
            throw new WithdrawalNotFoundException(String.format("Failed to find withdrawal with id %d",id));
        }
        else return mapWithdrawalToWithdrawalResponse(editVerifiedWithdrawalWithID(id, createWithdrawalRequest));
    }

    private Withdrawal editVerifiedWithdrawalWithID(long id, CreateWithdrawalRequest createWithdrawalRequest){
        Withdrawal withdrawal = findVerifiedWithdrawalById(id);
        withdrawal.setInvestmentType(createWithdrawalRequest.getInvestmentType());
        withdrawal.setAssetSymbol(createWithdrawalRequest.getAssetSymbol());
        withdrawal.setProceeds(createWithdrawalRequest.getProceeds());
        withdrawal.setQuantity(createWithdrawalRequest.getQuantity());
        withdrawal.setWithdrawalDate(createWithdrawalRequest.getWithdrawalDate());
        withdrawal.setFee(createWithdrawalRequest.getFee());
        withdrawal.setAlternateAmount(createWithdrawalRequest.getAlternateAmount());
        withdrawal.setAlternateCurrency(createWithdrawalRequest.getAlternateCurrency());
        return withdrawalRepository.save(withdrawal);
    }

    private boolean verifyIfAmountIsAvailable(CreateWithdrawalRequest createWithdrawalRequest) {
        Map<String, AssetHolding> currentHoldingMap = portfolioSummaryService.getStringAssetHoldingMap();
        AssetHolding asset = currentHoldingMap.get(createWithdrawalRequest.getAssetSymbol()+'|'+ createWithdrawalRequest.getInvestmentType());
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
