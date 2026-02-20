package uman.tunginside.jwt;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import uman.tunginside.domain.member.Member;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Member member;
    @Column(nullable = false, length = 500)
    private String tokenValue;
    @NotNull
    private LocalDateTime expirationDate;

    public RefreshToken(Member member, String tokenValue, Long expiredMs) {
        this.member = member;
        this.tokenValue = tokenValue;
        this.expirationDate = LocalDateTime.now().plus(Duration.ofMillis(expiredMs));
    }
    // 토큰 갱신 시 값만 변경하기 위한 메서드
    public void updateToken(String newToken, Long expiredMs) {
        this.tokenValue = newToken;
        this.expirationDate = LocalDateTime.now().plus(Duration.ofMillis(expiredMs));
    }
}
