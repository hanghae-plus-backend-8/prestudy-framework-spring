package hanghaeboard.api.controller.board;

import hanghaeboard.api.ApiResponse;
import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.service.board.BoardService;
import hanghaeboard.api.service.board.response.CreateBoardResponse;
import hanghaeboard.api.service.board.response.FindBoardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/v1/board/boards")
    public ApiResponse<List<FindBoardResponse>> getBoards(){
        return ApiResponse.ok(boardService.findAllBoard());
    }

    @PostMapping("/api/v1/board/create")
    public ApiResponse<CreateBoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) throws Exception{
        log.info("createBoard request: {}", request);

        return ApiResponse.ok(boardService.createBoard(request));
    }


}
