package com.hhplus.precourse.post.controller;

import com.hhplus.precourse.auth.AuthenticatedUser;
import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.post.service.DeletePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class DeletePostController {
    private final DeletePostService service;

    @DeleteMapping("/posts/{id}")
    ApiResponse<Void> delete(@PathVariable Long id,
                             @AuthenticationPrincipal AuthenticatedUser user) {
        service.delete(id, user.id());
        return ApiResponse.success();
    }
}
