package uman.tunginside.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.member.MemberRole;
import uman.tunginside.repository.RefreshTokenRepository;
import uman.tunginside.security.MemberContext;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/api/member/login");
        setUsernameParameter("userid");
    }

    // UserDetailsService를 상속한 서비스의 loadUserByUsername메소드를 구현해놔야 한다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공시 실행 메소드(jwt 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        MemberContext memberContext = (MemberContext) authResult.getPrincipal();
        Member member = memberContext.getMember();
        String userid = memberContext.getUsername();
        MemberRole memberRole = memberContext.getMemberRole();
        String accessToken = jwtUtil.createJwtAccessToken(memberContext.getId(), userid, memberRole);
        String refreshTokenValue = jwtUtil.createJwtRefreshToken(memberContext.getId(), memberContext.getUsername(), memberContext.getMemberRole());
        // db에 refreshtoken저장 (기존 토큰 있으면 업데이트, 없으면 신규 저장)
        RefreshToken refreshToken = refreshTokenRepository.findByMember(member)
                .map(token -> {
                    token.updateToken(refreshTokenValue, jwtUtil.refreshTokenExpiredMs);
                    return token;
                })
                .orElse(new RefreshToken(member, refreshTokenValue, jwtUtil.refreshTokenExpiredMs));
        refreshTokenRepository.save(refreshToken);
        // RFC 7235 정의 방식
        response.addHeader("authorization", "Bearer " + accessToken);
        // Refresh Token은 보안을 위해 HttpOnly 쿠키에 담는 것을 추천합니다.
        Cookie cookie = new Cookie("refreshToken", refreshTokenValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24); // 쿠키 수명
        // cookie.setSecure(true); // HTTPS 환경이라면 활성화
        response.addCookie(cookie);

        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"로그인 성공\"}");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"로그인 실패: "+exception.getMessage()+"\"}");
    }
}
