package com.hhplus.precourse.post.controller;

import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.post.service.AdminUpdatePostService;
import com.hhplus.precourse.post.vo.PostVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class AdminUpdatePostController {
    private final AdminUpdatePostService service;

    @PutMapping("/admin/posts/{id}")
    ApiResponse<Response> update(@PathVariable Long id,
                               @Valid @RequestBody Request request) {
        var command = request.toCommand(id);
        var postVo = service.update(command);

        return ApiResponse.success(Response.from(postVo));
    }

    public record Request(
        @NotBlank(message = "작성자명은 필수 값입니다.")
        String author,
        @NotBlank(message = "제목은 필수 값입니다.")
        String title,
        @NotBlank(message = "내용은 필수 값입니다.")
        String content
    ) {
        public AdminUpdatePostService.Command toCommand(long id) {
            return new AdminUpdatePostService.Command(
                id,
                author,
                title,
                content
            );
        }
    }

    record Response(
        long id,
        long userId,
        String title,
        String author,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        public static Response from(PostVo postVo) {
            return new Response(
                postVo.id(),
                postVo.userId(),
                postVo.title(),
                postVo.author(),
                postVo.content(),
                postVo.createdAt(),
                postVo.updatedAt()
            );
        }
    }
} 