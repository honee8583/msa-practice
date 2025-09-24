package com.example.boardservice.controller;

import com.example.boardservice.dto.BoardResponseDto;
import com.example.boardservice.dto.CreateBoardRequestDto;
import com.example.boardservice.service.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable("boardId") Long boardId) {
//        BoardResponseDto boardResponseDto = boardService.getBoard(boardId);
        BoardResponseDto boardResponseDto = boardService.getBoard2(boardId);
        return ResponseEntity.ok(boardResponseDto);
    }

    @GetMapping
    public ResponseEntity<?> getBoards() {
//        List<BoardResponseDto> boards = boardService.getBoards();
        List<BoardResponseDto> boards = boardService.getBoards2();
        return ResponseEntity.ok(boards);
    }
}
