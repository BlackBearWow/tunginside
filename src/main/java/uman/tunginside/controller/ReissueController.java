package uman.tunginside.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import uman.tunginside.domain.member.Member;
import uman.tunginside.exception.BadRequestException;
import uman.tunginside.jwt.JWTUtil;
import uman.tunginside.jwt.RefreshToken;
import uman.tunginside.repository.MemberRepository;
import uman.tunginside.repository.RefreshTokenRepository;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReissueController {
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/api/reissue")
    public String reissue(HttpServletRequest request, HttpServletResponse response) {

        // 1. Get refresh token from cookies
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    refresh = cookie.getValue();
                }
            }
        }

        // 2. 검증: 쿠키에 토큰이 없는 경우
        if (refresh == null) {
            throw new BadRequestException("refresh token null");
        }

        try {
            // DB에 저장되어 있는지 확인 (가장 중요한 보안 단계)
            Long member_id = jwtUtil.getId(refresh);
            Member member = memberRepository.findById(member_id).orElseThrow(()->new BadRequestException("멤버를 찾을 수 없음"));
            Boolean isExist = refreshTokenRepository.existsByMember(member);
            if (!isExist) {
                log.info("refresh token이 존재하지 않는다잉 {} {}", isExist, member.getId());
                throw new BadRequestException("해당하는 refresh token이 존재하지 않습니다~!!");
            }

            // 새로운 access token, refresh token 발급
            // 실제 운영 시에는 id와 role 정보도 토큰에서 추출하거나 DB에서 조회해야 합니다.
            String newAccess = jwtUtil.createJwtAccessToken(member.getId(), member.getUserid(), member.getRole());
            String newRefresh = jwtUtil.createJwtRefreshToken(member.getId(), member.getUserid(), member.getRole());

            // refreshtoken db에 업데이트
            RefreshToken refreshToken = refreshTokenRepository.findByMember(member)
                    .orElseThrow(() -> new BadRequestException("토큰 정보를 찾을 수 없습니다."));
            refreshToken.updateToken(newRefresh, jwtUtil.refreshTokenExpiredMs);
            refreshTokenRepository.save(refreshToken);

            // 응답 설정
            response.setHeader("authorization", "Bearer " + newAccess);

            Cookie cookie = new Cookie("refreshToken", newRefresh);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24); // 쿠키 수명
            // cookie.setSecure(true); // HTTPS 환경이라면 활성화
            response.addCookie(cookie);
        } catch (ExpiredJwtException e) {
            // 만료 체크
            throw new BadRequestException("refresh 토큰이 만료되었습니다. 다시 로그인해주세요");
        } catch (SignatureException | MalformedJwtException e) {
            throw new BadRequestException("유효하지 않은 토큰입니다. 다시 로그인해주세요");
        }
        return "refresh토큰으로 새 accesstoken 발급 완료";
    }
}
