package com.hhplus.precourse.comment.controller;

import com.hhplus.precourse.comment.service.AdminUpdateCommentService;
import com.hhplus.precourse.comment.vo.CommentVo;
import com.hhplus.precourse.common.web.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminUpdateCommentController {
    private final AdminUpdateCommentService service;

    @PutMapping("/admin/comments/{id}")
    ApiResponse<CommentVo> update(@PathVariable Long id,
                                 @Valid @RequestBody Request request) {
        var command = request.toCommand(id);

        return ApiResponse.success(
            service.update(command)
        );
    }

    public record Request(
        @NotBlank(message = "댓글 내용은 필수 값입니다.")
        String content
    ) {
        public AdminUpdateCommentService.Command toCommand(long id) {
            return new AdminUpdateCommentService.Command(
                id,
                content
            );
        }
    }
} 