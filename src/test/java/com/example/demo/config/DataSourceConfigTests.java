package com.example.demo.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DataSourceConfigTests {

    @Test
    void convertsPostgresUrlToJdbcUrl() {
        DataSourceConfig.ConnectionSettings settings = DataSourceConfig.normalizeUrl(
            "postgresql://user:secret@example.com/neondb?sslmode=require&channel_binding=require"
        );

        assertThat(settings.jdbcUrl())
            .isEqualTo("jdbc:postgresql://example.com/neondb?sslmode=require&channelBinding=require");
        assertThat(settings.username()).isEqualTo("user");
        assertThat(settings.password()).isEqualTo("secret");
    }

    @Test
    void keepsJdbcUrl() {
        DataSourceConfig.ConnectionSettings settings = DataSourceConfig.normalizeUrl(
            "jdbc:postgresql://example.com/neondb?sslmode=require&channelBinding=require"
        );

        assertThat(settings.jdbcUrl())
            .isEqualTo("jdbc:postgresql://example.com/neondb?sslmode=require&channelBinding=require");
    }
}
