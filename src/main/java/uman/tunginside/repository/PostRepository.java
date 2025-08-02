package uman.tunginside.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.category.QCategory;
import uman.tunginside.domain.post.*;
import uman.tunginside.domain.post.QPost;

import java.util.List;
import java.util.Optional;

import static uman.tunginside.domain.category.QCategory.*;
import static uman.tunginside.domain.post.QPost.*;

public interface PostRepository extends JpaRepository<Post,Long> , PostRepositoryCustom{

}
