package ru.itmo.wp.web.page;

import ru.itmo.wp.web.exception.RedirectException;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.web.annotation.Json;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public class ArticlePage extends Page {
    private final ArticleService articleService = new ArticleService();
    private void action(HttpServletRequest request, Map<String, Object> view) {
        if (getUser() == null) {
            throw new RedirectException("/index");
        }
    }
    @Json
    private void create(HttpServletRequest request, Map<String, Object> view)
            throws ValidationException {
        action(request, view);

        Article article = new Article();
        article.setUserId(getUser().getId());
        article.setTitle(request.getParameter("title"));
        article.setText(request.getParameter("text"));

        articleService.save(article);

        throw new RedirectException("/index");
    }
}