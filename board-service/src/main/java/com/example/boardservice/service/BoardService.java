package com.example.boardservice.service;

import com.example.boardservice.domain.Board;
import com.example.boardservice.dto.CreateBoardRequestDto;
import com.example.boardservice.domain.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public void create(CreateBoardRequestDto createBoardRequestDto) {
        Board board = Board.builder()
                .title(createBoardRequestDto.getTitle())
                .content(createBoardRequestDto.getContent())
                .userId(createBoardRequestDto.getUserId())
                .build();
        boardRepository.save(board);
    }
}
