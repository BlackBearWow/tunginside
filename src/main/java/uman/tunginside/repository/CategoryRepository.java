package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.category.Category;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public void save(Category category) {
        em.persist(category);
    }

    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class).getResultList();
    }

    public Optional<Category> findById(long id) {
        return Optional.ofNullable(em.find(Category.class, id));
    }

    public Optional<Category> findByAbbreviation(String abbreviation) {
        return em.createQuery("select c from Category c where c.abbreviation = :abbreviation", Category.class)
                .setParameter("abbreviation", abbreviation).getResultList().stream().findFirst();
    }

    public boolean existByName(String name) {
        Long count = em.createQuery("select count(c) from Category c where c.name = :name", Long.class)
                .setParameter("name", name).getSingleResult();
        return (count > 0);
    }

    public boolean existsByAbbreviation(String abbreviation) {
        Long count = em.createQuery("select count(c) from Category c where c.abbreviation = :abbreviation", Long.class)
                .setParameter("abbreviation", abbreviation).getSingleResult();
        return (count > 0);
    }

    public void delete(Category category) {
        em.remove(category);
    }
}
