package com.example.misson2.trade.service;

import com.example.misson2.file.FileStore;
import com.example.misson2.trade.dto.TradeDto;
import com.example.misson2.trade.dto.TradeReadDto;
import com.example.misson2.trade.entity.TradeStatus;
import com.example.misson2.trade.entity.Trade;
import com.example.misson2.file.UploadFile;
import com.example.misson2.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeService {

    private final TradeRepository tradeRepository;
    private final FileStore fileStore;

    public List<TradeReadDto> readAll() {
        List<Trade> tradeArticles = tradeRepository.findAll();
        return tradeArticles.stream()
                .map(TradeReadDto::fromEntity)
                .toList();
    }

    public TradeReadDto readOne(Long id) {
        Optional<Trade> optionalTradeArticle = tradeRepository.findById(id);
        if (optionalTradeArticle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return TradeReadDto.fromEntity(optionalTradeArticle.get());
    }

    @Transactional
    public Long create(TradeDto dto) throws IOException {

        UploadFile uploadFile = fileStore.storeFile(dto.getFile());

        Trade article = Trade.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .file(uploadFile)
                .status(TradeStatus.Sale)
                .build();

        return tradeRepository.save(article).getId();
    }

    @Transactional
    public TradeReadDto update(Long id, TradeDto dto) throws IOException {
        Optional<Trade> optionalTradeArticle = tradeRepository.findById(id);
        if (optionalTradeArticle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        UploadFile uploadFile = fileStore.storeFile(dto.getFile());
                Trade findTrade = optionalTradeArticle.get();

        findTrade.setTitle(dto.getTitle());
        findTrade.setDescription(dto.getDescription());
        findTrade.setPrice(dto.getPrice());
        findTrade.setFile(uploadFile);

        return TradeReadDto.fromEntity(findTrade);
    }

    @Transactional
    public void delete(Long id) {
        tradeRepository.deleteById(id);
    }
}
