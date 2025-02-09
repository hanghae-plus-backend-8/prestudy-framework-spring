package prestudy.framework.spring.api.controller.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import prestudy.framework.spring.api.controller.board.request.BoardCreateRequest;
import prestudy.framework.spring.api.controller.board.request.BoardUpdateRequest;
import prestudy.framework.spring.api.controller.board.response.BoardResponse;
import prestudy.framework.spring.api.controller.common.response.ApiResponse;
import prestudy.framework.spring.api.service.board.BoardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/v1/boards")
    public ApiResponse<List<BoardResponse>> getBoards() {
        return ApiResponse.success(boardService.getBoards());
    }

    @PostMapping("/api/v1/boards")
    public ApiResponse<BoardResponse> createBoard(@Valid @RequestBody BoardCreateRequest request) {
        return ApiResponse.success(boardService.createBoard(request.toCommand()));
    }

    @GetMapping("/api/v1/boards/{id}")
    public ApiResponse<BoardResponse> getBoardById(@PathVariable("id") Long id) {
        return ApiResponse.success(boardService.getBoardById(id));
    }

    @PutMapping("/api/v1/boards/{id}")
    public ApiResponse<BoardResponse> updateBoard(@PathVariable("id") Long id,
                                                  @Valid @RequestBody BoardUpdateRequest request) {
        return ApiResponse.success(boardService.updateBoard(request.toCommand(id)));
    }
}
