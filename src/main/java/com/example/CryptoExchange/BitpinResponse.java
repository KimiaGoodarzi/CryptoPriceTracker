package com.example.CryptoExchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BitpinResponse {
    private List<List<String>> asks; // Sell orders
    private List<List<String>> bids; // Buy orders
    public List<List<String>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<String>> asks) {
        this.asks = asks;
    }

    public List<List<String>> getBids() {
        return bids;
    }

    public void setBids(List<List<String>> bids) {
        this.bids = bids;
    }

    public PriceData getAsks(int index) {
        return new PriceData(Double.parseDouble(asks.get(index).get(0)));
    }

    public PriceData getBids(int index) {
        return new PriceData(Double.parseDouble(bids.get(index).get(0)));
    }

    public static class PriceData {
        private final Double price;

        public PriceData(Double price) {
            this.price = price;
        }

        public Double getPrice() {
            return price;
        }
    }
}
