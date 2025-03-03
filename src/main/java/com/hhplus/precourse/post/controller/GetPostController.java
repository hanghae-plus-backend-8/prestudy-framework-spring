package com.hhplus.precourse.post.controller;

import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.post.service.GetPostService;
import com.hhplus.precourse.post.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class GetPostController {
    private final GetPostService service;

    @GetMapping("/posts/{id}")
    ApiResponse<Response> get(@PathVariable Long id) {
        return ApiResponse.success(
            Response.from(service.get(id))
        );
    }

    record Response(
        long id,
        String title,
        String author,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        public static Response from(PostVo postVo) {
            return new Response(
                postVo.id(),
                postVo.title(),
                postVo.author(),
                postVo.content(),
                postVo.createdAt(),
                postVo.updatedAt()
            );
        }
    }
}
