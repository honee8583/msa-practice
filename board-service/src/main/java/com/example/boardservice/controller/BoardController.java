package com.example.boardservice.controller;

import com.example.boardservice.dto.CreateBoardRequestDto;
import com.example.boardservice.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateBoardRequestDto createBoardRequestDto) {
        boardService.create(createBoardRequestDto);
        return ResponseEntity.noContent().build();
    }
}
