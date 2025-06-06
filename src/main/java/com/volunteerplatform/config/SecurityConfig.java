package com.volunteerplatform.config;

import com.volunteerplatform.service.session.AppUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AppUserDetailsService appUserDetailsService) throws Exception {
        return http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable())
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(
                        authorizeRequest -> {
                            authorizeRequest
                                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                    .requestMatchers("/", "/users/login", "/users/login-error", "/users/register", "/about").permitAll()
                                    .requestMatchers("/api/comments/").permitAll()
                                    .requestMatchers("/admin/**").hasRole("ADMIN")
                                    .requestMatchers("/users/dashboard", "/users/profile").authenticated()
                                    .requestMatchers("/{userId}/roles", "/{userId}/remove-roles").hasRole("ADMIN")
                                    .anyRequest().authenticated();
                        }
                )
                .formLogin(
                        formLogin -> {
                            formLogin.loginPage("/users/login");
                            formLogin.usernameParameter("username");
                            formLogin.passwordParameter("password");
                            formLogin.defaultSuccessUrl("/", true);
                            formLogin.failureUrl("/users/login-error");
                        }
                )
                .logout(
                        logout -> {
                            logout.logoutUrl("/users/logout");
                            logout.logoutSuccessUrl("/");
                            logout.invalidateHttpSession(true);
                        }
                )
                .userDetailsService(appUserDetailsService)
                .build();
    }

}