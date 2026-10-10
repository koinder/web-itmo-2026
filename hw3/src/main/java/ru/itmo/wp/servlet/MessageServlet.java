package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageServlet extends HttpServlet {
    private static final int MAX_LENGTH = 200;

    private final List<Message> messages = new CopyOnWriteArrayList<>();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String action = request.getPathInfo();

        Object result;
        if ("/auth".equals(action)) {
            String user = clean(request.getParameter("user"));
            if (user != null) {
                session.setAttribute("user", user);
            }
            Object currentUser = session.getAttribute("user");
            result = currentUser != null ? currentUser : "";
        } else if ("/findAll".equals(action)) {
            result = messages;
        } else if ("/add".equals(action)) {
            String user = (String) session.getAttribute("user");
            String text = clean(request.getParameter("text"));
            if (user == null || text == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            messages.add(new Message(user, text));
            result = true;
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(gson.toJson(result));
        response.getWriter().flush();
    }

    // Обрезает пробелы; пустую или слишком длинную строку считает отсутствующей.
    private static String clean(String value) {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.isEmpty() || value.length() > MAX_LENGTH) {
            return null;
        }
        return value;
    }

    private static class Message {
        private final String user;
        private final String text;

        private Message(String user, String text) {
            this.user = user;
            this.text = text;
        }
    }
}
