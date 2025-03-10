package com.hhplus.precourse.integration;

import com.hhplus.precourse.comment.controller.AdminUpdateCommentController;
import com.hhplus.precourse.comment.domain.Comment;
import com.hhplus.precourse.comment.domain.CommentFixture;
import com.hhplus.precourse.comment.repository.CommentRepository;
import com.hhplus.precourse.common.IntegrationTest;
import com.hhplus.precourse.common.component.JwtTokenManager;
import com.hhplus.precourse.common.support.utils.JsonUtils;
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

public class AdminCommentIntegrationTest extends IntegrationTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenManager jwtTokenManager;
    private String jwtToken;
    private Comment savedComment;

    @BeforeEach
    void setUp() {
        var user = UserFixture.normalUser();
        var savedUser = userRepository.save(user);

        var admin = UserFixture.adminUser();
        var savedAdmin = userRepository.save(admin);
        this.jwtToken = jwtTokenManager.issue(savedAdmin.id(), savedAdmin.name());

        var post = new PostFixture().setUserId(savedUser.id()).build();
        var savedPost = postRepository.save(post);

        var comment = new CommentFixture()
            .setPostId(savedPost.id())
            .setUserId(savedUser.id())
            .build();
        this.savedComment = commentRepository.save(comment);
    }

    @DisplayName("댓글 수정")
    @Test
    void updateCommentByAdmin() throws Exception {
        var request = new AdminUpdateCommentController.Request(
            "수정된 댓글 내용"
        );

        mockMvc.perform(
                put("/admin/comments/{id}", savedComment.id())
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + jwtToken)
                    .content(JsonUtils.stringify(request))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.id").value(savedComment.id()))
            .andExpect(jsonPath("$.data.postId").value(savedComment.postId()))
            .andExpect(jsonPath("$.data.userId").value(savedComment.userId()))
            .andExpect(jsonPath("$.data.content").value(request.content()));
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteCommentByAdmin() throws Exception {
        mockMvc.perform(
                delete("/admin/comments/{id}", savedComment.id())
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + jwtToken)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").doesNotExist());
    }
} 