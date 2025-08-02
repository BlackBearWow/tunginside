package uman.tunginside.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uman.tunginside.domain.category.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByAbbr(String abbr);
    public boolean existsByName(String name);
    public boolean existsByAbbr(String abbr);
}
