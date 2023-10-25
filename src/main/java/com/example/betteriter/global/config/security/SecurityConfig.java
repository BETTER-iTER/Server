package com.example.betteriter.global.config.security;

import com.example.betteriter.global.config.web.CorsConfig;
import com.example.betteriter.global.filter.JwtAccessDeniedHandler;
import com.example.betteriter.global.filter.JwtAuthenticationEntryPoint;
import com.example.betteriter.global.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CorsConfig corsConfig;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/css/**", "/js/**",
                        "/img/**", "/error",
                        "/favicon.ico", "/resources/**",
                        "/swagger-ui/**", "/v3/api-docs/**"
                );
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                // 특정 URI 예외 처리
                // antMatchers().permitAll() 을 통해 특정 API 요청은
                // jwtAuthenticationFilter 에 걸려도 ExceptionTranslationFilter 을 거치지 않는다 x (무시 x -> 일단 인증 필터를 거치긴 함 !!)
                .antMatchers("/login/callback/**",
                        "/auth/**", "/temp/**",
                        "/test/**", "/reissue")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Authentication
                .accessDeniedHandler(jwtAccessDeniedHandler) // Authorization
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
