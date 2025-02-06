package hanghaeboard.api.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.service.board.BoardService;
import hanghaeboard.api.service.member.MemberService;
import hanghaeboard.domain.member.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BoardService boardService;

    @MockitoBean
    private MemberService memberService;

    @AfterEach
    void tearDown() {

    }

    @DisplayName("게시물을 생성할 수 있다.")
    @Test
    void createBoard() throws Exception{
        // given
        Member dummyMember = Member.builder().id(1L).username("yeop").build();
        when(memberService.findMemberById(1L)).thenReturn(dummyMember);
        CreateBoardRequest request = CreateBoardRequest.builder()
                .memberId(1L)
                .title("title")
                .content("content")
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/board/create")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("게시물을 생성할 수 있다.")
    @Test
    void createBoardWithoutMember() throws Exception{
        // given
        CreateBoardRequest request = CreateBoardRequest.builder()
                .memberId(1L)
                .title("title")
                .content("content")
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/board/create")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


}