package hanghaeboard.api.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.service.board.BoardService;
import hanghaeboard.api.service.board.response.CreateBoardResponse;
import hanghaeboard.api.service.board.response.FindBoardResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BoardService boardService;

    @DisplayName("게시물 목록을 조회할 수 있다.")
    @Test
    void test() throws Exception {
        // given
        FindBoardResponse response = FindBoardResponse.builder()
                .id(1L)
                .writer("yeop")
                .title("title")
                .content("content")
                .build();

        when(boardService.findAllBoard()).thenReturn(List.of(response));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/boards"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[*].id").value(1))
                .andExpect(jsonPath("$.data[*].writer").value("yeop"))
                .andExpect(jsonPath("$.data[*].title").value("title"))
                .andExpect(jsonPath("$.data[*].content").value("content"));
    }

    @DisplayName("게시물을 생성할 수 있다.")
    @Test
    void createBoard() throws Exception{
        // given
        CreateBoardRequest request = CreateBoardRequest.builder()
                .writer("yeop")
                .password("1234")
                .title("title")
                .content("content")
                .build();

        LocalDateTime createdDatetime = LocalDateTime.of(2025, 2, 19, 14, 0);

        CreateBoardResponse response = CreateBoardResponse.builder().id(1L).writer("yeop").title("title").content("content").createdDatetime(createdDatetime).build();

        when(boardService.createBoard(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.writer").value("yeop"))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.content").value("content"))
                .andExpect(jsonPath("$.data.createdDatetime").value("2025-02-19T14:00:00"))
                ;
    }

    @DisplayName("작성자 없이 게시물을 생성할 수 없다.")
    @Test
    void createBoardWithoutWriter() throws Exception{
        // given
        CreateBoardRequest request = CreateBoardRequest.builder()
                .password("1234")
                .title("title")
                .content("content")
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/boards")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("작성자명은 필수 입력입니다."))
                ;
    }

    @DisplayName("비밀번호 없이 게시물을 생성할 수 없다.")
    @Test
    void createBoardWithoutPassword() throws Exception{
        // given
        CreateBoardRequest request = CreateBoardRequest.builder()
                .writer("yeop")
                .title("title")
                .content("content")
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/boards")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수 입력입니다."))
        ;
    }

    @DisplayName("제목 없이 게시물을 생성할 수 없다.")
    @Test
    void createBoardWithoutTitle() throws Exception{
        // given
        CreateBoardRequest request = CreateBoardRequest.builder()
                .writer("yeop")
                .password("1234")
                .content("content")
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/boards")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("제목은 필수 입력입니다."))
        ;
    }

    @DisplayName("내용 없이 게시물을 생성할 수 없다.")
    @Test
    void createBoardWithoutContent() throws Exception{
        // given
        CreateBoardRequest request = CreateBoardRequest.builder()
                .writer("yeop")
                .password("1234")
                .title("title")
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/boards")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("내용은 필수 입력입니다."))
        ;
    }
}