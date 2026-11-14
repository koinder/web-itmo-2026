package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.web.exception.RedirectException;
import ru.itmo.wp.model.service.TalkService;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

@SuppressWarnings("unused")
public class TalksPage extends Page {
    private final TalkService talkService = new TalkService();
    private final UserService userService = new UserService();
    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = getUser();

        if (user == null) {
            throw new RedirectException("/index");
        }
        List<User> users = userService.findAll();
        Map<String, String> logins = new HashMap<>();
        for (User u : users) {
            logins.put(Long.toString(u.getId()), u.getLogin());
        }

        view.put("logins", logins);
        view.put("users", users);
        view.put("talks", talkService.findByUserId(user.getId()));
    }
    private void send(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        action(request, view);
        User user = getUser();
        String targetUserId = request.getParameter("targetUserId");
        String text = request.getParameter("text");

        Talk talk = new Talk();
        talk.setSourceUserId(user.getId());
        talk.setText(text);
        try {
            talk.setTargetUserId(Long.parseLong(targetUserId));
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid recipient");
        }
        
        talkService.save(talk);
        throw new RedirectException("/talks");
    }
}