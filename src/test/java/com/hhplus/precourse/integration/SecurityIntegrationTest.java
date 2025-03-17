package com.hhplus.precourse.integration;

import com.hhplus.precourse.common.IntegrationTest;
import com.hhplus.precourse.common.component.JwtTokenManager;
import com.hhplus.precourse.user.domain.User;
import com.hhplus.precourse.user.domain.UserFixture;
import com.hhplus.precourse.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityIntegrationTest extends IntegrationTest {
    private static final String TEST_ENDPOINT = "/users/me";
    private static final String ADMIN_ENDPOINT = "/admin/users/{id}";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenManager jwtTokenManager;

    private User normalUser;
    private User adminUser;
    private String jwtToken;


    @BeforeEach
    void setUp() {
        this.normalUser = userRepository.save(UserFixture.normalUser());
        this.adminUser = userRepository.save(UserFixture.adminUser());
        this.jwtToken = jwtTokenManager.issue(normalUser.id(), normalUser.name());
    }
    
    @Test
    @DisplayName("유효한 토큰으로 인증된 요청은 성공한다")
    void authenticateWithValidToken() throws Exception {
        mockMvc.perform(
                get(TEST_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("토큰이 없는 요청은 인증에 실패한다")
    void failAuthenticationWithoutToken() throws Exception {
        mockMvc.perform(
                get(TEST_ENDPOINT)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다."));
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 요청하면 인증에 실패한다")
    void failAuthenticationWithInvalidToken() throws Exception {
        var invalidToken = "invalid.token.value";

        mockMvc.perform(
                get(TEST_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다."));
    }
    
    @Test
    @DisplayName("Bearer 접두사 없는 토큰은 인증에 실패한다")
    void failAuthenticationWithoutBearerPrefix() throws Exception {
        mockMvc.perform(
                get(TEST_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, jwtToken)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다."));
    }
    
    @Test
    @DisplayName("존재하지 않는 사용자 ID로 생성된 토큰은 인증에 실패한다")
    void failAuthenticationWithNonExistentUserId() throws Exception {
        var token = jwtTokenManager.issue(9999L, "notExist");

        mockMvc.perform(
                get(TEST_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다."));
    }
    
    @Test
    @DisplayName("관리자 권한이 필요한 엔드포인트에 일반 사용자가 접근하면 실패한다")
    void failAccessToAdminEndpointWithNormalUser() throws Exception {
        mockMvc.perform(
                get(ADMIN_ENDPOINT, normalUser.id())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            )
            .andDo(print())
            .andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("관리자 권한이 필요한 엔드포인트에 관리자가 접근하면 성공한다")
    void accessAdminEndpointWithAdminUser() throws Exception {
        var token = jwtTokenManager.issue(adminUser.id(), normalUser.name());

        mockMvc.perform(
                get(ADMIN_ENDPOINT, normalUser.id())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }
}