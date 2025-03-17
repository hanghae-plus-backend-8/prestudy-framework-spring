package com.hhplus.precourse.comment.controller;

import com.hhplus.precourse.comment.service.AdminDeleteCommentService;
import com.hhplus.precourse.common.web.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminDeleteCommentController {
    private final AdminDeleteCommentService service;

    @DeleteMapping("/admin/comments/{id}")
    ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.success();
    }
} 