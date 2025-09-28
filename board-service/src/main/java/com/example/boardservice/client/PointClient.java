package com.example.boardservice.client;

import com.example.boardservice.dto.AddPointsRequestDto;
import com.example.boardservice.dto.DeductPointsRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class PointClient {

    private final RestClient restClient; // WebClient, RestTemplate, FeignClient, ...

    public PointClient(@Value("${client.point-service.url}") String pointServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(pointServiceUrl)
                .build();
    }
    // 게시글 작성 -> 포인트(point-service) 차감 -> 활동점수(user-service) 적립
    public void deductPoints(Long userId, int amount) {
        DeductPointsRequestDto deductPointsRequestDto = DeductPointsRequestDto.builder()
                .userId(userId)
                .amount(amount)
                .build();

        restClient.post()
                .uri("/internal/points/deduct")
                .contentType(MediaType.APPLICATION_JSON)
                .body(deductPointsRequestDto)
                .retrieve()
                .toBodilessEntity();
    }

    public void addPoints(Long userId, int amount) {
        AddPointsRequestDto addPointsRequestDto = AddPointsRequestDto.builder()
                .userId(userId)
                .amount(amount)
                .build();

        restClient.post()
                .uri("/internal/points/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(addPointsRequestDto)
                .retrieve()
                .toBodilessEntity();
    }
}
