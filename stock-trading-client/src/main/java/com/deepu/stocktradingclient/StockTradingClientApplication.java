package com.deepu.stocktradingclient;

import com.deepu.stocktradingclient.service.StockClientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StockTradingClientApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(StockTradingClientApplication.class, args);
        StockClientService service = context.getBean(StockClientService.class);
//        System.out.println(service.getStockPrice("IBM")); //unary
//        service.subscribeStockPrice("IBM"); // server stream
//        service.bulkOrders(); // client stream
        service.liveTrading(); // bidirectional stream
    }

}
