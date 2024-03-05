package com.example.misson2.trade.entity;

import com.example.misson2.auth.security.entity.UserEntity;
import com.example.misson2.file.UploadFile;
import com.example.misson2.trade.dto.TradeDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long id;

    @Setter
    private String title;
    @Setter
    private String description;
    @Setter
    private Integer price;

    @Setter
    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @Setter
    @Embedded
    private UploadFile file;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id")
    private UserEntity user;
}
