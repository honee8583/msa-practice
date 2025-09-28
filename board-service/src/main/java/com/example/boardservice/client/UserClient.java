package com.example.boardservice.client;

import com.example.boardservice.dto.AddActivityScoreRequestDto;
import com.example.boardservice.dto.UserResponseDto;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
public class UserClient {

    private final RestClient restClient; // WebClient, RestTemplate, FeignClient, ...

    public UserClient(@Value("${client.user-service.url}") String userServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

    public Optional<UserResponseDto> fetchUser(Long userId) {
        try {
            UserResponseDto userResponseDto = this.restClient.get()
                    .uri("/internal/users/{userId}", userId)
                    .retrieve()
                    .body(UserResponseDto.class);
            return Optional.ofNullable(userResponseDto);
        } catch (RestClientException e) {
             log.error("사용자 정보 조회 실패");
            return Optional.empty();
        }
    }

    public List<UserResponseDto> fetchUsersByIds(List<Long> ids) {
        try {
            return this.restClient.get()
                    .uri(uriBuilder -> uriBuilder  // 파라미터로 전송하기 위해
                            .path("/internal/users")
                            .queryParam("ids", ids)
                            .build()
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("전체 사용자 조회 실패");
            return Collections.emptyList(); // 실패하면 빈 리스트 전달
        }
    }

    public void addActivityScore(Long userId, int score) {
        AddActivityScoreRequestDto addActivityScoreRequestDto = AddActivityScoreRequestDto.builder()
                .userId(userId)
                .score(score)
                .build();

        restClient.post()
                .uri("/internal/users/activity-score/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(addActivityScoreRequestDto)
                .retrieve()
                .toBodilessEntity();
    }
}
