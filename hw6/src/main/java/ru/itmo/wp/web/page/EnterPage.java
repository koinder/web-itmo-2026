package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;
import ru.itmo.wp.web.annotation.Json;
import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.repository.EventRepository;
import ru.itmo.wp.model.repository.impl.EventRepositoryImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused"})
public class EnterPage extends Page {
    private final UserService userService = new UserService();
    private final EventRepository eventRepository = new EventRepositoryImpl();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    @Json
    private void enter(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        String loginOrEmail = request.getParameter("loginOrEmail");
        String password = request.getParameter("password");

        userService.validateEnter(loginOrEmail, password);
        User user = userService.findByLoginAndPassword(loginOrEmail, password);
        Event event = new Event();
        event.setUserId(user.getId());
        event.setType(Event.Type.ENTER);
        eventRepository.save(event);

        setUser(user);
        setMessage("Hello, " + user.getLogin());
        throw new RedirectException("/index");
    }
}
