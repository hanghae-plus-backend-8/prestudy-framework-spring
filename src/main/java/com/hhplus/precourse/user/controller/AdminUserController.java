package com.hhplus.precourse.user.controller;

import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.user.service.GetUserService;
import com.hhplus.precourse.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminUserController {
    private final GetUserService service;

    @GetMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<UserVo> get(@PathVariable Long id) {
        return ApiResponse.success(
            service.get(id)
        );
    }
}