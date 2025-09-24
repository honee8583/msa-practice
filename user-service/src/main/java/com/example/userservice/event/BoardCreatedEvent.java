package com.example.userservice.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreatedEvent {

    private Long userId;

    public static BoardCreatedEvent fromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, BoardCreatedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json 파싱 에러");
        }
    }
}
