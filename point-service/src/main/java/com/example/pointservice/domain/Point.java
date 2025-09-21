package com.example.pointservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    private Long userId;

    private int amount;

    public Point(Long userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }

    // 포인트 적립
    public void addAmount(int amount) {
        this.amount += amount;
    }

    // 포인트 차감
    public void deductAmount(int amount) {
        this.amount -= amount;
    }

}
