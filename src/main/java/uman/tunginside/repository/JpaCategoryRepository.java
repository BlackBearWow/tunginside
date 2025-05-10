package uman.tunginside.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uman.tunginside.domain.Category;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaCategoryRepository {

    private final EntityManager em;

    public void save(Category category) {
        em.persist(category);
    }

    public Category findById(long id) {
        return em.find(Category.class, id);
    }

    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class).getResultList();
    }

    public void delete(Category category) {
        em.remove(category);
    }
}
