package com.example.boardservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardRequestDto {

    private String title;

    private String content;

    private Long userId;

}
