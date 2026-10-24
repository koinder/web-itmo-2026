package ru.itmo.wp.web.page;

import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class DemoPage extends DemoBase {
    // Метод самой страницы, единственный параметр Map.
    private void ownAction(Map<String, Object> view) {
        view.put("message", "ownAction(view): объявлен в самой DemoPage");
    }

    private void requestOnly(HttpServletRequest request) {
        throw new RedirectException("/demo?action=ownAction");
    }
}
