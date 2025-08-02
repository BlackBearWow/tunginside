package uman.tunginside.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uman.tunginside.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query("select m from Member m where m.id = :id")
    public Optional<Member> findByIdNullable(@Param("id") Long id);

    public Optional<Member> findByUserid(String userid);

    public boolean existsByUserid(String userid);

    public boolean existsByNickname(String nickname);
}
