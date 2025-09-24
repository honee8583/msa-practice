package com.example.boardservice.consumer;

import com.example.boardservice.dto.SaveUserRequestDto;
import com.example.boardservice.event.UserSignedUpEvent;
import com.example.boardservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSignedUpEventConsumer {

    private final UserService userService;

    @KafkaListener(
            topics = "user.signed-up",
            groupId = "board-service"
    )
    public void consume(String message) {
        UserSignedUpEvent userSignedUpEvent = UserSignedUpEvent.fromJson(message);

        SaveUserRequestDto saveUserRequestDto = SaveUserRequestDto.builder()
                .userId(userSignedUpEvent.getUserId())
                .name(userSignedUpEvent.getName())
                .build();
        userService.save(saveUserRequestDto);
    }

}
