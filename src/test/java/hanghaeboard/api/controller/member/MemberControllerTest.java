package hanghaeboard.api.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanghaeboard.api.controller.member.request.CreateMemberRequest;
import hanghaeboard.api.service.member.MemberService;
import hanghaeboard.api.service.member.response.FindMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberService memberService;


    @DisplayName("회원가입을 할 수 있다.")
    @Test
    void join()  throws Exception{
        // given
        CreateMemberRequest request = CreateMemberRequest.builder()
                .username("yeop")
                .password("12345678")
                .build();

        FindMember response = FindMember.builder()
                .memberId(1L)
                .username("yeop")
                .build();

        when(memberService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/member/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.memberId").value(1))
                .andExpect(jsonPath("$.data.username").value("yeop"))
                ;
    }

    @DisplayName("회원가입 시 username이 4글자 이하일 시 회원가입을 할 수 없다.")
    @Test
    void join_usernameMinLength() throws Exception {
        CreateMemberRequest request = CreateMemberRequest.builder()
                .username("yep")
                .password("12345678")
                .build();

        FindMember response = FindMember.builder()
                .memberId(1L)
                .username("yep")
                .build();

        when(memberService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/member/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("아이디는 4글자 이상, 10글자 이하여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("회원가입 시 username이 10글자 이상일 시 회원가입을 할 수 없다.")
    @Test
    void join_usernameMaxLength() throws Exception {
        CreateMemberRequest request = CreateMemberRequest.builder()
                .username("yeopyeopyep")
                .password("12345678")
                .build();

        FindMember response = FindMember.builder()
                .memberId(1L)
                .username("yeopye")
                .build();

        when(memberService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/member/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("아이디는 4글자 이상, 10글자 이하여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("회원가입 시 아이디는 소문자 영문과 숫자로만 이루어지지 않은 경우 회원가입을 할 수 없다.")
    @Test
    void join_withoutUpperCase() throws Exception{
        CreateMemberRequest request = CreateMemberRequest.builder()
                .username("YEOPYEOP")
                .password("12345678")
                .build();

        FindMember response = FindMember.builder()
                .memberId(1L)
                .username("YEOPYEOP")
                .build();

        when(memberService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/member/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("아이디는 소문자 영문과 숫자로만 이루어져야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                ;
    }

    @DisplayName("회원가입 시 비밀번호가 8글자 이하일 시 회원가입을 할 수 없다.")
    @Test
    void join_passwordMinLength() throws Exception {
        CreateMemberRequest request = CreateMemberRequest.builder()
                .username("yeop")
                .password("1234567")
                .build();

        FindMember response = FindMember.builder()
                .memberId(1L)
                .username("yep")
                .build();

        when(memberService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/member/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("비밀번호는 8글자 이상, 15글자 이하여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("회원가입 시 비밀번호가 15글자 이상일 시 회원가입을 할 수 없다.")
    @Test
    void join_passwordMaxLength() throws Exception {
        CreateMemberRequest request = CreateMemberRequest.builder()
                .username("yeop")
                .password("1234567890123456")
                .build();

        FindMember response = FindMember.builder()
                .memberId(1L)
                .username("yeop")
                .build();

        when(memberService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/member/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("비밀번호는 8글자 이상, 15글자 이하여야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("회원가입 시 비밀번호는 소문자 혹은 대문자 영문과 숫자로만 이루어지지 않은 경우 회원가입을 할 수 없다.")
    @Test
    void join_withoutSpecialCharacter() throws Exception{
        CreateMemberRequest request = CreateMemberRequest.builder()
                .username("yeop")
                .password("yeop!@###")
                .build();

        FindMember response = FindMember.builder()
                .memberId(1L)
                .username("yeop")
                .build();

        when(memberService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/member/join")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("비밀번호는 소문자 혹은 대문자 영문과 숫자로만 이루어져야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

}