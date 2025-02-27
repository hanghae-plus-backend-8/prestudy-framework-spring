package com.hhplus.precourse.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hhplus.precourse.common.IntegrationTest;
import com.hhplus.precourse.common.support.utils.JsonUtils;
import com.hhplus.precourse.post.controller.CreatePostController;
import com.hhplus.precourse.post.controller.UpdatePostController;
import com.hhplus.precourse.post.domain.PostFixture;
import com.hhplus.precourse.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostIntegrationTest extends IntegrationTest {
    @Autowired
    private PostRepository postRepository;

    @DisplayName("게시글 목록 조회")
    @Test
    void getPostList() throws Exception {
        var saved = postRepository.save(new PostFixture().build());

        mockMvc.perform(get("/posts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].id").value(saved.id()))
            .andExpect(jsonPath("$.data[0].title").value(saved.title()))
            .andExpect(jsonPath("$.data[0].author").value(saved.author()))
            .andExpect(jsonPath("$.data[0].content").value(saved.content()))
            .andExpect(jsonPath("$.data[0].createdAt").exists())
            .andExpect(jsonPath("$.data[0].updatedAt").exists());
    }

    @DisplayName("게시글 상세 조회")
    @Test
    void getPost() throws Exception {
        var saved = postRepository.save(new PostFixture().build());

        mockMvc.perform(get("/posts/{id}", saved.id()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.id").value(saved.id()))
            .andExpect(jsonPath("$.data.title").value(saved.title()))
            .andExpect(jsonPath("$.data.author").value(saved.author()))
            .andExpect(jsonPath("$.data.content").value(saved.content()))
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andExpect(jsonPath("$.data.updatedAt").exists());
    }

    @DisplayName("게시글 생성")
    @Test
    void createPost() throws Exception {
        var request = new CreatePostController.Request(
            "testAuthor",
            "testTitle",
            "testContent",
            "testPassword"
        );

        mockMvc.perform(
                post("/posts")
                    .contentType("application/json")
                    .content(JsonUtils.stringify(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.id").isNumber())
            .andExpect(jsonPath("$.data.title").value(request.title()))
            .andExpect(jsonPath("$.data.author").value(request.author()))
            .andExpect(jsonPath("$.data.content").value(request.content()))
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andExpect(jsonPath("$.data.updatedAt").exists());
    }

    @DisplayName("게시글 수정")
    @Test
    void updatePost() throws Exception {
        var saved = postRepository.save(new PostFixture().build());
        var request = new UpdatePostController.Request(
            "changedAuthor",
            "changedTitle",
            "changedContent",
            saved.password()
        );

        mockMvc.perform(
                put("/posts/{id}", saved.id())
                    .contentType("application/json")
                    .content(JsonUtils.stringify(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.id").isNumber())
            .andExpect(jsonPath("$.data.title").value(request.title()))
            .andExpect(jsonPath("$.data.author").value(request.author()))
            .andExpect(jsonPath("$.data.content").value(request.content()))
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andExpect(jsonPath("$.data.updatedAt").exists());
    }

    @DisplayName("게시글 삭제")
    @Test
    void deletePost() throws Exception {
        var saved = postRepository.save(new PostFixture().build());

        mockMvc.perform(
            delete("/posts/{id}", saved.id())
                .queryParam("password", saved.password())
            )
            .andExpect(status().isOk());
    }
}
