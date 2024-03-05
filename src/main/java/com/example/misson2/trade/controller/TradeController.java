package com.example.misson2.trade.controller;

import com.example.misson2.trade.service.TradeService;
import com.example.misson2.trade.dto.TradeDto;
import com.example.misson2.trade.dto.TradeReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("trade")
public class TradeController {

    private final TradeService tradeService;

    @GetMapping
    public List<TradeReadDto> readAll() {
        return tradeService.readAll();
    }

    @GetMapping("/{id}")
    public TradeReadDto readOne(
            @PathVariable("id")
            Long id
    ) {
        return tradeService.readOne(id);
    }

    @PostMapping
    public Long create(
            @RequestParam("title")
            String title,
            @RequestParam("description")
            String description,
            @RequestParam("price")
            Integer price,
            @RequestParam("file")
            MultipartFile file
    ) throws IOException {
        TradeDto dto = new TradeDto(title, description, price, file);
        return tradeService.create(dto);
    }

    @PutMapping("{id}")
    public TradeReadDto update(
            @PathVariable("id")
            Long id,
            @RequestParam("title")
            String title,
            @RequestParam("description")
            String description,
            @RequestParam("price")
            Integer price,
            @RequestParam("file")
            MultipartFile file
    ) throws IOException {
        TradeDto dto = new TradeDto(title, description, price, file);
        return tradeService.update(id, dto);
    }

    @DeleteMapping("{id}")
    public void delete(
            @PathVariable("id")
            Long id
    ) {
        tradeService.delete(id);
    }
}
