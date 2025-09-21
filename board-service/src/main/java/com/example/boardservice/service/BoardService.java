package com.example.boardservice.service;

import com.example.boardservice.client.UserClient;
import com.example.boardservice.domain.Board;
import com.example.boardservice.dto.BoardResponseDto;
import com.example.boardservice.dto.CreateBoardRequestDto;
import com.example.boardservice.domain.BoardRepository;
import com.example.boardservice.dto.UserDto;
import com.example.boardservice.dto.UserResponseDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserClient userClient;

    @Transactional
    public void create(CreateBoardRequestDto createBoardRequestDto) {
        Board board = Board.builder()
                .title(createBoardRequestDto.getTitle())
                .content(createBoardRequestDto.getContent())
                .userId(createBoardRequestDto.getUserId())
                .build();
        boardRepository.save(board);
    }

    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다!"));

        Optional<UserResponseDto> optionalUserResponseDto = userClient.fetchUser(board.getUserId());

        UserDto userDto = null;
        if (optionalUserResponseDto.isPresent()) {
            UserResponseDto userResponseDto = optionalUserResponseDto.get();
            userDto = UserDto.builder()
                    .userId(userResponseDto.getUserId())
                    .name(userResponseDto.getName())
                    .build();
        }

        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .user(userDto)
                .build();
    }

    public List<BoardResponseDto> getBoards() {
        List<Board> boards = boardRepository.findAll();

        // userId 목록 추출
        List<Long> userIds = boards.stream()
                .map(board -> board.getUserId())
                .distinct()
                .toList();

        List<UserResponseDto> userResponseDtos = userClient.fetchUsersByIds(userIds);

        Map<Long, UserDto> userMap = new HashMap<>();
        for (UserResponseDto userResponseDto : userResponseDtos) {
            Long userId = userResponseDto.getUserId();
            String name = userResponseDto.getName();
            userMap.put(userId, new UserDto(userId, name));
        }

        return boards.stream()
                .map(board -> BoardResponseDto.builder()
                        .boardId(board.getBoardId())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .user(userMap.get(board.getUserId()))
                        .build())
                .toList();
    }
}
