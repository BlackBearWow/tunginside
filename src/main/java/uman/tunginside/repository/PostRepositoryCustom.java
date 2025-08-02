package uman.tunginside.repository;

public interface PostRepositoryCustom {
    public Long countByCondition(String abbr, Integer likeCut, String search);
}
