package uman.tunginside.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static uman.tunginside.domain.category.QCategory.category;
import static uman.tunginside.domain.post.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
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
