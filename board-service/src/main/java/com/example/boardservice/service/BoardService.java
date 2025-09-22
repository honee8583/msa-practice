package com.example.boardservice.service;

import com.example.boardservice.client.PointClient;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserClient userClient;
    private final PointClient pointClient;

//    @Transactional
    public void create(CreateBoardRequestDto createBoardRequestDto) {
        // 게시글 저장을 성공했는지 판단하는 플래그
        boolean isBoardCreated = false;
        Long savedBoardId = null;

        // 포인트 차감을 성공했는지 판단하는 플래그
        boolean isPointDeducted = false;
        try {
            // 포인트 차감
            pointClient.deductPoints(createBoardRequestDto.getUserId(), 100);
            isPointDeducted = true; // 포인트 차감 성공
            log.info("포인트 차감 성공");

            // 게시글 작성
            Board board = Board.builder()
                    .title(createBoardRequestDto.getTitle())
                    .content(createBoardRequestDto.getContent())
                    .userId(createBoardRequestDto.getUserId())
                    .build();
            Board savedBoard = boardRepository.save(board);
            savedBoardId = savedBoard.getBoardId();
            isBoardCreated = true;  // 게시글 저장 성공 플래그
            log.info("게시글 저장 성공");

            // 작성자 활동 점수 적립
            // 마지막 처리이므로 보상 트랜잭션을 적용시킬 필요가 없다
            userClient.addActivityScore(savedBoard.getUserId(), 10);
            log.info("포인트 적립 성공");
        } catch(Exception e) {
            if (isBoardCreated) {
                // 게시글 작성 보상 트랜잭션 => 게시글 삭제
                boardRepository.deleteById(savedBoardId);
                log.info("[보상 트랜잭션] 게시글 삭제");
            }

            if (isPointDeducted) {
                // 포인트 차감 보상 트랜잭션 => 포인트 적립
                pointClient.addPoints(createBoardRequestDto.getUserId(), 100);
                log.info("[보상 트랜잭션] 포인트 적립");
            }

            // 실패 응답으로 처리하기 위해 예외 던지기
            throw e;
        }
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
