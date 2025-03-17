package com.hhplus.precourse.post.controller;

import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.post.service.GetPostListService;
import com.hhplus.precourse.post.vo.PostSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetPostListController {
    private final GetPostListService service;

    @GetMapping("/posts")
    ApiResponse<List<PostSummary>> get() {
        return ApiResponse.success(
            service.get()
        );
    }
}
