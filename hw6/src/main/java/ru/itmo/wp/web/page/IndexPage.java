package ru.itmo.wp.web.page;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.annotation.Json;

@SuppressWarnings({"unused"})
public class IndexPage extends Page {
    private final ArticleService articleService = new ArticleService();
    private final UserService userService = new UserService();
    private void action(HttpServletRequest request, Map<String, Object> view) {
    }
    @Json
    private void articles(HttpServletRequest request, Map<String, Object> view) {
        view.put("articles", articleService.findAll());

        Map<String, String> logins = new HashMap<>();
        for (User user : userService.findAll()) {
            logins.put(Long.toString(user.getId()), user.getLogin());
        }

        view.put("logins", logins);
    }
}
