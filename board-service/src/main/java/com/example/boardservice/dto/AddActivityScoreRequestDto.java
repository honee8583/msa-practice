package com.example.boardservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddActivityScoreRequestDto {
    private Long userId;
    private int score;
}
