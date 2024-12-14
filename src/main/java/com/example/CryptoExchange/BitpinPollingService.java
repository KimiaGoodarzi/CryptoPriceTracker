package com.example.CryptoExchange;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class BitpinPollingService {
    private static final String BITPIN_API_URL = "https://api.bitpin.ir/api/v1/mth/orderbook/{symbol}/";
    private final PriceRepository priceRepository;

    public BitpinPollingService(PriceRepository priceRepository){
        this.priceRepository= priceRepository;
    }

    public void fetchBitpinPrices(){
        fetchAndSavePrice("BTC_USDT");
        fetchAndSavePrice("ETH_USDT");
    }

    // for example symbol could be BTC_USDT or ethereum
    private void fetchAndSavePrice(String symbol){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = BITPIN_API_URL.replace("symbol", symbol);


            System.out.println("Raw Response: " + restTemplate.getForObject(url, BitpinResponse.class));

            BitpinResponse response = restTemplate.getForObject(url, BitpinResponse.class);
            if(response != null){

                Double buyPrice = response.getBids(0).getPrice();
                Double sellPrice = response.getAsks(0).getPrice();

                Price priceRecord = priceRepository.findBySymbol(symbol).orElse(new Price());
                priceRecord.setExchange("Bitpin");
                priceRecord.setSymbol(symbol.replace("_"," "));
                //Do we get the average for bitpin price in the dataset?
                priceRecord.setPrice((buyPrice + sellPrice) / 2 );
                priceRecord.setTimestamp(LocalDateTime.now());

                priceRepository.save(priceRecord);

                System.out.printf("Saved Bitpin %s: Buy $%.2f | Sell $%.2f%n", symbol, buyPrice, sellPrice);



            }
        }
        catch(Exception e){
            System.err.println("Error fetching data from Bitpin for " + symbol + ": " + e.getMessage());
        }
    }

}
