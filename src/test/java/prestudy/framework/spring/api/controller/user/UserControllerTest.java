package prestudy.framework.spring.api.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import prestudy.framework.spring.api.controller.user.request.UserCreateRequest;
import prestudy.framework.spring.api.controller.user.request.UserLoginRequest;
import prestudy.framework.spring.api.service.user.command.UserCreateCommand;
import prestudy.framework.spring.api.service.user.command.UserLoginCommand;
import prestudy.framework.spring.support.ControllerTestSupport;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends ControllerTestSupport {

    @DisplayName("유저 생성할 때 유저명은 필수 값이다.")
    @Test
    void createUserWithoutUsername() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
            .password("<PASSWORD>")
            .build();

        // when & then
        mockMvc.perform(
                post("/api/v1/users")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("유저명은 필수 값 입니다."));
    }

    @DisplayName("유저 생성할 때 비밀번호는 필수 값이다.")
    @Test
    void createUserWithoutPassword() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
            .username("username")
            .build();

        // when & then
        mockMvc.perform(
                post("/api/v1/users")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("비밀번호는 필수 값 입니다."));
    }

    @DisplayName("이미 존재하는 유저명으로 유저를 생성하지 못한다.")
    @Test
    void createUserWithExistUsername() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
            .username("123abc")
            .password("Password12")
            .build();

        doThrow(new IllegalArgumentException("중복된 username 입니다."))
            .when(userService)
            .createUser(any(UserCreateCommand.class));

        // when & then
        mockMvc.perform(
                post("/api/v1/users")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("중복된 username 입니다."));
    }

    @DisplayName("유저를 생성한다.")
    @Test
    void createUser() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
            .username("123abc")
            .password("Password12")
            .build();

        // when & then
        mockMvc.perform(
                post("/api/v1/users")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("로그인 시, 유저명은 필수 값이다.")
    @Test
    void loginUserWithoutUsername() throws Exception {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
            .password("<PASSWORD>")
            .build();

        // when & then
        mockMvc.perform(
                post("/api/v1/users/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("유저명은 필수 값 입니다."));
    }

    @DisplayName("로그인 시, 패스워드는 필수 값이다.")
    @Test
    void loginUserWithoutPassword() throws Exception {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
            .username("username")
            .build();

        // when & then
        mockMvc.perform(
                post("/api/v1/users/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("비밀번호는 필수 값 입니다."));
    }

    @DisplayName("로그인 정보가 일치하지 않으면 로그인 할 수 없다.")
    @Test
    void loginUserWithWrongUserInfo() throws Exception {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
            .username("username")
            .password("<PASSWORD>")
            .build();

        when(userService.loginUser(any(UserLoginCommand.class)))
            .thenThrow(new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(
                post("/api/v1/users/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다."));
    }

    @DisplayName("로그인을 한다.")
    @Test
    void loginUser() throws Exception {
        // given
        UserLoginRequest request = UserLoginRequest.builder()
            .username("username")
            .password("<PASSWORD>")
            .build();

        when(userService.loginUser(any(UserLoginCommand.class))).thenReturn("token");

        // when & then
        mockMvc.perform(
                post("/api/v1/users/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(header().string("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200));
    }
}