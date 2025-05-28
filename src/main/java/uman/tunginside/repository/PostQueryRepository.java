package uman.tunginside.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.member.QMember;
import uman.tunginside.domain.post.Post;
import uman.tunginside.domain.post.PostDetailDTO;
import uman.tunginside.domain.post.PostOrderby;
import uman.tunginside.domain.post.PostSummaryDTO;

import java.util.List;
import java.util.Optional;

import static uman.tunginside.domain.category.QCategory.category;
import static uman.tunginside.domain.member.QMember.*;
import static uman.tunginside.domain.post.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public List<PostSummaryDTO> findByCondition(String abbr, Integer page, Integer likeCut, String search, Integer size, PostOrderby orderby) {
        JPAQuery<PostSummaryDTO> query = queryFactory.select(Projections.constructor(PostSummaryDTO.class, post.id, category.name, member.nickname, post.title, post.ip_addr, post.create_at, post.last_modified_at, post.post_like_count, post.comment_count, post.view_count))
                .from(post)
                .innerJoin(post.category, category)
                .leftJoin(post.member, member)
                .where(abbrEq(abbr), likeCutGoe(likeCut), searchLike(search));
        if (orderby != null) {
            query = switch (orderby) {
                case PostOrderby.LIKE -> query.orderBy(post.post_like_count.desc(), post.create_at.desc());
                case PostOrderby.DISLIKE -> query.orderBy(post.post_dislike_count.desc(), post.create_at.desc());
                case PostOrderby.VIEWCOUNT -> query.orderBy(post.view_count.desc(), post.create_at.desc());
            };
        }
        return query.offset((page - 1) * size)
                .limit(size)
                .fetch();
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

    public Optional<PostDetailDTO> findDetailById(Long postId) {
        return em.createQuery("select p.id, c.name, c.abbr, m.nickname, p.ip_addr, p.last_modified_ip, p.create_at, p.last_modified_at, p.title, p.content, p.post_like_count, p.post_dislike_count, p.comment_count, p.view_count from Post p join p.category c on p.id = :postId left join p.member m", PostDetailDTO.class)
                .setParameter("postId", postId).getResultList().stream().findFirst();
    }
}
