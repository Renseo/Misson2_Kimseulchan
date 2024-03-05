package com.example.misson2.trade.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class TradeDto {

    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotNull
    private Integer price;

    private MultipartFile file;
}
