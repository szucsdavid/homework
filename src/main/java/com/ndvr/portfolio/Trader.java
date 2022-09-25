package com.ndvr.portfolio;

import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trader {

    public enum Stock { IBM, GOOG, AAPL, TSLA; }

    private final HashMap<Stock, Double> prices;

    public Trader(HashMap<Stock, Double> prices) {
        this.prices = prices;
    }

    @Value
    public static class Position {
        Stock stock;
        int quantity;
    }

    @Value
    public static class TradeOrder {
        public enum Order { BUY, SELL; }
        Order order;
        Stock stock;
        int quantity;
    }

    /**
     * Compares the current positions' value to the expected weights and creates BUY/SELL orders to
     * get closer to the expected weights.
     * The portfolio should not get into a negative cash balance after the transactions,
     * there should not be trades smaller than $1000 (because commissions will eat up our profits!)
     *
     *
     * @param currentPositions what we currently have
     * @param expectedWeights what the value of positions should be, e.g. TSLA 0.6, IBM 0.3, AAPL 0.1
     *                        means that a $100 portfolio should contain $60 worth of TSLA, $30 IBM, $10 AAPL
     * @return list of trades to execute to get closer to the expected weights
     * @throws Exception
     */
    public List<TradeOrder> rebalance(List<Position> currentPositions, Map<Stock, Double> expectedWeights) throws Exception {
        Double portfolioValue = porfolioValue(currentPositions);
        List traderOrders = new ArrayList<TradeOrder>();
        Double portfolioValueAfterTrade = 0.0;
        isWeightsValid(expectedWeights);
        
        for(int i = 0; i < currentPositions.size(); i++){
            Stock currentStock = currentPositions.get(i).getStock();
            Double stockValue = portfolioValue * expectedWeights.get(currentStock);
            int totalQuantity = (int) (stockValue / prices.get(currentStock));
            System.out.println(totalQuantity);
            int tradeOrderQuantity = Math.abs(currentPositions.get(i).getQuantity() - totalQuantity);
            Boolean isTradeOrderValid = isTradeValueValid(tradeOrderQuantity, currentStock);
        
            portfolioValueAfterTrade += totalQuantity * prices.get(currentStock);
            
            if( totalQuantity > currentPositions.get(i).getQuantity() && isTradeOrderValid){
                traderOrders.add(new TradeOrder(TradeOrder.Order.BUY, currentPositions.get(i).getStock(), tradeOrderQuantity));               
            }else if ( totalQuantity < currentPositions.get(i).getQuantity() && isTradeOrderValid){
                traderOrders.add(new TradeOrder(TradeOrder.Order.SELL, currentPositions.get(i).getStock(), tradeOrderQuantity));
            }else{
                continue;
            }
        }
        if ( portfolioValueAfterTrade > portfolioValue){
            throw new Exception("Cash balance cannot be negative!");
        }
        return traderOrders;
    }

    public Double porfolioValue(List<Position> currentPositions){
        Double portfolioValue = 0.0;
        for (int i = 0; i < currentPositions.size(); i++){
            portfolioValue += currentPositions.get(i).getQuantity() * prices.get(currentPositions.get(i).getStock());
        }
        return portfolioValue;
    }

    public Boolean isTradeValueValid(int tradeOrderQuantity, Stock stock){
        if (tradeOrderQuantity * prices.get(stock) >= 1000){
            return true;
        }else{
            return false;
        }
    }

    public Boolean isWeightsValid(Map<Stock, Double> expectedWeights) throws Exception{
        Double totalWeight = 0.0;
        for( Stock stock: expectedWeights.keySet()){
            totalWeight += expectedWeights.get(stock);
        }
        if (totalWeight == 1.0){
            return true;
        } else{
            throw new Exception("ExpectedWeights are invalid!");
        }
    }

}
