package com.example.CryptoExchange;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;

@Service
public class BinanceWebSocketService {

    private static final String BINANCE_WS_URL = "wss://stream.binance.com:9443/ws";
    private WebSocket webSocket;

    @PostConstruct
    public void startWebSocket() {
        HttpClient client = HttpClient.newHttpClient();

        this.webSocket = client.newWebSocketBuilder()
                .buildAsync(URI.create(BINANCE_WS_URL), new BinanceWebSocketListener())
                .join();

    }
}
