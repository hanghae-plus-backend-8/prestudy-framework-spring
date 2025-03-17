package com.hhplus.precourse.post.controller;

import com.hhplus.precourse.auth.AuthenticatedUser;
import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.post.service.CreatePostService;
import com.hhplus.precourse.post.vo.PostVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class CreatePostController {
    private final CreatePostService service;

    @PostMapping("/posts")
    ApiResponse<Response> create(@Valid @RequestBody Request request,
                                 @AuthenticationPrincipal AuthenticatedUser user) {
        var postVo = service.create(request.toCommand(user.id()));

        return ApiResponse.success(
            Response.from(postVo)
        );
    }

    public record Request(
        @NotBlank(message = "작성자명은 필수 값입니다.")
        String author,
        @NotBlank(message = "제목은 필수 값입니다.")
        String title,
        @NotBlank(message = "내용은 필수 값입니다.")
        String content
    ) {
        public CreatePostService.Command toCommand(long userId) {
            return new CreatePostService.Command(
                userId,
                author,
                title,
                content
            );
        }
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
