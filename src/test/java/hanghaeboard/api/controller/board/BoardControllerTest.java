package hanghaeboard.api.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.controller.board.request.DeleteBoardRequest;
import hanghaeboard.api.controller.board.request.UpdateBoardRequest;
import hanghaeboard.api.exception.exception.InvalidPasswordException;
import hanghaeboard.api.service.board.BoardService;
import hanghaeboard.api.service.board.response.CreateBoardResponse;
import hanghaeboard.api.service.board.response.DeleteBoardResponse;
import hanghaeboard.api.service.board.response.FindBoardResponse;
import hanghaeboard.api.service.board.response.UpdateBoardResponse;
import jakarta.persistence.EntityNotFoundException;
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

    @DisplayName("id로 게시물을 조회할 수 있다.")
    @Test
    void findBoardById() throws Exception{
        // given
        FindBoardResponse response = FindBoardResponse.builder()
                .id(1L)
                .writer("yeop")
                .title("title")
                .content("content")
                .build();

        when(boardService.findBoardById(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/boards/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.writer").value("yeop"))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.content").value("content"));
    }

    @DisplayName("id에 해당하는 게시물이 없는경우 조회되지 않는다.")
    @Test
    void findBoardById_notFoundBoard() throws Exception{
        // given

        when(boardService.findBoardById(any())).thenThrow(new EntityNotFoundException("조회된 게시물이 없습니다."));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/boards/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("조회된 게시물이 없습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                ;
    }


    @DisplayName("게시물을 수정할 수 있다.")
    @Test
    void modifyBoard() throws Exception{
        // given
        UpdateBoardRequest request = UpdateBoardRequest.builder()
                .password("1234")
                .writer("writer")
                .title("title")
                .content("content")
                .build();

        UpdateBoardResponse response = UpdateBoardResponse.builder()
                .id(1L)
                .writer("writer")
                .title("title")
                .content("content")
                .createdDatetime(LocalDateTime.of(2025, 2, 21, 14, 0))
                .lastModifiedDatetime(LocalDateTime.of(2025, 2, 21, 19, 0))
                .build();

        when(boardService.updateBoard(any(), any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/boards/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.writer").value("writer"))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.content").value("content"))
                .andExpect(jsonPath("$.data.createdDatetime").value("2025-02-21T14:00:00"))
                .andExpect(jsonPath("$.data.lastModifiedDatetime").value("2025-02-21T19:00:00"))
                ;
    }

    @DisplayName("게시물 수정 요청 시 비밀번호가 없으면 수정할 수 없다.")
    @Test
    void modifyBoardWithoutPassword() throws Exception{
        // given
        UpdateBoardRequest request = UpdateBoardRequest.builder()
                .writer("writer")
                .title("title")
                .content("content")
                .build();

        UpdateBoardResponse response = UpdateBoardResponse.builder()
                .id(1L)
                .writer("writer")
                .title("title")
                .content("content")
                .createdDatetime(LocalDateTime.of(2025, 2, 21, 14, 0))
                .lastModifiedDatetime(LocalDateTime.of(2025, 2, 21, 19, 0))
                .build();

        when(boardService.updateBoard(any(), any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/boards/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수 입력입니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("게시물 수정 요청 시 작성자가 없으면 수정할 수 없다.")
    @Test
    void modifyBoardWithoutWriter() throws Exception{
        // given
        UpdateBoardRequest request = UpdateBoardRequest.builder()
                .password("1234")
                .title("title")
                .content("content")
                .build();

        UpdateBoardResponse response = UpdateBoardResponse.builder()
                .id(1L)
                .writer("writer")
                .title("title")
                .content("content")
                .createdDatetime(LocalDateTime.of(2025, 2, 21, 14, 0))
                .lastModifiedDatetime(LocalDateTime.of(2025, 2, 21, 19, 0))
                .build();

        when(boardService.updateBoard(any(), any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/boards/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("작성자명은 필수 입력입니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("게시물 수정 요청 시 제목이 없으면 수정할 수 없다.")
    @Test
    void modifyBoardWithoutTitle() throws Exception{
        // given
        UpdateBoardRequest request = UpdateBoardRequest.builder()
                .password("1234")
                .writer("writer")
                .content("content")
                .build();

        UpdateBoardResponse response = UpdateBoardResponse.builder()
                .id(1L)
                .writer("writer")
                .title("title")
                .content("content")
                .createdDatetime(LocalDateTime.of(2025, 2, 21, 14, 0))
                .lastModifiedDatetime(LocalDateTime.of(2025, 2, 21, 19, 0))
                .build();

        when(boardService.updateBoard(any(), any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/boards/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("제목은 필수 입력입니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("게시물 수정 요청 시 내용이 없으면 수정할 수 없다.")
    @Test
    void modifyBoardWithoutContent() throws Exception{
        // given
        UpdateBoardRequest request = UpdateBoardRequest.builder()
                .password("1234")
                .writer("writer")
                .title("title")
                .build();

        UpdateBoardResponse response = UpdateBoardResponse.builder()
                .id(1L)
                .writer("writer")
                .title("title")
                .content("content")
                .createdDatetime(LocalDateTime.of(2025, 2, 21, 14, 0))
                .lastModifiedDatetime(LocalDateTime.of(2025, 2, 21, 19, 0))
                .build();

        when(boardService.updateBoard(any(), any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/boards/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("내용은 필수 입력입니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("게시물 수정 요청 시 게시물을 확인할 수 없는 경우 수정할 수 없다.")
    @Test
    void modifyBoard_notFound() throws Exception{
        // given
        UpdateBoardRequest request = UpdateBoardRequest.builder()
                .password("1234")
                .writer("writer")
                .title("title")
                .content("content")
                .build();

        when(boardService.updateBoard(any(), any())).thenThrow(new EntityNotFoundException("조회된 게시물이 없습니다."));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/boards/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("조회된 게시물이 없습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("게시물 수정 요청 시 비밀번호가 다른 경우 수정할 수 없다.")
    @Test
    void modifyBoard_notInvalidPassword() throws Exception{
        // given
        UpdateBoardRequest request = UpdateBoardRequest.builder()
                .password("1234")
                .writer("writer")
                .title("title")
                .content("content")
                .build();

        when(boardService.updateBoard(any(), any())).thenThrow(new InvalidPasswordException("비밀번호가 올바르지 않습니다."));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/boards/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("비밀번호가 올바르지 않습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("게시물을 삭제할 수 있다.")
    @Test
    void deleteBoard() throws Exception{
        // given
        DeleteBoardRequest request = DeleteBoardRequest.builder().password("1234").build();

        LocalDateTime deletedDatetime = LocalDateTime.of(2025, 2, 24, 14, 40);
        DeleteBoardResponse response = DeleteBoardResponse.builder().deletedDatetime(deletedDatetime).build();


        when(boardService.deleteBoard(any(), any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/boards/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.deletedDatetime").value("2025-02-24T14:40:00"))
        ;
    }

    @DisplayName("비밀번호 없이 게시물을 삭제할 수 없다.")
    @Test
    void deleteBoardWithoutPassword() throws Exception{
        // given
        DeleteBoardRequest request = DeleteBoardRequest.builder().build();

        LocalDateTime deletedDatetime = LocalDateTime.of(2025, 2, 24, 14, 40);
        DeleteBoardResponse response = DeleteBoardResponse.builder().deletedDatetime(deletedDatetime).build();


        when(boardService.deleteBoard(any(), any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/boards/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수 입력입니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("존재하지 않는 게시물인 경우 게시물을 삭제할 수 없다.")
    @Test
    void deleteBoard_notFoundBoard() throws Exception{
        // given
        DeleteBoardRequest request = DeleteBoardRequest.builder().password("1234").build();

        when(boardService.deleteBoard(any(), any())).thenThrow(new EntityNotFoundException("조회된 게시물이 없습니다."));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/boards/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("조회된 게시물이 없습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("비밀번호가 틀린 경우 게시물을 삭제할 수 없다.")
    @Test
    void deleteBoard_notInvalidPassword() throws Exception{
        // given
        DeleteBoardRequest request = DeleteBoardRequest.builder().password("1234").build();

        when(boardService.deleteBoard(any(), any())).thenThrow(new InvalidPasswordException("비밀번호가 올바르지 않습니다."));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/boards/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("비밀번호가 올바르지 않습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

}