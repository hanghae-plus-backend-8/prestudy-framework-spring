package com.hhplus.precourse.common;

import com.hhplus.precourse.auth.AuthenticatedUser;
import com.hhplus.precourse.user.domain.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestConfiguration
public class TestUserDetailsConfig {
    public static final String USER_DETAILS_BEAN_NAME = "testUserDetailsService";
    public static final String ADMIN_DETAILS_BEAN_NAME = "testAdminDetailsService";

    @Bean(name = USER_DETAILS_BEAN_NAME)
    public UserDetailsService testUserDetailsService() {
        return username -> new AuthenticatedUser(1L, "testUser12", User.RoleType.USER);
    }

    @Bean(name = ADMIN_DETAILS_BEAN_NAME)
    public UserDetailsService testAdminDetailsService() {
        return username -> new AuthenticatedUser(2L, "testAdmin12", User.RoleType.ADMIN);
    }
}
