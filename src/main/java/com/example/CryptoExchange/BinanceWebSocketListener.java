package com.example.CryptoExchange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class BinanceWebSocketListener implements WebSocket.Listener{

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
    public CompletionStage<?> onText(WebSocket websocket, CharSequence data, boolean last) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(data.toString());

            String symbol = jsonNode.get("s").asText(); // Symbol (e.g., BTCUSDT)
            String price = jsonNode.get("c").asText(); // Current price

            System.out.printf("Symbol: %s, Price: %s%n", symbol, price);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
        return WebSocket.Listener.super.onText(websocket, data, last);
    }


    public void onError(WebSocket webSocket, Throwable error){

        System.out.println("WebSocket error: " + error.getMessage());
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.printf("WebSocket closed with status %d, reason: %s%n", statusCode, reason);
        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
    }

}
