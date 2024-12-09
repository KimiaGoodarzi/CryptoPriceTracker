package com.example.CryptoExchange;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;

@Service
public class BinanceWebSocketService {

    private static final String BINANCE_WS_URL = "wss://stream.binance.com:9443/ws";
    private final BinanceWebSocketListener binanceWebSocketListener;
    private WebSocket webSocket;

    // Constructor injection for BinanceWebSocketListener
    public BinanceWebSocketService(BinanceWebSocketListener binanceWebSocketListener) {
        this.binanceWebSocketListener = binanceWebSocketListener;
    }

    @PostConstruct
    public void startWebSocket() {
        HttpClient client = HttpClient.newHttpClient();

        this.webSocket = client.newWebSocketBuilder()
                .buildAsync(URI.create(BINANCE_WS_URL), binanceWebSocketListener)
                .join();
    }
}
