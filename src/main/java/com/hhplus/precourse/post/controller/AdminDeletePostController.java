package com.hhplus.precourse.post.controller;

import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.post.service.AdminDeletePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminDeletePostController {
    private final AdminDeletePostService service;

    @DeleteMapping("/admin/posts/{id}")
    ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.success();
    }
} 