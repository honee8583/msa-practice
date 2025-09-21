package com.example.boardservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeductPointsRequestDto {
    private Long userId;
    private int amount;
}
