package uman.tunginside.repository;

import uman.tunginside.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    void save(Category category);
    List<Category> findAll();
    Optional<Category> findById(long id);
    Optional<Category> findByAbbreviation(String abbreviation);
    boolean existByName(String name);
    boolean existsByAbbreviation(String abbreviation);
    void delete(Category category);
}
