package com.deepu.stocktradingclient.service;

import com.deepu.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class StockClientService {
//    @GrpcClient("stockService")
//    private StockTradingServiceGrpc.StockTradingServiceBlockingStub serviceBlockingStub;

    // Unary
//    public StockResponse getStockPrice(String stockSymbol){
//        StockRequest request = StockRequest.newBuilder()
//                .setStockSymbol(stockSymbol)
//                .build();
//        return serviceBlockingStub.getStockPrice(request);
//    }

    @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceStub serviceStub;

    // Server Streaming
    public void subscribeStockPrice(String stockSymbol){
        StockRequest request = StockRequest.newBuilder()
                .setStockSymbol(stockSymbol)
                .build();
        serviceStub.subscribeStockPrice(request, new StreamObserver<StockResponse>() {
            @Override
            public void onNext(StockResponse stockResponse) {
                System.out.println("price : "+stockResponse.getPrice()+", time : "+stockResponse.getTimestamp());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("error :"+throwable);
            }

            @Override
            public void onCompleted() {
                System.out.println("..........Completed.......");
            }
        });
    }

    // Client Streaming
    public void bulkOrders(){
        StreamObserver<OrderSummary> responseObserver=new StreamObserver<OrderSummary>() {
            @Override
            public void onNext(OrderSummary orderSummary) {
                System.out.println(orderSummary.getTotalOrders());
                System.out.println(orderSummary.getSuccessCount());
                System.out.println(orderSummary.getTotalAmount());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error :"+throwable);
            }

            @Override
            public void onCompleted() {
                System.out.println("...........Completed..........");
            }
        };

        StreamObserver<StockOrder> requestObserver = serviceStub.bulkStockOrder(responseObserver);
        //send multiple stream of stock order request from client

        try {
            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("1")
                            .setStockSymbol("IBM")
                            .setOrderType("BUY")
                            .setPrice(150.0)
                            .setQuantity(2)
                            .build()
            );
            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("2")
                            .setStockSymbol("AAPL")
                            .setOrderType("SELL")
                            .setPrice(195.5)
                            .setQuantity(5)
                            .build()
            );

            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("3")
                            .setStockSymbol("GOOGL")
                            .setOrderType("BUY")
                            .setPrice(2849.0)
                            .setQuantity(3)
                            .build()
            );

            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("4")
                            .setStockSymbol("AMZN")
                            .setOrderType("SELL")
                            .setPrice(135.7)
                            .setQuantity(4)
                            .build()
            );

            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("5")
                            .setStockSymbol("TSLA")
                            .setOrderType("BUY")
                            .setPrice(781.2)
                            .setQuantity(1)
                            .build()
            );

            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("6")
                            .setStockSymbol("MSFT")
                            .setOrderType("SELL")
                            .setPrice(306.3)
                            .setQuantity(6)
                            .build()
            );

            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("7")
                            .setStockSymbol("NFLX")
                            .setOrderType("BUY")
                            .setPrice(411.1)
                            .setQuantity(2)
                            .build()
            );

            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("8")
                            .setStockSymbol("META")
                            .setOrderType("SELL")
                            .setPrice(313.4)
                            .setQuantity(3)
                            .build()
            );

            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("9")
                            .setStockSymbol("NVDA")
                            .setOrderType("BUY")
                            .setPrice(496.5)
                            .setQuantity(1)
                            .build()
            );

            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("10")
                            .setStockSymbol("ORCL")
                            .setOrderType("SELL")
                            .setPrice(122.0)
                            .setQuantity(7)
                            .build()
            );

            // complete All stream from client
            requestObserver.onCompleted();
        }catch (Exception e){
            System.out.println("Error Client :"+e);
        }
    }

    //Bidirectional Streaming
    public void liveTrading() throws InterruptedException {
        StreamObserver<TradeStatus> responseObserver = new StreamObserver<TradeStatus>() {
            @Override
            public void onNext(TradeStatus tradeStatus) {
                System.out.println("Server Response : "+tradeStatus);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Server Error :"+throwable);
            }

            @Override
            public void onCompleted() {
                System.out.println("Server Ended : "+Instant.now().toEpochMilli());
            }
        };
        StreamObserver<StockOrder> requestObserver = serviceStub.liveTrading(responseObserver);

        //sending multiple request to server
        for(int i=1;i<=10;i++){
            StockOrder order = StockOrder.newBuilder()
                    .setOrderId("ORD_" + i)
                    .setStockSymbol("APPLE")
                    .setOrderType("BUY")
                    .setPrice(100 * i)
                    .setQuantity(i * 2)
                    .build();
            requestObserver.onNext(order);
            Thread.sleep(500);
        }
        System.out.println("Client Stream Completed : "+ Instant.now().toEpochMilli());
        requestObserver.onCompleted();
    }

}
