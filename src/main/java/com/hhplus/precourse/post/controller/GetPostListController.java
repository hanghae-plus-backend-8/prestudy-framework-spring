package com.hhplus.precourse.post.controller;

import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.post.service.GetPostListService;
import com.hhplus.precourse.post.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetPostListController {
    private final GetPostListService service;

    @GetMapping("/posts")
    ApiResponse<List<Response>> get() {
        return ApiResponse.success(
            service.get()
                .stream()
                .map(Response::from)
                .toList()
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
