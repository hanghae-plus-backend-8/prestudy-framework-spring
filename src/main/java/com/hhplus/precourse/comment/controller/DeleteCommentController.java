package com.hhplus.precourse.comment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hhplus.precourse.auth.AuthenticatedUser;
import com.hhplus.precourse.comment.service.DeleteCommentService;
import com.hhplus.precourse.common.web.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class DeleteCommentController {
    private final DeleteCommentService service;

    @DeleteMapping("/comments/{id}")
    ApiResponse<Void> delete(@PathVariable Long id,
                             @AuthenticationPrincipal AuthenticatedUser user) {
        service.delete(id, user.id());
        return ApiResponse.success();
    }
} 