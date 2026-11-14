package ru.itmo.wp.web.page;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.annotation.Json;
import ru.itmo.wp.web.exception.RedirectException;

@SuppressWarnings({"unused"})
public class UsersPage extends Page {
    private final UserService userService = new UserService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("users", userService.findAll());
        boolean canManageAdmins = false;
        if (getUser() != null) {
            User currentUser = userService.find(getUser().getId());
            canManageAdmins = currentUser != null && currentUser.isAdmin();
        }

        view.put("canManageAdmins", canManageAdmins);
    }

    @Json
    private void changeAdmin(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user = getUser();
        if (user == null) {
            throw new RedirectException("/index");
        }
        long id;
        try {
            id = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid user id");
        }
        String adminParameter = request.getParameter("admin");
        if (!"true".equals(adminParameter) && !"false".equals(adminParameter)) {
            throw new ValidationException("Invalid admin value");
        }
        boolean admin = Boolean.parseBoolean(adminParameter);
        userService.updateAdmin(user.getId(), id, admin);
        view.put("admin", admin);
    }
}
