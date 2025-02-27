package com.hhplus.precourse.common.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenManagerTest {
    private JwtTokenManager jwtTokenManager;

    @BeforeEach
    void setUp() {
        var testSecretKey = "test-IreS4OmMJMiwHIu4sCZTqKSeo/AGjVe6SyN7Wo=";
        var testValidityInMilliseconds = 120000L;
        jwtTokenManager = new JwtTokenManager(testSecretKey, testValidityInMilliseconds);
    }

    @Nested
    class IssueTest {
        @Test
        void success() {
            // given
            var userId = 1L;
            var userName = "testuser";

            // when
            var result = jwtTokenManager.issue(userId, userName);

            // then
            assertThat(result).isNotNull();
        }
    }
}