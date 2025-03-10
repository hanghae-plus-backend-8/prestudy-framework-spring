package com.hhplus.precourse.integration;

import com.hhplus.precourse.common.IntegrationTest;
import com.hhplus.precourse.common.component.JwtTokenManager;
import com.hhplus.precourse.common.support.utils.JsonUtils;
import com.hhplus.precourse.post.controller.AdminUpdatePostController;
import com.hhplus.precourse.post.domain.Post;
import com.hhplus.precourse.post.domain.PostFixture;
import com.hhplus.precourse.post.repository.PostRepository;
import com.hhplus.precourse.user.domain.User;
import com.hhplus.precourse.user.domain.UserFixture;
import com.hhplus.precourse.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminPostIntegrationTest extends IntegrationTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenManager jwtTokenManager;
    private String jwtToken;
    private User savedUser;
    private Post savedPost;

    @BeforeEach
    void setUp() {
        var user = UserFixture.normalUser();
        this.savedUser = userRepository.save(user);
        var admin = UserFixture.adminUser();
        var savedAdmin = userRepository.save(admin);
        var post = new PostFixture().setUserId(savedUser.id()).build();
        this.savedPost = postRepository.save(post);
        this.jwtToken = jwtTokenManager.issue(savedAdmin.id(), savedAdmin.name());
    }

    // 게시글 수정
    @DisplayName("게시글 수정")
    @Test
    void updatePostByAdmin() throws Exception {
        var request = new AdminUpdatePostController.Request(
            "수정된 작성자명",
            "수정된 제목",
            "수정된 내용"
        );

        mockMvc.perform(
                put("/admin/posts/{id}", savedPost.id())
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + jwtToken)
                    .content(JsonUtils.stringify(request))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.id").value(savedPost.id()))
            .andExpect(jsonPath("$.data.userId").value(savedPost.userId()))
            .andExpect(jsonPath("$.data.author").value(request.author()))
            .andExpect(jsonPath("$.data.title").value(request.title()))
            .andExpect(jsonPath("$.data.content").value(request.content()));
    }

    @DisplayName("게시글 삭제")
    @Test
    void deletePostByAdmin() throws Exception {
        mockMvc.perform(
                delete("/admin/posts/{id}", savedPost.id())
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + jwtToken)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").doesNotExist());
    }
}
