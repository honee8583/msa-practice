package com.example.boardservice.event;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreatedEvent {
    private Long userId;

}
