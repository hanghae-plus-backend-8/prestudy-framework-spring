package prestudy.framework.spring.api.controller.comment;

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

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Authentication
    @PostMapping("/api/v1/boards/{boardId}/comments")
    public ApiResponse<CommentResponse> createComment(@PathVariable("boardId") Long boardId,
                                                      @Valid @RequestBody CommentCreateRequest request) {
        return ApiResponse.success(commentService.createComment(request.toCommand(boardId)));
    }

    @Authentication
    @PutMapping("/api/v1/comments/{id}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable("id") Long id,
                                                      @Valid @RequestBody CommentUpdateRequest request) {
        return ApiResponse.success(commentService.updateComment(request.toCommand(id)));
    }

    @Authentication
    @DeleteMapping("/api/v1/comments/{id}")
    public ApiResponse<Void> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(CommentDeleteCommand.of(id));
        return ApiResponse.success();
    }
}
