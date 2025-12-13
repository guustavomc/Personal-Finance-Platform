package com.example.investment.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.investment.dto.PortfolioEvent;
import com.example.investment.repository.WithdrawalRepository;
import org.springframework.stereotype.Service;
import com.example.investment.dto.PortfolioSummaryResponse;
import com.example.investment.model.AssetHolding;
import com.example.investment.model.Investment;
import com.example.investment.repository.InvestmentRepository;

@Service
public class PortfolioSummaryService {

    private InvestmentRepository investmentRepository;
    private WithdrawalRepository withdrawalRepository;

    public PortfolioSummaryService(InvestmentRepository investmentRepository, WithdrawalRepository withdrawalRepository){
        this.investmentRepository=investmentRepository;
        this.withdrawalRepository=withdrawalRepository;
    }

    public PortfolioSummaryResponse getPortfolioSummary(){
        PortfolioSummaryResponse response = new PortfolioSummaryResponse();
        response.setPortfolioEvents(buildPortfolioEventTimeline());
        response.getAssetList();
        return response;
    }



    private List<PortfolioEvent> buildPortfolioEventTimeline(){
        return Stream
                .concat(mapInvestmentListToPortfolioEventList().stream(), mapWithdrawalListToPortfolioEventList()
                        .stream())
                .sorted((a,b) -> a.getDate().compareTo(b.getDate())).toList();
    }

    private List<AssetHolding> getAssetList(){
        List<PortfolioEvent> portfolioEventList = buildPortfolioEventTimeline();
        Map<String, AssetHolding> holdingMap = new HashMap<String, AssetHolding>();

        for(PortfolioEvent event: portfolioEventList){
            String tag = event.getAssetTag();

            AssetHolding assetHolding = holdingMap.computeIfAbsent(tag, k-> {
                AssetHolding holding = new AssetHolding();
                holding.setInvestmentType(event.getInvestmentType());
                holding.setAssetSymbol(event.getAssetSymbol());
                holding.setTotalAmountInvested(event.getAmount());
                holding.setTotalQuantity(event.getQuantity());
                holding.setPrimaryCurrency(event.getCurrency());
                holding.setAlternateTotalAmountInvested(event.getAlternateAmount());
                holding.setAlternateCurrency(event.getAlternateCurrency());
                holding.setAssetTag(event.getAssetTag());
                return holding;
            });

            if(event.getEventType().equalsIgnoreCase("INVESTMENT")){
                BigDecimal fee = event.getFee() != null? event.getFee():BigDecimal.ZERO;
                BigDecimal netAmount = event.getAmount().subtract(fee);

                assetHolding.setTotalAmountInvested(
                        assetHolding.getTotalAmountInvested()
                                .add(netAmount));

                assetHolding.setTotalQuantity(
                        assetHolding.getTotalQuantity()
                                .add(event.getQuantity()));

                assetHolding.setAlternateTotalAmountInvested(
                        assetHolding.getAlternateTotalAmountInvested()
                                .add(event.getAlternateAmount()));
            }
            else if(event.getEventType().equalsIgnoreCase("WITHDRAWAL")){
                BigDecimal fee = event.getFee() != null? event.getFee():BigDecimal.ZERO;
                BigDecimal netAmount = event.getAmount().subtract(fee);

                assetHolding.setTotalAmountInvested(
                        assetHolding.getTotalAmountInvested()
                                .subtract(netAmount));

                assetHolding.setTotalQuantity(
                        assetHolding.getTotalQuantity()
                                .subtract(event.getQuantity()));

                assetHolding.setAlternateTotalAmountInvested(
                        assetHolding.getAlternateTotalAmountInvested()
                                .subtract(event.getAlternateAmount()));
            }
        }
        return new ArrayList<>(holdingMap.values());
    }

    private BigDecimal getTotalAmount(){

    }

    private List<PortfolioEvent> mapInvestmentListToPortfolioEventList(){
        return investmentRepository.findAll().stream()
                .map(inv -> new PortfolioEvent(
                        inv.getInvestmentType(),
                        inv.getAssetSymbol(),
                        inv.getAmountInvested(),
                        inv.getQuantity(),
                        inv.getInvestmentDate(),
                        inv.getCurrency(),
                        inv.getAlternateAmount() != null ? inv.getAlternateAmount() : BigDecimal.ZERO,
                        inv.getAlternateCurrency() != null ? inv.getAlternateCurrency() : "",
                        inv.getFee() != null ? inv.getFee() : BigDecimal.ZERO,
                        "INVESTMENT",
                        inv.getAssetTag()))
                .toList();
    }

    private List<PortfolioEvent> mapWithdrawalListToPortfolioEventList(){
        return withdrawalRepository.findAll().stream()
                .map(withdrawal -> new PortfolioEvent(
                        withdrawal.getInvestmentType(),
                        withdrawal.getAssetSymbol(),
                        withdrawal.getProceeds(),
                        withdrawal.getQuantity(),
                        withdrawal.getWithdrawalDate(),
                        withdrawal.getCurrency(),
                        withdrawal.getAlternateAmount()!= null ? withdrawal.getAlternateAmount() : BigDecimal.ZERO,
                        withdrawal.getAlternateCurrency() != null ? withdrawal.getAlternateCurrency() : "",
                        withdrawal.getFee() != null ? withdrawal.getFee() : BigDecimal.ZERO,
                        "WITHDRAWAL",
                        withdrawal.getAssetTag()))
                .toList();
    }
}
