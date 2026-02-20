package uman.tunginside;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uman.tunginside.jwt.JWTFilter;
import uman.tunginside.jwt.JWTUtil;
import uman.tunginside.jwt.LoginFilter;
import uman.tunginside.repository.RefreshTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenRepository refreshTokenRepository;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 패스워드 암호화 메소드
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 테스트를 위해 일단 csrf비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
//                .formLogin(login -> login
//                        .loginProcessingUrl("/api/member/login")
//                        .usernameParameter("userid")
//                        .successHandler(((request, response, authentication) -> {
//                            response.setStatus(HttpServletResponse.SC_OK);
//                            response.setCharacterEncoding("UTF-8");
//                            response.setContentType("application/json");
//                            response.getWriter().write("{\"message\":\"로그인 성공\"}");
//                        }))
//                        .failureHandler(((request, response, exception) -> {
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            response.setCharacterEncoding("UTF-8");
//                            response.setContentType("application/json");
//                            response.getWriter().write("{\"message\":\"로그인 실패: "+exception.getMessage()+"\"}");
//                        }))
//                )
                .logout(logout -> logout
                        .logoutUrl("/api/member/logout")
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"로그아웃 성공\"}");
                        }))
                        .deleteCookies("JSESSIONID")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/member").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/member").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/member").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/category").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/category").authenticated()
                        .requestMatchers("/api/category/admin").hasRole("ADMIN")
                        .requestMatchers("/api/member/login", "/api/reissue").permitAll() // reissue 허용
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"인증이 필요합니다\"}");
                        }))
                        .accessDeniedHandler(((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"허가되지 않은 권한입니다\"}");
                        }))
                )
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenRepository), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
