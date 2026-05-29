package com.example.demo.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(Environment environment) {
        String rawUrl = firstNonBlank(
            environment.getProperty("SPRING_DATASOURCE_URL"),
            environment.getProperty("DATABASE_URL"),
            environment.getProperty("spring.datasource.url")
        );

        if (!hasText(rawUrl)) {
            throw new IllegalStateException(
                "Define SPRING_DATASOURCE_URL or DATABASE_URL with your PostgreSQL connection string."
            );
        }

        ConnectionSettings settings = normalizeUrl(rawUrl);
        String username = firstNonBlank(
            environment.getProperty("SPRING_DATASOURCE_USERNAME"),
            environment.getProperty("spring.datasource.username"),
            settings.username()
        );
        String password = firstNonBlank(
            environment.getProperty("SPRING_DATASOURCE_PASSWORD"),
            environment.getProperty("spring.datasource.password"),
            settings.password()
        );

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(settings.jdbcUrl());
        config.setMaximumPoolSize(maximumPoolSize(environment));

        if (hasText(username)) {
            config.setUsername(username);
        }
        if (hasText(password)) {
            config.setPassword(password);
        }

        return new HikariDataSource(config);
    }

    static ConnectionSettings normalizeUrl(String rawUrl) {
        String url = rawUrl.trim();
        if (url.startsWith("jdbc:postgresql://") || url.startsWith("jdbc:h2:")) {
            return new ConnectionSettings(url, null, null);
        }
        if (url.startsWith("postgresql://") || url.startsWith("postgres://")) {
            return normalizePostgresUri(url);
        }

        throw new IllegalArgumentException(
            "Database URL must start with jdbc:postgresql://, postgresql://, postgres://, or jdbc:h2:."
        );
    }

    private static ConnectionSettings normalizePostgresUri(String rawUrl) {
        try {
            URI uri = new URI(rawUrl);
            String path = hasText(uri.getRawPath()) ? uri.getRawPath() : "/";
            String query = uri.getRawQuery();
            StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://")
                .append(uri.getHost());

            if (uri.getPort() > 0) {
                jdbcUrl.append(':').append(uri.getPort());
            }

            jdbcUrl.append(path);

            if (hasText(query)) {
                jdbcUrl.append('?').append(query.replace("channel_binding=", "channelBinding="));
            }

            Credentials credentials = credentialsFrom(uri);
            return new ConnectionSettings(jdbcUrl.toString(), credentials.username(), credentials.password());
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid PostgreSQL connection string.", ex);
        }
    }

    private static Credentials credentialsFrom(URI uri) {
        String userInfo = uri.getRawUserInfo();
        if (!hasText(userInfo)) {
            return new Credentials(null, null);
        }

        int separator = userInfo.indexOf(':');
        if (separator < 0) {
            return new Credentials(decode(userInfo), null);
        }

        String username = decode(userInfo.substring(0, separator));
        String password = decode(userInfo.substring(separator + 1));
        return new Credentials(username, password);
    }

    private static int maximumPoolSize(Environment environment) {
        String value = firstNonBlank(
            environment.getProperty("SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE"),
            environment.getProperty("spring.datasource.hikari.maximum-pool-size")
        );
        if (!hasText(value)) {
            return 5;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return 5;
        }
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    record ConnectionSettings(String jdbcUrl, String username, String password) {
    }

    private record Credentials(String username, String password) {
    }
}
