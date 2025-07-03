package com.deepu.stocktradingserver.service;

import com.deepu.grpc.*;
import com.deepu.stocktradingserver.entity.Stock;
import com.deepu.stocktradingserver.repository.StockRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@GrpcService
@RequiredArgsConstructor
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {
    private final StockRepository stockRepository;

    // Unary
    @Override
    public void getStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {

        // StockName -> DB -> map response -> return
        String stockSymbol = request.getStockSymbol();
        Stock stock=stockRepository.findByStockSymbol(stockSymbol);
        StockResponse response =  StockResponse.newBuilder()
                .setStockSymbol(stock.getStockSymbol())
                .setPrice(stock.getPrice())
                .setTimestamp(stock.getLastUpdated().toString())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Server Streaming
    @Override
    public void subscribeStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {

        String stockSymbol = request.getStockSymbol();
        for(int i=0;i<10;i++){
            StockResponse response =  StockResponse.newBuilder()
                    .setStockSymbol(stockSymbol)
                    .setPrice(new Random().nextDouble(500))
                    .setTimestamp(Instant.now().toString())
                    .build();
            responseObserver.onNext(response);
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            } catch (InterruptedException e) {
                responseObserver.onError(e);
            }
        }
        responseObserver.onCompleted();
    }

    //Client Streaming
    @Override
    public StreamObserver<StockOrder> bulkStockOrder(StreamObserver<OrderSummary> responseObserver) {
        return new StreamObserver<>() {
            private int totalOrders=0;
            private int successCount=0;
            private int totalAmount=0;
            @Override
            public void onNext(StockOrder stockOrder) {
                totalOrders++;
                totalAmount += (int)(stockOrder.getQuantity() * stockOrder.getPrice());
                successCount++;
                System.out.println(stockOrder);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error :"+throwable);
            }

            @Override
            public void onCompleted() {
                OrderSummary response = OrderSummary.newBuilder()
                        .setTotalAmount(totalAmount)
                        .setTotalOrders(totalOrders)
                        .setSuccessCount(successCount)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    //Bidirectional Streaming
    @Override
    public StreamObserver<StockOrder> liveTrading(StreamObserver<TradeStatus> responseObserver) {
        return new StreamObserver<StockOrder>() {
            @Override
            public void onNext(StockOrder stockOrder) {
                System.out.println("Received Order : "+stockOrder);
                String message = "Order placed Successfully";
                String status = "EXECUTED";
                if(stockOrder.getQuantity()<=0){
                    message="Invalid Quantity";
                    status="FAILED";
                }
                TradeStatus tradeStatus = TradeStatus.newBuilder()
                        .setOrderId(stockOrder.getOrderId())
                        .setMessage(message)
                        .setStatus(status)
                        .setTimestamp(Instant.now().toString())
                        .build();
                responseObserver.onNext(tradeStatus);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                System.out.println("Server Stream Completed : "+ Instant.now().toEpochMilli());
                responseObserver.onCompleted();
            }
        };
    }
}
