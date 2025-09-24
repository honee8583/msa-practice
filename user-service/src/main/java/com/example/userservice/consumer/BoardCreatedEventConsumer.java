package com.example.userservice.consumer;

import com.example.userservice.dto.AddActivityScoreRequestDto;
import com.example.userservice.event.BoardCreatedEvent;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardCreatedEventConsumer {

    private final UserService userService;

    @KafkaListener(
            topics = "board.created",
            groupId = "user-service"
    )
    public void consume(String message) {
        BoardCreatedEvent boardCreatedEvent = BoardCreatedEvent.fromJson(message);

        // 게시글 작성시 활동 점수 추가
        AddActivityScoreRequestDto addActivityScoreRequestDto =
                new AddActivityScoreRequestDto(boardCreatedEvent.getUserId(), 10);

        userService.addActivityScore(addActivityScoreRequestDto);

        log.info("활동 점수 적립 완료");
    }
}
