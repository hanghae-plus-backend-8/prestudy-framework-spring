package prestudy.framework.spring.api.controller.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import prestudy.framework.spring.api.controller.board.request.BoardCreateRequest;
import prestudy.framework.spring.api.controller.board.response.BoardResponse;
import prestudy.framework.spring.support.ControllerTestSupport;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}