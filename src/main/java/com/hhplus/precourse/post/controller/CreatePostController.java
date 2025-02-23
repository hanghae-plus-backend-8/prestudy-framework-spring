package com.hhplus.precourse.post.controller;

import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.post.service.CreatePostService;
import com.hhplus.precourse.post.vo.PostVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class CreatePostController {
    private final CreatePostService service;

    @PostMapping("/posts")
    ApiResponse<Response> create(@Valid @RequestBody Request request) {
        var postVo = service.create(request.toCommand());

        return ApiResponse.success(
            Response.from(postVo)
        );
    }

    record Request(
        @NotBlank(message = "작성자명은 필수 값입니다.")
        String author,
        @NotBlank(message = "제목은 필수 값입니다.")
        String title,
        @NotBlank(message = "내용은 필수 값입니다.")
        String content,
        @NotBlank(message = "비밀번호는 필수 값입니다.")
        String password
    ) {
        public CreatePostService.Command toCommand() {
            return new CreatePostService.Command(
                author,
                title,
                content,
                password
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
