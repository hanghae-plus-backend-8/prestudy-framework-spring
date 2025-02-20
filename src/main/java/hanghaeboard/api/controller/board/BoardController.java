package hanghaeboard.api.controller.board;

import hanghaeboard.api.ApiResponse;
import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.service.board.BoardService;
import hanghaeboard.api.service.board.response.CreateBoardResponse;
import hanghaeboard.api.service.board.response.FindBoardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/v1/boards")
    public ApiResponse<List<FindBoardResponse>> getBoards(){
        return ApiResponse.ok(boardService.findAllBoard());
    }

    @PostMapping("/api/v1/boards")
    public ApiResponse<CreateBoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) throws Exception{
        log.info("createBoard request: {}", request);

        return ApiResponse.ok(boardService.createBoard(request));
    }

    @GetMapping("/api/v1/boards/{id}")
    public ApiResponse<FindBoardResponse> findBoardById(@PathVariable Long id) throws Exception{
        return ApiResponse.ok(boardService.findBoardById(id));
    }


}
