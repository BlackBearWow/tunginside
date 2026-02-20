package uman.tunginside.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uman.tunginside.domain.member.MemberRole;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// jwt 생성, 검증 클래스
@Component
public class JWTUtil {
    private final SecretKey secretKey;
    public final Long accessTokenExpiredMs = 1000*60*60L;
    public final Long refreshTokenExpiredMs = 1000*60*60*24L;
    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    public Long getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }
    public String getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userid", String.class);
    }
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }
    // 사실 ExpiredJwtException이 터지므로 사용할 필요 없음
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }
    public String createJwtAccessToken(Long id, String userid, MemberRole memberRole) {
        return Jwts.builder()
                .claim("id", id)
                .claim("userid", userid)
                .claim("role", memberRole.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiredMs))
                .signWith(secretKey)
                .compact();
    }
    public String createJwtRefreshToken(Long id, String userid, MemberRole memberRole) {
        return Jwts.builder()
                .claim("id", id)
                .claim("userid", userid)
                .claim("role", memberRole.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiredMs))
                .signWith(secretKey)
                .compact();
    }
}
