package ru.itmo.wp.model.service;

import java.util.List;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;


public class ArticleService {
    private final ArticleRepository articleRepository =
            new ArticleRepositoryImpl();

    public void validate(Article article) throws ValidationException {
        String title = article.getTitle();
        String text = article.getText();

        if (title == null || title.isBlank()) {
            throw new ValidationException("Title is required");
        }
        if (title.length() > 255) {
            throw new ValidationException("Title is too long");
        }

        if (text == null || text.isBlank()) {
            throw new ValidationException("Article text is required");
        }
        if (text.length() > 10000) {
            throw new ValidationException("Article text is too long");
        }
    }

    public void save(Article article) throws ValidationException {
        validate(article);
        articleRepository.save(article);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findByUserId(long userId) {
        return articleRepository.findByUserId(userId);
    }

    public void updateHidden(long id, long userId, boolean hidden) throws ValidationException {
        Article article = articleRepository.find(id);

        if (article == null) {
            throw new ValidationException("Article does not exist");
        }

        if (article.getUserId() != userId) {
            throw new ValidationException("You can only change your own articles");
        }

        articleRepository.updateHidden(id, userId, hidden);
    }
}