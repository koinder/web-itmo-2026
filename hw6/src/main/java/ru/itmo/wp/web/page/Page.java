package ru.itmo.wp.web.page;

import javax.servlet.http.HttpServletRequest;

import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.model.domain.User;

import java.util.Map;
import com.google.common.base.Strings;

public class Page {
    private final UserService userService = new UserService();
    private HttpServletRequest request;

    public void before(HttpServletRequest request, Map<String, Object> view) {
        this.request = request;
        view.put("userCount", userService.findCount());
        User user = getUser();
        if (user != null) {
            view.put("user", user);
        }
        if ("GET".equals(request.getMethod())) {
            putMessage(request, view);
        }
    }

    public void after(HttpServletRequest request, Map<String, Object> view) {

    }

    private void putMessage(HttpServletRequest request, Map<String, Object> view) {
        String message = (String) request.getSession().getAttribute("message");

        if (!Strings.isNullOrEmpty(message)) {
            view.put("message", message);
            request.getSession().removeAttribute("message");
        }
    }

    protected void setMessage(String message) {
        request.getSession().setAttribute("message", message);
    }

    protected User getUser() {
        return (User) request.getSession().getAttribute("user");
    }

    protected void setUser(User user) {
        request.getSession().setAttribute("user", user);
    }

    protected void removeUser() {
        request.getSession().removeAttribute("user");
    }
}