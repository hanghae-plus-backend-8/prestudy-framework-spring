package com.hhplus.precourse.common;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
public class MySQLTestContainerConfig {

    private static final MySQLContainer<?> MYSQL_CONTAINER;

    static {
        MYSQL_CONTAINER = new MySQLContainer<>("mysql:8")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
        MYSQL_CONTAINER.start();
    }

    @Bean
    public MySQLContainer<?> mysqlContainer() {
        return MYSQL_CONTAINER;
    }

    // 정적 메서드를 통해 컨테이너 인스턴스를 반환할 수 있음
    public static MySQLContainer<?> getContainer() {
        return MYSQL_CONTAINER;
    }
}