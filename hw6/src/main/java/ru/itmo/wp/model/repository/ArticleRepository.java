package ru.itmo.wp.model.repository;

import java.util.List;

import ru.itmo.wp.model.domain.Article;

public interface ArticleRepository {
    void save(Article article);
    List<Article> findAll();
    List<Article> findByUserId(long userId);
    void updateHidden(long id, long userId, boolean hidden);
    Article find(long id);
}