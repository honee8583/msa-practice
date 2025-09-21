package com.example.userservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddPointsRequestDto {
    private Long userId;
    private int amount;
}
