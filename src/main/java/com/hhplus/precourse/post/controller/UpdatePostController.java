package com.hhplus.precourse.post.controller;

import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.post.service.UpdatePostService;
import com.hhplus.precourse.post.vo.PostVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class UpdatePostController {
    private final UpdatePostService service;

    @PutMapping("/posts/{id}")
    ApiResponse<Response> update(@PathVariable Long id,
                                 @Valid @RequestBody Request request) {
        var command = request.toCommand(id);
        var postVo = service.update(command);

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
        public UpdatePostService.Command toCommand(Long id) {
            return new UpdatePostService.Command(
                id,
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
        public static UpdatePostController.Response from(PostVo postVo) {
            return new UpdatePostController.Response(
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
