package com.hhplus.precourse.integration;

import com.hhplus.precourse.common.IntegrationTest;
import com.hhplus.precourse.common.support.utils.JsonUtils;
import com.hhplus.precourse.user.controller.CreateUserController;
import com.hhplus.precourse.user.controller.LoginController;
import com.hhplus.precourse.user.domain.UserFixture;
import com.hhplus.precourse.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import static com.hhplus.precourse.common.support.ApplicationStatus.MISMATCH_PASSWORD;
import static com.hhplus.precourse.common.support.ApplicationStatus.USER_NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserIntegrationTest extends IntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원 가입")
    @Test
    void signUp() throws Exception {
        var request = new CreateUserController.Request(
            "test1234",
            "testPassword"
        );

        mockMvc.perform(
                post("/users/sign-up")
                    .contentType("application/json")
                    .content(JsonUtils.stringify(request))
            )
            .andExpect(status().isOk());
    }

    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        var user = userRepository.save(new UserFixture().build());
        var loginRequest = new LoginController.Request(
            user.name(),
            user.password()
        );

        mockMvc.perform(
                post("/users/login")
                    .contentType("application/json")
                    .content(JsonUtils.stringify(loginRequest))
            )
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.AUTHORIZATION));
    }

    @DisplayName("잘못된 비밀번호로 로그인 시도")
    @Test
    void loginWithWrongPassword() throws Exception {
        var user = userRepository.save(new UserFixture().build());
        var loginRequest = new LoginController.Request(
            user.name(),
            "wrongPassword"
        );

        mockMvc.perform(
                post("/users/login")
                    .contentType("application/json")
                    .content(JsonUtils.stringify(loginRequest))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(MISMATCH_PASSWORD.message()));
    }

    @DisplayName("존재하지 않는 사용자로 로그인 시도")
    @Test
    void loginWithNonExistentUser() throws Exception {
        var loginRequest = new LoginController.Request(
            "nonExistentUser",
            "testPassword"
        );

        mockMvc.perform(
                post("/users/login")
                    .contentType("application/json")
                    .content(JsonUtils.stringify(loginRequest))
            )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(USER_NOT_FOUND.message()));
    }
}
