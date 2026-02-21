package uman.tunginside.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import uman.tunginside.domain.member.Member;
import uman.tunginside.domain.member.MemberRole;
import uman.tunginside.exception.BadRequestException;
import uman.tunginside.security.MemberContext;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 로그인이나 재발급 경로는 토큰 검사를 하지 않고 바로 다음 필터로 넘김
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/api/member/login") || requestURI.equals("/api/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorization = request.getHeader("authorization");
        // 헤더 검증. 토큰이 아예 없는 경우 -> 익명 사용자이므로 통과
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorization.split(" ")[1];
        try {
            // 소멸 시간 검증은 따로 안해도 된다. 예외가 터진다.
            // 토큰에서 값 획득
            Long id = jwtUtil.getId(token);
            String userid = jwtUtil.getUserId(token);
            MemberRole memberRole = MemberRole.valueOf(jwtUtil.getRole(token));

            // 세션에 저장할 정보 만들기
            Member member = new Member();
            member.setForStatelessSession(id, userid, memberRole);
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().toString()));
            MemberContext memberContext = new MemberContext(member, authorities);
            // 스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(memberContext, null, memberContext.getAuthorities());
            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch(ExpiredJwtException e) {
            log.info("jwt 토큰 시간 만료");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"access token expired\"}");
            // 여기서 filterChain.doFilter를 호출하지 않고 끝낸다. 익명 사용자와 jwt토큰 만료 사용자를 구분하기 위해.
        } catch (SignatureException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"유효하지 않은 토큰\"}");
        }
    }
}
