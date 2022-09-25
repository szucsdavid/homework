package com.ndvr.portfolio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ndvr.portfolio.Trader.Position;
import com.ndvr.portfolio.Trader.Stock;
import com.ndvr.portfolio.Trader.TradeOrder;
import com.ndvr.portfolio.Trader.TradeOrder.Order;

public class TestTrader {
    @Test(description = "Test a happy path where a valid TradeOrder can be made")
    public void validTradeOrder() throws Exception{
        HashMap<Stock, Double> currentPrice = new HashMap<Stock, Double>();
        currentPrice.put(Stock.IBM, 100.5);
        currentPrice.put(Stock.GOOG, 90.9);
        currentPrice.put(Stock.AAPL, 100.0);
        currentPrice.put(Stock.TSLA, 88.8);

        Trader trader = new Trader(currentPrice);

        List<Position> currentPositions = new ArrayList<Position>(){{
            add(new Position(Stock.IBM, 1000));
            add(new Position(Stock.GOOG, 1000));
            add(new Position(Stock.AAPL, 1000));
            add(new Position(Stock.TSLA, 1000));
        }};

        Map<Stock, Double> expectedWeights = new HashMap<Stock,Double>() {{
            put(Stock.IBM, 0.25);
            put(Stock.GOOG, 0.25);
            put(Stock.AAPL, 0.15);
            put(Stock.TSLA, 0.35);
        }};

        List expectedTraderOrders = new ArrayList<TradeOrder>(){{
            add(new TradeOrder(Order.SELL, Stock.IBM, 55));
            add(new TradeOrder(Order.BUY, Stock.GOOG, 45));
            add(new TradeOrder(Order.SELL, Stock.AAPL, 430));
            add(new TradeOrder(Order.BUY, Stock.TSLA, 498));
        }};

        Assert.assertEquals(trader.rebalance(currentPositions, expectedWeights), expectedTraderOrders);
    }
    @Test(description = "Test what happens when no TradeOrder would be over 1000$")
    public void lowQuantities() throws Exception{
        HashMap<Stock, Double> currentPrice = new HashMap<Stock, Double>();
        currentPrice.put(Stock.IBM, 100.5);
        currentPrice.put(Stock.GOOG, 90.9);
        currentPrice.put(Stock.AAPL, 100.0);
        currentPrice.put(Stock.TSLA, 88.8);

        Trader trader = new Trader(currentPrice);

        List<Position> currentPositions = new ArrayList<Position>(){{
            add(new Position(Stock.IBM, 10));
            add(new Position(Stock.GOOG, 10));
            add(new Position(Stock.AAPL, 10));
            add(new Position(Stock.TSLA, 10));
        }};

        Map<Stock, Double> expectedWeights = new HashMap<Stock,Double>() {{
            put(Stock.IBM, 0.25);
            put(Stock.GOOG, 0.25);
            put(Stock.AAPL, 0.15);
            put(Stock.TSLA, 0.35);
        }};

        List expectedTraderOrders = new ArrayList<TradeOrder>();
       
        Assert.assertEquals(trader.rebalance(currentPositions, expectedWeights), expectedTraderOrders);
    }
    @Test(description = "Test what happens if one of the Stock price is 0.0")
    // Depends on the specifiacation. Right now TradeOrder is not created if the price is 0
    public void zeroPriceStock() throws Exception{
        HashMap<Stock, Double> currentPrice = new HashMap<Stock, Double>();
        currentPrice.put(Stock.IBM, 100.5);
        currentPrice.put(Stock.GOOG, 90.9);
        currentPrice.put(Stock.AAPL, 100.0);
        currentPrice.put(Stock.TSLA, 0.0);

        Trader trader = new Trader(currentPrice);

        List<Position> currentPositions = new ArrayList<Position>(){{
            add(new Position(Stock.IBM, 1000));
            add(new Position(Stock.GOOG, 1000));
            add(new Position(Stock.AAPL, 1000));
            add(new Position(Stock.TSLA, 1000));
        }};

        Map<Stock, Double> expectedWeights = new HashMap<Stock,Double>() {{
            put(Stock.IBM, 0.25);
            put(Stock.GOOG, 0.25);
            put(Stock.AAPL, 0.15);
            put(Stock.TSLA, 0.35);
        }};

        List expectedTraderOrders = new ArrayList<TradeOrder>(){{
            add(new TradeOrder(Order.SELL, Stock.IBM, 276));
            add(new TradeOrder(Order.SELL, Stock.GOOG, 199));
            add(new TradeOrder(Order.SELL, Stock.AAPL, 563));
        }};
       
        Assert.assertEquals(trader.rebalance(currentPositions, expectedWeights), expectedTraderOrders);
    }
    @Test(description = "Test what happens if the expectedWeight total is less then 1.0",
          expectedExceptionsMessageRegExp = "ExpectedWeights are invalid!",
          expectedExceptions = Exception.class)
    public void totalWeightInvalid() throws Exception{
        HashMap<Stock, Double> currentPrice = new HashMap<Stock, Double>();
        currentPrice.put(Stock.IBM, 100.5);
        currentPrice.put(Stock.GOOG, 90.9);
        currentPrice.put(Stock.AAPL, 100.0);
        currentPrice.put(Stock.TSLA, 88.8);

        Trader trader = new Trader(currentPrice);

        List<Position> currentPositions = new ArrayList<Position>(){{
            add(new Position(Stock.IBM, 1000));
            add(new Position(Stock.GOOG, 1000));
            add(new Position(Stock.AAPL, 1000));
            add(new Position(Stock.TSLA, 1000));
        }};

        Map<Stock, Double> expectedWeights = new HashMap<Stock,Double>() {{
            put(Stock.IBM, 0.25);
            put(Stock.GOOG, 0.25);
            put(Stock.AAPL, 0.15);
            put(Stock.TSLA, 0.15);
        }};

        List expectedTraderOrders = new ArrayList<TradeOrder>(){{
            add(new TradeOrder(Order.SELL, Stock.IBM, 55));
            add(new TradeOrder(Order.BUY, Stock.GOOG, 45));
            add(new TradeOrder(Order.SELL, Stock.AAPL, 430));
            add(new TradeOrder(Order.BUY, Stock.TSLA, 498));
        }};
       
        Assert.assertEquals(trader.rebalance(currentPositions, expectedWeights), expectedTraderOrders);
    }
    
}
