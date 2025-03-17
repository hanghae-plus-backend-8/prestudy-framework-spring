package com.hhplus.precourse.comment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhplus.precourse.comment.service.CreateCommentService;
import com.hhplus.precourse.comment.vo.CommentVo;
import com.hhplus.precourse.common.web.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CreateCommentController {
    private final CreateCommentService service;

    @PostMapping
    ApiResponse<CommentVo> create(@Valid @RequestBody CreateCommentService.Command command) {
        return ApiResponse.success(service.create(command));
    }

    public record Request(
        @NotNull(message = "게시글 ID는 필수 값입니다.")
        Long postId,
        @NotNull(message = "사용자 ID는 필수 값입니다.")
        Long userId,
        @NotBlank(message = "댓글 내용은 필수 값입니다.")
        String content
    ) {
    }
}