package com.example.final_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name="penaltyCounts")
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyCountEntity {
    // 사용자 식별 번호
    @Id
    private int userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    private UserEntity user;

    // 패널티 횟수 기록
    private int penaltyCount;

    // 패널티 횟수 업데이트 일자
    private LocalDate lastUpdate;
}
