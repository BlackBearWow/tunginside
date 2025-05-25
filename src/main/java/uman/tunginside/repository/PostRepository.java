package uman.tunginside.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.category.QCategory;
import uman.tunginside.domain.post.*;
import uman.tunginside.domain.post.QPost;

import java.util.List;
import java.util.Optional;

import static uman.tunginside.domain.category.QCategory.*;
import static uman.tunginside.domain.post.QPost.*;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void save(Post post) {
        em.persist(post);
    }

    public void remove(Long postId) {
        Post post = em.find(Post.class, postId);
        em.remove(post);
    }

    public Optional<Post> findById(long id) {
        return Optional.ofNullable(em.find(Post.class, id));
    }

    public List<Post> findByCondition(String abbr, Integer page, Integer likeCut, String search) {
        return queryFactory.selectFrom(post)
                .innerJoin(post.category, category)
                .where(abbrEq(abbr), likeCutGoe(likeCut), searchLike(search))
                .offset((page - 1) * 20)
                .limit(20)
                .fetch();
    }

    public Long countByCondition(String abbr, Integer likeCut, String search) {
        return queryFactory.select(post.count())
                .from(post)
                .innerJoin(post.category, category)
                .where(abbrEq(abbr), likeCutGoe(likeCut), searchLike(search))
                .fetchOne();
    }

    private BooleanExpression searchLike(String search) {
        return search == null ? null : post.title.contains(search).or(post.content.contains(search));
    }

    private BooleanExpression likeCutGoe(Integer likeCut) {
        return likeCut == null ? null : post.post_like_count.goe(likeCut);
    }

    private BooleanExpression abbrEq(String abbr) {
        return abbr == null ? null : category.abbr.eq(abbr);
    }
}
