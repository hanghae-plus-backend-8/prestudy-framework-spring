package hanghaeboard.api.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.service.board.BoardService;
import hanghaeboard.api.service.board.response.FindBoardResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

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

    @AfterEach
    void tearDown() {

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

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[*].id").value(1))
                .andExpect(jsonPath("$.data[*].writer").value("yeop"))
                .andExpect(jsonPath("$.data[*].title").value("title"))
                .andExpect(jsonPath("$.data[*].content").value("content"));
    }
}