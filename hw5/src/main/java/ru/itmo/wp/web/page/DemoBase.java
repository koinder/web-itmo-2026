package ru.itmo.wp.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


// Предок страницы DemoPage
@SuppressWarnings({"unused", "RedundantSuppression"})
public abstract class DemoBase implements Greeter {
    // Действие по умолчанию.
    private void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "action(request, view): объявлен в предке DemoBase");
    }

    // Параметры в обратном порядке: сначала Map, потом HttpServletRequest.
    protected void fromAncestor(Map<String, Object> view, HttpServletRequest request) {
        view.put("message", "fromAncestor(view, request): объявлен в предке DemoBase, адрес запроса "
                + request.getRequestURI());
    }
}
