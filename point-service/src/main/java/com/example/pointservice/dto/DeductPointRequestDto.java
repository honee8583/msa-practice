package com.example.pointservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeductPointRequestDto {
    private Long userId;
    private int amount;
}
