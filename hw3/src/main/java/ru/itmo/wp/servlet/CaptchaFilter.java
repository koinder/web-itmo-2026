package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

public class CaptchaFilter extends HttpFilter {
    private static final String PASSED = "captchaPassed";
    private static final String EXPECTED = "captchaExpected";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (!"GET".equals(request.getMethod()) || session.getAttribute(PASSED) != null) {
            chain.doFilter(request, response);
            return;
        }

        String expected = (String) session.getAttribute(EXPECTED);
        String answer = request.getParameter("captcha");
        if (answer != null && answer.equals(expected)) {
            session.setAttribute(PASSED, true);
            session.removeAttribute(EXPECTED);
            response.sendRedirect(request.getRequestURI());
            return;
        }

        // Новое число: при первом показе или после неверного ответа.
        if (expected == null || answer != null) {
            expected = String.valueOf(ThreadLocalRandom.current().nextInt(100, 1000));
            session.setAttribute(EXPECTED, expected);
        }

        String image = Base64.getEncoder().encodeToString(ImageUtils.toPng(expected));
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print("<!DOCTYPE html>\n"
                + "<html><head><meta charset=\"UTF-8\"><title>Captcha</title></head><body>\n"
                + "<img src=\"data:image/png;base64," + image + "\" alt=\"captcha\">\n"
                + "<form method=\"get\">\n"
                + "    <label for=\"captcha\">Enter the number:</label>\n"
                + "    <input id=\"captcha\" name=\"captcha\" autofocus>\n"
                + "    <input type=\"submit\" value=\"Submit\">\n"
                + "</form>\n"
                + "</body></html>\n");
        writer.flush();
    }
}
