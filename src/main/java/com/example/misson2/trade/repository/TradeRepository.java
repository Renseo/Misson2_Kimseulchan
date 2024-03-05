package com.example.misson2.trade.repository;

import com.example.misson2.trade.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
