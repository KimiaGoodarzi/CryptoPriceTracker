package com.example.CryptoExchange;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class BitpinPollingService {

    private static final String BITPIN_API_URL = "https://api.bitpin.ir/api/v1/mth/orderbook/{symbol}/";
    private final PriceRepository priceRepository;

    public BitpinPollingService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    // Scheduled method with no arguments
    @Scheduled(fixedRate = 3000)
    public void fetchBitpinPrices() {
        fetchAndSavePrice("BTC_USDT");
        fetchAndSavePrice("ETH_USDT");
    }


    private void fetchAndSavePrice(String symbol) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = BITPIN_API_URL.replace("{symbol}", symbol);

            // Fetch data from the Bitpin API
            BitpinResponse response = restTemplate.getForObject(url, BitpinResponse.class);
            System.out.println("Raw Response: " + response);

            if (response != null && !response.getBids().isEmpty() && !response.getAsks().isEmpty()) {
                // Parse first bid and ask prices
                Double buyPrice = Double.parseDouble(response.getBids().get(0).get(0));
                Double sellPrice = Double.parseDouble(response.getAsks().get(0).get(0));

                // Fetch existing record or create a new one
                Price priceRecord = priceRepository.findBySymbol(symbol)
                        .orElse(new Price()); // Fetch or create a new Price entity

                // Update record fields
                priceRecord.setExchange("Bitpin");
                priceRecord.setSymbol(symbol.replace("_", " ")); // Normalize symbol
                priceRecord.setPrice((buyPrice + sellPrice) / 2); // Average price
                priceRecord.setTimestamp(LocalDateTime.now());

                // Save updated or new record
                priceRepository.save(priceRecord);
                System.out.printf("Saved Bitpin %s: Buy $%.2f | Sell $%.2f%n", symbol, buyPrice, sellPrice);
            } else {
                System.err.println("Received empty bids/asks from Bitpin API for " + symbol);
            }
        } catch (Exception e) {
            System.err.println("Error fetching data from Bitpin for " + symbol + ": " + e.getMessage());
        }
    }



}
