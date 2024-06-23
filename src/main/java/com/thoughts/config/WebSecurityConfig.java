package com.thoughts.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final DataSource dataSource;

    private final static String USERNAME_PASSWORD_ACTIVE_QUERY = """
            SELECT username, password, active FROM users WHERE username = ?
            """;

    private final static String USERNAME_ROLES_JOIN = """
            SELECT u.username, ur.roles FROM users u JOIN user_role ur ON u.id = ur.user_id WHERE u.username = ?
            """;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/login", "/registration").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) {
        try {

            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .passwordEncoder(NoOpPasswordEncoder.getInstance())
                    .usersByUsernameQuery(USERNAME_PASSWORD_ACTIVE_QUERY)
                    .authoritiesByUsernameQuery(USERNAME_ROLES_JOIN);

        } catch (Exception exception) {
            throw new RuntimeException("Error setting authentication parameters", exception);
        }
    }
}