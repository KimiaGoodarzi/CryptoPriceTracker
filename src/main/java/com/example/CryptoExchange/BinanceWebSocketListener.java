package com.example.CryptoExchange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.http.WebSocket;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Service
public class BinanceWebSocketListener implements WebSocket.Listener {
    private final PriceRepository priceRepository;

    public BinanceWebSocketListener(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public void onOpen(WebSocket websocket) {
        System.out.println("WebSocket connection established");

        String subscribeMessage = """
        {
            "method": "SUBSCRIBE",
            "params": ["btcusdt@ticker", "ethusdt@ticker"],
            "id": 1
        }
        """;

        websocket.sendText(subscribeMessage, true);
        WebSocket.Listener.super.onOpen(websocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        try {
            // Parse data
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(data.toString());

            String symbol = jsonNode.get("s").asText();
            Double price = jsonNode.get("c").asDouble();

            // Check if the symbol already exists in the database
            Optional<Price> existingPrice = priceRepository.findBySymbol(symbol);
            if (existingPrice.isPresent()) {
                Price priceRecord = existingPrice.get();
                double previousPrice = priceRecord.getPrice();
                double changePercentage = Math.abs((price - previousPrice) / previousPrice) * 100;

                if (changePercentage > 0.1) { // Save only if the change is greater than 0.1%
                    priceRecord.setPrice(price);
                    priceRecord.setTimestamp(LocalDateTime.now());
                    priceRepository.save(priceRecord);

                    System.out.printf("Updated %s: $%.2f (%.2f%% change)%n", symbol, price, changePercentage);
                }
            } else {
                // Insert a new record
                Price newPriceRecord = new Price();
                newPriceRecord.setExchange("Binance");
                newPriceRecord.setSymbol(symbol);
                newPriceRecord.setPrice(price);
                newPriceRecord.setTimestamp(LocalDateTime.now());
                priceRepository.save(newPriceRecord);

                System.out.printf("Saved new %s: $%.2f%n", symbol, price);
            }


        } catch (Exception e) {
            System.err.println("Error processing WebSocket message: " + e.getMessage());
        }
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }


    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.printf("WebSocket closed with status %d, reason: %s%n", statusCode, reason);
        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        System.err.println("WebSocket error: " + error.getMessage());
    }
}
