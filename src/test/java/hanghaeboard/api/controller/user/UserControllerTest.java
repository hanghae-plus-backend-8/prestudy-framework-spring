package hanghaeboard.api.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanghaeboard.api.controller.user.request.CreateUserRequest;
import hanghaeboard.api.controller.user.request.LoginRequest;
import hanghaeboard.api.exception.exception.InvalidPasswordException;
import hanghaeboard.api.service.user.UserService;
import hanghaeboard.api.service.user.response.FindUser;
import hanghaeboard.api.service.user.response.LoginResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;


    @DisplayName("회원가입을 할 수 있다.")
    @Test
    void join()  throws Exception{
        // given
        CreateUserRequest request = CreateUserRequest.builder()
                .username("yeop")
                .password("12345678")
                .build();

        FindUser response = FindUser.builder()
                .userId(1L)
                .username("yeop")
                .build();

        when(userService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.username").value("yeop"))
                ;
    }

    @DisplayName("회원가입 시 username이 4글자 이하일 시 회원가입을 할 수 없다.")
    @Test
    void join_usernameMinLength() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("yep")
                .password("12345678")
                .build();

        FindUser response = FindUser.builder()
                .userId(1L)
                .username("yep")
                .build();

        when(userService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
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
        CreateUserRequest request = CreateUserRequest.builder()
                .username("yeopyeopyep")
                .password("12345678")
                .build();

        FindUser response = FindUser.builder()
                .userId(1L)
                .username("yeopye")
                .build();

        when(userService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
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
        CreateUserRequest request = CreateUserRequest.builder()
                .username("YEOPYEOP")
                .password("12345678")
                .build();

        FindUser response = FindUser.builder()
                .userId(1L)
                .username("YEOPYEOP")
                .build();

        when(userService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
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
        CreateUserRequest request = CreateUserRequest.builder()
                .username("yeop")
                .password("1234567")
                .build();

        FindUser response = FindUser.builder()
                .userId(1L)
                .username("yep")
                .build();

        when(userService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
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
        CreateUserRequest request = CreateUserRequest.builder()
                .username("yeop")
                .password("1234567890123456")
                .build();

        FindUser response = FindUser.builder()
                .userId(1L)
                .username("yeop")
                .build();

        when(userService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
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
        CreateUserRequest request = CreateUserRequest.builder()
                .username("yeop")
                .password("yeop!@###")
                .build();

        FindUser response = FindUser.builder()
                .userId(1L)
                .username("yeop")
                .build();

        when(userService.join(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("비밀번호는 소문자 혹은 대문자 영문과 숫자로만 이루어져야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("회원가입 시 동일한 username으로 회원가입을 할 수 없다.")
    @Test
    void join_duplicateUsername() throws Exception{
        CreateUserRequest request = CreateUserRequest.builder()
                .username("yeop")
                .password("yeop1234")
                .build();

        when(userService.join(any())).thenThrow(new DuplicateKeyException("이미 존재하는 ID입니다."));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("409"))
                .andExpect(jsonPath("$.message").value("이미 존재하는 ID입니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("로그인을 할 수 있다.")
    @Test
    void login()  throws Exception{
        // given
        LoginRequest request = LoginRequest.builder()
                .username("yeop")
                .password("12345678")
                .build();

        LoginResponse response = LoginResponse.builder()
                .jwtToken("token")
                .build();

        when(userService.login(any())).thenReturn(response);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, Matchers.startsWith("Bearer ")))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("로그인 시 username에 해당하는 user가 없는 경우 로그인을 할 수 없다.")
    @Test
    void login_notFoundUser()  throws Exception{
        // given
        LoginRequest request = LoginRequest.builder()
                .username("yeop")
                .password("12345678")
                .build();


        when(userService.login(any())).thenThrow(new IllegalArgumentException("일치하는 회원이 없습니다."));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("일치하는 회원이 없습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @DisplayName("로그인 시 비밀번호가 틀린 경우 로그인을 할 수 없다.")
    @Test
    void login_isNotCorrectPassword()  throws Exception{
        // given
        LoginRequest request = LoginRequest.builder()
                .username("yeop")
                .password("12345678")
                .build();

        when(userService.login(any())).thenThrow(new InvalidPasswordException("비밀번호가 올바르지 않습니다."));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("비밀번호가 올바르지 않습니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

}