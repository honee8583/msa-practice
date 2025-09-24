package com.example.boardservice.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserSignedUpEvent {
    private Long userId;
    private String name;

    public static UserSignedUpEvent fromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, UserSignedUpEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json 파싱 에러");
        }
    }
}
