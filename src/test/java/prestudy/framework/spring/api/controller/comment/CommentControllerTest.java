package prestudy.framework.spring.api.controller.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import prestudy.framework.spring.api.controller.comment.request.CommentCreateRequest;
import prestudy.framework.spring.api.controller.comment.request.CommentUpdateRequest;
import prestudy.framework.spring.api.controller.comment.response.CommentResponse;
import prestudy.framework.spring.support.ControllerTestSupport;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends ControllerTestSupport {

    @DisplayName("댓글을 작성할 때 내용은 필수 값이다.")
    @Test
    void createCommentWithoutComment() throws Exception {
        // given
        CommentCreateRequest request = CommentCreateRequest.of("");

        // when & then
        mockMvc.perform(
                post("/api/v1/boards/{boardId}/comments", 1L)
                    .header("Authorization", "Bearer <Access Token>")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("내용은 필수 값 입니다."));
    }

    @DisplayName("댓글을 작성할 때 토큰이 유효해야 한다.")
    @Test
    void createCommentWithInvalidToken() throws Exception {
        // given
        CommentCreateRequest request = CommentCreateRequest.of("댓글 내용");

        when(commentService.createComment(any())).thenThrow(new IllegalStateException("토큰이 유효하지 않습니다."));

        // when & then
        mockMvc.perform(
                post("/api/v1/boards/{boardId}/comments", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다."));
    }

    @DisplayName("댓글을 작성할 때 게시글이 존재해야한다.")
    @Test
    void createCommentWithNotExistBoard() throws Exception {
        // given
        CommentCreateRequest request = CommentCreateRequest.of("댓글 내용");

        when(commentService.createComment(any())).thenThrow(new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // when & then
        mockMvc.perform(
                post("/api/v1/boards/{boardId}/comments", 1L)
                    .header("Authorization", "Bearer <Access Token>")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("게시글이 존재하지 않습니다."));
    }

    @DisplayName("댓글을 작성한다.")
    @Test
    void createComment() throws Exception {
        // given
        CommentCreateRequest request = CommentCreateRequest.of("댓글 내용");

        CommentResponse response = CommentResponse.builder()
            .id(1L)
            .content("댓글 내용")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        when(commentService.createComment(any())).thenReturn(response);

        // when & then
        mockMvc.perform(
                post("/api/v1/boards/{boardId}/comments", 1L)
                    .header("Authorization", "Bearer <Access Token>")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.content").value("댓글 내용"))
            .andExpect(jsonPath("$.data.createdDate").value("2025-02-07T12:00:00"));
    }

    @DisplayName("댓글을 수정할 때 내용은 필수 값이다.")
    @Test
    void updateCommentWithoutComment() throws Exception {
        // given
        CommentUpdateRequest request = CommentUpdateRequest.of("");

        // when & then
        mockMvc.perform(
                put("/api/v1/comments/{id}", 1L)
                    .header("Authorization", "Bearer <Access Token>")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("내용은 필수 값 입니다."));
    }

    @DisplayName("댓글을 수정할 때 토큰이 유효해야 한다.")
    @Test
    void updateCommentWithInvalidToken() throws Exception {
        // given
        CommentUpdateRequest request = CommentUpdateRequest.of("댓글 내용 수정");

        when(commentService.updateComment(any())).thenThrow(new IllegalStateException("토큰이 유효하지 않습니다."));

        // when & then
        mockMvc.perform(
                put("/api/v1/comments/{id}", 1L)
                    .header("Authorization", "Bearer <Access Token>")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다."));
    }

    @DisplayName("댓글을 수정할 때 권한이 있어야 한다.")
    @Test
    void updateCommentWithoutPermission() throws Exception {
        // given
        CommentUpdateRequest request = CommentUpdateRequest.of("댓글 내용 수정");

        when(commentService.updateComment(any())).thenThrow(new IllegalStateException("작성자만 삭제/수정할 수 있습니다."));

        // when & then
        mockMvc.perform(
                put("/api/v1/comments/{id}", 1L)
                    .header("Authorization", "Bearer <Access Token>")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("작성자만 삭제/수정할 수 있습니다."));
    }

    @DisplayName("댓글을 수정한다.")
    @Test
    void updateComment() throws Exception {
        // given
        CommentUpdateRequest request = CommentUpdateRequest.of("댓글 내용 수정");

        CommentResponse response = CommentResponse.builder()
            .id(1L)
            .content("댓글 내용 수정")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        when(commentService.updateComment(any())).thenReturn(response);

        // when & then
        mockMvc.perform(
                put("/api/v1/comments/{id}", 1L)
                    .header("Authorization", "Bearer <Access Token>")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.content").value("댓글 내용 수정"))
            .andExpect(jsonPath("$.data.createdDate").value("2025-02-07T12:00:00"));
    }

    @DisplayName("댓글을 수정할 때 토큰이 유효해야 한다.")
    @Test
    void deleteCommentWithInvalidToken() throws Exception {
        // given
        doThrow(new IllegalArgumentException("토큰이 유효하지 않습니다."))
            .when(commentService)
            .deleteComment(any());

        // when & then
        mockMvc.perform(
                delete("/api/v1/comments/{id}", 1L)
                    .header("Authorization", "Bearer <Access Token>")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다."));
    }

    @DisplayName("댓글을 수정할 때 권한이 있어야 한다.")
    @Test
    void deleteCommentWithoutPermission() throws Exception {
        // given
        doThrow(new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다."))
            .when(commentService)
            .deleteComment(any());

        // when & then
        mockMvc.perform(
                delete("/api/v1/comments/{id}", 1L)
                    .header("Authorization", "Bearer <Access Token>")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("작성자만 삭제/수정할 수 있습니다."));
    }

    @DisplayName("댓글을 삭제한다.")
    @Test
    void deleteComment() throws Exception {
        // when & then
        mockMvc.perform(
                delete("/api/v1/comments/{id}", 1L)
                    .header("Authorization", "Bearer <Access Token>")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("OK"));
    }
}