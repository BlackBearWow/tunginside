package uman.tunginside.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uman.tunginside.domain.member.Member;
import uman.tunginside.jwt.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
    Boolean existsByMember(Member member);
}
