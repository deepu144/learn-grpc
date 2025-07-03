package com.deepu.stocktradingserver.repository;

import com.deepu.stocktradingserver.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long> {

    Stock findByStockSymbol(String stockSymbol);
}
