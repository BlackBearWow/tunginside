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

    public Optional<Category> findByAbbr(String abbr) {
        return em.createQuery("select c from Category c where c.abbr = :abbr", Category.class)
                .setParameter("abbr", abbr).getResultList().stream().findFirst();
    }

    public boolean existByName(String name) {
        Long count = em.createQuery("select count(c) from Category c where c.name = :name", Long.class)
                .setParameter("name", name).getSingleResult();
        return (count > 0);
    }

    public boolean existsByAbbr(String abbr) {
        Long count = em.createQuery("select count(c) from Category c where c.abbr = :abbr", Long.class)
                .setParameter("abbr", abbr).getSingleResult();
        return (count > 0);
    }

    public void delete(Category category) {
        em.remove(category);
    }
}
