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
        response.setAssetList(getAssetList());
        response.setTotalAmount(getTotalAmount());
        response.setNumberOfAssets(getAssetListSize());
        return response;
    }

    private List<PortfolioEvent> buildPortfolioEventTimeline(){
        return Stream
                .concat(mapInvestmentListToPortfolioEventList().stream(), mapWithdrawalListToPortfolioEventList()
                        .stream())
                .sorted((a,b) -> a.getDate().compareTo(b.getDate())).toList();
    }

    public List<AssetHolding> getAssetList(){
        List<PortfolioEvent> portfolioEventList = buildPortfolioEventTimeline();
        Map<String, AssetHolding> holdingMap = new HashMap<String, AssetHolding>();

        for(PortfolioEvent event: portfolioEventList){
            String tag = event.getAssetSymbol() + "|" + event.getInvestmentType();


            AssetHolding assetHolding = holdingMap.computeIfAbsent(tag, k-> {
                AssetHolding holding = new AssetHolding();
                holding.setInvestmentType(event.getInvestmentType());
                holding.setAssetSymbol(event.getAssetSymbol());
                holding.setTotalAmountInvested(BigDecimal.ZERO);
                holding.setTotalQuantity(BigDecimal.ZERO);
                holding.setPrimaryCurrency(event.getCurrency());
                holding.setAlternateTotalAmountInvested(BigDecimal.ZERO);
                holding.setAlternateCurrency(event.getAlternateCurrency());
                if (event.getAssetTag() != null) {
                    holding.setAssetTag(event.getAssetTag()); // if AssetHolding has this field
                }
                return holding;
            });

            BigDecimal fee = event.getFee() != null? event.getFee():BigDecimal.ZERO;
            BigDecimal netAmount = event.getAmount().subtract(fee);
            BigDecimal alternateAmount = event.getAlternateAmount() != null ? event.getAlternateAmount() : BigDecimal.ZERO;

            if(event.getEventType().equalsIgnoreCase("INVESTMENT")){


                assetHolding.setTotalAmountInvested(
                        assetHolding.getTotalAmountInvested()
                                .add(netAmount));

                assetHolding.setTotalQuantity(
                        assetHolding.getTotalQuantity()
                                .add(event.getQuantity()));

                assetHolding.setAlternateTotalAmountInvested(
                        assetHolding.getAlternateTotalAmountInvested()
                                .add(alternateAmount));
            }
            else if(event.getEventType().equalsIgnoreCase("WITHDRAWAL")){


                assetHolding.setTotalAmountInvested(
                        assetHolding.getTotalAmountInvested()
                                .subtract(netAmount));

                assetHolding.setTotalQuantity(
                        assetHolding.getTotalQuantity()
                                .subtract(event.getQuantity()));

                assetHolding.setAlternateTotalAmountInvested(
                        assetHolding.getAlternateTotalAmountInvested()
                                .subtract(alternateAmount));
            }
        }
        return new ArrayList<>(holdingMap.values());
    }

    private BigDecimal getTotalAmount(){
        List<AssetHolding> assets= getAssetList();
        return assets.stream()
                .map(AssetHolding::getTotalAmountInvested)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private int getAssetListSize(){
        return getAssetList().size();
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
