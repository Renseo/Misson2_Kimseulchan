package com.example.misson2.trade.dto;

import com.example.misson2.trade.entity.Trade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TradeReadDto {

    private String title;
    private String description;
    private Integer price;

    private String status;

    private String storeFileName;
    private String uploadFileName;

    public static TradeReadDto fromEntity(Trade entity) {
        return new TradeReadDto(
                entity.getTitle(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStatus().toString(),
                entity.getFile().getStoreFileName(),
                entity.getFile().getUploadFileName()
        );
    }
}
