package com.deepu.stocktradingserver;

import com.deepu.stocktradingserver.entity.Stock;
import com.deepu.stocktradingserver.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class StockTradingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockTradingServerApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner loadData(StockRepository stockRepository) {
//        return args -> {
//            List<Stock> stocks = List.of(
//                    new Stock(null, "AAPL", 193.2, LocalDateTime.now()),
//                    new Stock(null, "GOOGL", 2850.4, LocalDateTime.now()),
//                    new Stock(null, "AMZN", 134.7, LocalDateTime.now()),
//                    new Stock(null, "TSLA", 780.1, LocalDateTime.now()),
//                    new Stock(null, "MSFT", 305.6, LocalDateTime.now()),
//                    new Stock(null, "NFLX", 410.8, LocalDateTime.now()),
//                    new Stock(null, "META", 312.3, LocalDateTime.now()),
//                    new Stock(null, "NVDA", 495.0, LocalDateTime.now()),
//                    new Stock(null, "IBM", 143.2, LocalDateTime.now()),
//                    new Stock(null, "ORCL", 121.9, LocalDateTime.now())
//            );
//            stockRepository.saveAll(stocks);
//        };
//    }

}
