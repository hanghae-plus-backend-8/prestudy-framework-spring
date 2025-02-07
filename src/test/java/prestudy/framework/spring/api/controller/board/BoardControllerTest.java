package prestudy.framework.spring.api.controller.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import prestudy.framework.spring.support.ControllerTestSupport;
import prestudy.framework.spring.api.controller.board.response.BoardResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest extends ControllerTestSupport {

    @DisplayName("게시글 목록을 조회한다.")
    @Test
    void getBoards() throws Exception {
        //given
        BoardResponse response = BoardResponse.builder()
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
            .andExpect(jsonPath("$.data[*].title").value("제목"))
            .andExpect(jsonPath("$.data[*].content").value("내용"))
            .andExpect(jsonPath("$.data[*].writer").value("작성자"))
            .andExpect(jsonPath("$.data[*].createdDate").value("2025-02-07T12:00:00"));
    }

}