package com.hhplus.precourse.post.controller;

import com.hhplus.precourse.common.web.ApiResponse;
import com.hhplus.precourse.post.service.CreatePostService;
import com.hhplus.precourse.post.service.DeletePostService;
import com.hhplus.precourse.post.vo.PostVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class DeletePostController {
    private final DeletePostService service;

    @DeleteMapping("/posts/{id}")
    ApiResponse<Void> delete(@PathVariable Long id,
                             @NotBlank(message = "패스워드는 필수 값 입니다.") String password) {
        service.delete(id, password);
        return ApiResponse.success();
    }
}
