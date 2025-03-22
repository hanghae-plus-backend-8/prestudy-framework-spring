package com.hhplus.precourse.post.controller;

import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.post.service.GetPostService;
import com.hhplus.precourse.post.vo.PostDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetPostController {
    private final GetPostService service;

    @GetMapping("/posts/{id}")
    ApiResponse<PostDetails> get(@PathVariable Long id) {
        return ApiResponse.success(
            service.get(id)
        );
    }
}
