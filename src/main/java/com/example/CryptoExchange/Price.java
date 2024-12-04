package com.example.CryptoExchange;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "prices")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;

    @Column(nullable = false)
private String exchange;

    @Column(nullable = false)
private String symbol;

    @Column(nullable = false, precision = 18, scale = 8)
private double price;
 @Column(nullable = false)
    private LocalDateTime timestamp;


    public Long getId() {
        return id;
    }

    public void setId( long id){
        this.id= id;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange){
        this.exchange= exchange;

    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
