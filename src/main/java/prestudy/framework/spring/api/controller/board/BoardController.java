package prestudy.framework.spring.api.controller.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import prestudy.framework.spring.api.authenticate.Authentication;
import prestudy.framework.spring.api.controller.board.request.BoardCreateRequest;
import prestudy.framework.spring.api.controller.board.request.BoardUpdateRequest;
import prestudy.framework.spring.api.controller.board.response.BoardResponse;
import prestudy.framework.spring.api.controller.common.response.ApiResponse;
import prestudy.framework.spring.api.service.board.BoardService;
import prestudy.framework.spring.api.service.board.command.BoardDeleteCommand;

import java.util.List;

@Tag(name = "게시물 API")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시물 목록 조회")
    @GetMapping("/api/v1/boards")
    public ApiResponse<List<BoardResponse>> getBoards() {
        return ApiResponse.success(boardService.getBoards());
    }

    @Operation(summary = "게시물 생성")
    @Authentication
    @PostMapping("/api/v1/boards")
    public ApiResponse<BoardResponse> createBoard(@Valid @RequestBody BoardCreateRequest request) {
        return ApiResponse.success(boardService.createBoard(request.toCommand()));
    }

    @Operation(summary = "게시물 상세 조회")
    @GetMapping("/api/v1/boards/{id}")
    public ApiResponse<BoardResponse> getBoardById(@PathVariable("id") Long id) {
        return ApiResponse.success(boardService.getBoardById(id));
    }

    @Operation(summary = "게시물 수정")
    @Authentication
    @PutMapping("/api/v1/boards/{id}")
    public ApiResponse<BoardResponse> updateBoard(@PathVariable("id") Long id,
                                                  @Valid @RequestBody BoardUpdateRequest request) {
        return ApiResponse.success(boardService.updateBoard(request.toCommand(id)));
    }

    @Operation(summary = "게시물 삭제")
    @Authentication
    @DeleteMapping("/api/v1/boards/{id}")
    public ApiResponse<Void> deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoard(BoardDeleteCommand.of(id));
        return ApiResponse.success();
    }
}
