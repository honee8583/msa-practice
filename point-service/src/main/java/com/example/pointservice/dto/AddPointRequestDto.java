package com.example.pointservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddPointRequestDto {
    private Long userId;
    private int amount;
}
