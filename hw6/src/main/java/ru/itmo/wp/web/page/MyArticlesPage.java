package ru.itmo.wp.web.page;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.annotation.Json;
import ru.itmo.wp.web.exception.RedirectException;

@SuppressWarnings("unused")
public class MyArticlesPage extends Page {
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = getUser();

        if (user == null) {
            throw new RedirectException("/index");
        }

        view.put("articles", articleService.findByUserId(user.getId()));
    }

    @Json
    private void changeHidden(HttpServletRequest request, Map<String, Object> view)
            throws ValidationException {
        User user = getUser();
        if (user == null) {
            throw new RedirectException("/index");
        }

        long id;
        try {
            id = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid article id");
        }

        String hiddenParameter = request.getParameter("hidden");
        if (!"true".equals(hiddenParameter) && !"false".equals(hiddenParameter)) {
            throw new ValidationException("Invalid hidden value");
        }

        boolean hidden = Boolean.parseBoolean(hiddenParameter);

        articleService.updateHidden(id, user.getId(), hidden);

        view.put("hidden", hidden);
    }
}