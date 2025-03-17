package com.hhplus.precourse.comment.controller;

import com.hhplus.precourse.auth.AuthenticatedUser;
import com.hhplus.precourse.comment.service.UpdateCommentService;
import com.hhplus.precourse.comment.vo.CommentVo;
import com.hhplus.precourse.common.web.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateCommentController {
    private final UpdateCommentService service;

    @PutMapping("/comments/{id}")
    ApiResponse<CommentVo> update(@PathVariable Long id,
                                 @Valid @RequestBody Request request,
                                 @AuthenticationPrincipal AuthenticatedUser user) {
        var command = request.toCommand(id, user.id());

        return ApiResponse.success(
            service.update(command)
        );
    }

    public record Request(
        @NotBlank(message = "댓글 내용은 필수 값입니다.")
        String content
    ) {
        public UpdateCommentService.Command toCommand(long id, long userId) {
            return new UpdateCommentService.Command(
                id,
                userId,
                content
            );
        }
    }
} 