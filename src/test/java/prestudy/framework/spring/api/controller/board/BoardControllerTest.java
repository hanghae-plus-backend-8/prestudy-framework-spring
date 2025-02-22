package prestudy.framework.spring.api.controller.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import prestudy.framework.spring.api.controller.board.request.BoardCreateRequest;
import prestudy.framework.spring.api.controller.board.request.BoardDeleteRequest;
import prestudy.framework.spring.api.controller.board.request.BoardUpdateRequest;
import prestudy.framework.spring.api.controller.board.response.BoardResponse;
import prestudy.framework.spring.api.service.board.command.BoardDeleteCommand;
import prestudy.framework.spring.api.service.board.command.BoardUpdateCommand;
import prestudy.framework.spring.support.ControllerTestSupport;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest extends ControllerTestSupport {

    @DisplayName("게시글 목록을 조회한다.")
    @Test
    void getBoards() throws Exception {
        //given
        BoardResponse response = BoardResponse.builder()
            .id(1L)
            .title("제목")
            .content("내용")
            .writer("작성자")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        when(boardService.getBoards()).thenReturn(List.of(response));

        // when & then
        mockMvc.perform(
                get("/api/v1/boards")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[*].id").value(1))
            .andExpect(jsonPath("$.data[*].title").value("제목"))
            .andExpect(jsonPath("$.data[*].content").value("내용"))
            .andExpect(jsonPath("$.data[*].writer").value("작성자"))
            .andExpect(jsonPath("$.data[*].createdDate").value("2025-02-07T12:00:00"));
    }

    @DisplayName("게시글을 작성한다.")
    @Test
    void createBoard() throws Exception {
        // given
        BoardCreateRequest request = BoardCreateRequest.builder()
            .title("제목")
            .content("내용")
            .writer("작성자")
            .password("1234")
            .build();

        BoardResponse response = BoardResponse.builder()
            .id(1L)
            .title("제목")
            .content("내용")
            .writer("작성자")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        when(boardService.createBoard(any())).thenReturn(response);

        // when & then
        mockMvc.perform(
                post("/api/v1/boards")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.title").value("제목"))
            .andExpect(jsonPath("$.data.content").value("내용"))
            .andExpect(jsonPath("$.data.writer").value("작성자"))
            .andExpect(jsonPath("$.data.createdDate").value("2025-02-07T12:00:00"));
    }

    @DisplayName("게시글을 작성할 때 제목은 필수 값이다.")
    @Test
    void createBoardWithoutTitle() throws Exception {
        // given
        BoardCreateRequest request = BoardCreateRequest.builder()
            .content("내용")
            .writer("작성자")
            .password("1234")
            .build();

        // when & then
        mockMvc.perform(
                post("/api/v1/boards")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("제목은 필수 값 입니다."));
    }

    @DisplayName("게시글을 작성할 때 내용은 필수 값이다.")
    @Test
    void createBoardWithoutContent() throws Exception {
        // given
        BoardCreateRequest request = BoardCreateRequest.builder()
            .title("제목")
            .writer("작성자")
            .password("1234")
            .build();

        // when & then
        mockMvc.perform(
                post("/api/v1/boards")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("내용은 필수 값 입니다."));
    }

    @DisplayName("게시글을 작성할 때 작성자는 필수 값이다.")
    @Test
    void createBoardWithoutWriter() throws Exception {
        // given
        BoardCreateRequest request = BoardCreateRequest.builder()
            .title("제목")
            .content("내용")
            .password("1234")
            .build();

        // when & then
        mockMvc.perform(
                post("/api/v1/boards")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("작성자는 필수 값 입니다."));
    }

    @DisplayName("게시글을 작성할 때 비밀번호는 필수 값이다.")
    @Test
    void createBoardWithoutPassword() throws Exception {
        // given
        BoardCreateRequest request = BoardCreateRequest.builder()
            .title("제목")
            .content("내용")
            .writer("작성자")
            .build();

        // when & then
        mockMvc.perform(
                post("/api/v1/boards")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("비밀번호는 필수 값 입니다."));
    }

    @DisplayName("게시글 ID로 게시글을 상세 조회한다.")
    @Test
    void getBoardById() throws Exception {
        // given
        BoardResponse response = BoardResponse.builder()
            .id(1L)
            .title("제목")
            .content("내용")
            .writer("작성자")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        when(boardService.getBoardById(anyLong())).thenReturn(response);

        // when & then
        mockMvc.perform(
                get("/api/v1/boards/{id}", 1L)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.title").value("제목"))
            .andExpect(jsonPath("$.data.content").value("내용"))
            .andExpect(jsonPath("$.data.writer").value("작성자"))
            .andExpect(jsonPath("$.data.createdDate").value("2025-02-07T12:00:00"));
    }

    @DisplayName("게시글 상세 조회 시 ID는 유효해야 한다.")
    @Test
    void getBoardByInvalidId() throws Exception {
        // given
        when(boardService.getBoardById(anyLong())).thenThrow(new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // when & then
        mockMvc.perform(
                get("/api/v1/boards/{id}", -1L)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다."));
    }

    @DisplayName("게시글을 수정할 때 비밀번호는 필수 값이다.")
    @Test
    void updateBoardWithoutPassword() throws Exception {
        // given
        BoardUpdateRequest request = BoardUpdateRequest.builder()
            .title("제목")
            .content("내용")
            .writer("작성자")
            .password("")
            .build();

        // when & then
        mockMvc.perform(
                put("/api/v1/boards/{id}", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("비밀번호는 필수 값 입니다."));
    }

    @DisplayName("게시글을 수정할 때 ID는 유효해야 한다.")
    @Test
    void updateBoardWithInvalidId() throws Exception {
        // given
        BoardUpdateRequest request = BoardUpdateRequest.builder()
            .title("제목")
            .content("내용")
            .writer("작성자")
            .password("1234")
            .build();

        when(boardService.updateBoard(any(BoardUpdateCommand.class)))
            .thenThrow(new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // when & then
        mockMvc.perform(
                put("/api/v1/boards/{id}", -1L)
                    .content(objectMapper.writeValueAsString(request.toCommand(-1L)))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다."));
    }

    @DisplayName("게시글을 수정할 때 패스워드는 유효해야 한다.")
    @Test
    void updateBoardWithInvalidPassword() throws Exception {
        // given
        BoardUpdateRequest request = BoardUpdateRequest.builder()
            .title("제목")
            .content("내용")
            .writer("작성자")
            .password("12345")
            .build();

        when(boardService.updateBoard(any(BoardUpdateCommand.class)))
            .thenThrow(new IllegalArgumentException("비밀번호가 일치하지 않습니다."));

        // when & then
        mockMvc.perform(
                put("/api/v1/boards/{id}", -1L)
                    .content(objectMapper.writeValueAsString(request.toCommand(1L)))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void updateBoard() throws Exception {
        // given
        BoardCreateRequest request = BoardCreateRequest.builder()
            .title("제목 수정")
            .content("내용 수정")
            .writer("작성자 수정")
            .password("1234")
            .build();

        BoardResponse response = BoardResponse.builder()
            .id(1L)
            .title("제목 수정")
            .content("내용 수정")
            .writer("작성자 수정")
            .createdDate(LocalDateTime.of(2025, 2, 7, 12, 0))
            .build();

        when(boardService.updateBoard(any())).thenReturn(response);

        // when & then
        mockMvc.perform(
                put("/api/v1/boards/{id}", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.title").value("제목 수정"))
            .andExpect(jsonPath("$.data.content").value("내용 수정"))
            .andExpect(jsonPath("$.data.writer").value("작성자 수정"))
            .andExpect(jsonPath("$.data.createdDate").value("2025-02-07T12:00:00"));
    }

    @DisplayName("게시글을 삭제할 때 ID는 유효해야 한다.")
    @Test
    void deleteBoardWithInvalidId() throws Exception {
        // given
        BoardDeleteRequest request = BoardDeleteRequest.builder()
            .password("1234")
            .build();

        doThrow(new IllegalArgumentException("존재하지 않는 게시글입니다."))
            .when(boardService)
            .deleteBoard(any(BoardDeleteCommand.class));

        // when & then
        mockMvc.perform(
                delete("/api/v1/boards/{id}", -1L)
                    .content(objectMapper.writeValueAsString(request.toCommand(-1L)))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다."));
    }

    @DisplayName("게시글을 삭제할 때 패스워드는 유효해야 한다.")
    @Test
    void deleteBoardWithInvalidPassword() throws Exception {
        // given
        BoardDeleteRequest request = BoardDeleteRequest.builder()
            .password("12345")
            .build();

        doThrow(new IllegalArgumentException("비밀번호가 일치하지 않습니다."))
            .when(boardService)
            .deleteBoard(any(BoardDeleteCommand.class));

        // when & then
        mockMvc.perform(
                delete("/api/v1/boards/{id}", -1L)
                    .content(objectMapper.writeValueAsString(request.toCommand(1L)))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void deleteBoard() throws Exception {
        // given
        BoardDeleteRequest request = BoardDeleteRequest.builder()
            .password("1234")
            .build();

        // when & then
        mockMvc.perform(
                put("/api/v1/boards/{id}", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("OK"));
    }
}