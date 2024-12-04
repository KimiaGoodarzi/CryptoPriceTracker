package com.example.CryptoExchange.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Allow React frontend
public class CryptoController {

    @GetMapping("/prices")
    public List<Map<String, Object>> getPrices() {
        List<Map<String, Object>> prices = new ArrayList<>();
        prices.add(Map.of("name", "Bitcoin", "price", 57382));
        prices.add(Map.of("name", "Ethereum", "price", 1845));
        return prices;
    }
}
