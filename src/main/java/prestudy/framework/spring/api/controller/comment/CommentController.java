package prestudy.framework.spring.api.controller.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import prestudy.framework.spring.api.authenticate.Authentication;
import prestudy.framework.spring.api.controller.comment.request.CommentCreateRequest;
import prestudy.framework.spring.api.controller.comment.request.CommentUpdateRequest;
import prestudy.framework.spring.api.controller.comment.response.CommentResponse;
import prestudy.framework.spring.api.controller.common.response.ApiResponse;
import prestudy.framework.spring.api.service.comment.CommentService;
import prestudy.framework.spring.api.service.comment.command.CommentDeleteCommand;

@Tag(name = "댓글 API")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "게시물 댓글 생성")
    @Authentication
    @PostMapping("/api/v1/boards/{boardId}/comments")
    public ApiResponse<CommentResponse> createComment(@PathVariable("boardId") Long boardId,
                                                      @Valid @RequestBody CommentCreateRequest request) {
        return ApiResponse.success(commentService.createComment(request.toCommand(boardId)));
    }

    @Operation(summary = "댓글 수정")
    @Authentication
    @PutMapping("/api/v1/comments/{id}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable("id") Long id,
                                                      @Valid @RequestBody CommentUpdateRequest request) {
        return ApiResponse.success(commentService.updateComment(request.toCommand(id)));
    }

    @Operation(summary = "댓글 삭제")
    @Authentication
    @DeleteMapping("/api/v1/comments/{id}")
    public ApiResponse<Void> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(CommentDeleteCommand.of(id));
        return ApiResponse.success();
    }
}
