package prestudy.framework.spring.api.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import prestudy.framework.spring.api.controller.user.request.UserCreateRequest;
import prestudy.framework.spring.api.service.user.command.UserCreateCommand;
import prestudy.framework.spring.support.ControllerTestSupport;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
            .andExpect(status().isOk())
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
            .andExpect(status().isOk())
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

        doThrow(new IllegalArgumentException("이미 존재하는 유저명입니다."))
            .when(userService)
            .createUser(any(UserCreateCommand.class));

        // when & then
        mockMvc.perform(
                post("/api/v1/users")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("이미 존재하는 유저명입니다."));
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
}