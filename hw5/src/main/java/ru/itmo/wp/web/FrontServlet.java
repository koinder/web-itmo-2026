package ru.itmo.wp.web;

import freemarker.template.*;
import ru.itmo.wp.web.exception.NotFoundException;
import ru.itmo.wp.web.exception.RedirectException;
import ru.itmo.wp.web.page.IndexPage;
import ru.itmo.wp.web.page.NotFoundPage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FrontServlet extends HttpServlet {
    private static final String BASE_PACKAGE = FrontServlet.class.getPackage().getName() + ".page";
    private static final String DEFAULT_ACTION = "action";

    private Configuration sourceConfiguration;
    private Configuration targetConfiguration;

    private Configuration newFreemarkerConfiguration(String templateDirName, boolean debug)
            throws ServletException {
        File templateDir = new File(templateDirName);
        if (!templateDir.isDirectory()) {
            return null;
        }

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        try {
            configuration.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            throw new ServletException("Can't create freemarker configuration [templateDir="
                    + templateDir + "]");
        }
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setTemplateExceptionHandler(debug ? TemplateExceptionHandler.HTML_DEBUG_HANDLER :
                TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        
        configuration.setLocalizedLookup(false);
        // Без этого FreeMarker сам подставит IndexPage_ru.ftlh по языку операционной системы.

        return configuration;
    }

    @Override
    public void init() throws ServletException {
        sourceConfiguration = newFreemarkerConfiguration(
                getServletContext().getRealPath("/") + "../../src/main/webapp/WEB-INF/templates", true);
        targetConfiguration = newFreemarkerConfiguration(
                getServletContext().getRealPath("WEB-INF/templates"), false);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        saveLanguage(request);
        Route route = Route.newRoute(request);
        try {
            process(route, request, response);
        } catch (NotFoundException e) {
            try {
                process(Route.newNotFoundRoute(), request, response);
            } catch (NotFoundException notFoundException) {
                throw new ServletException(notFoundException);
            }
        }
    }

    private void process(Route route, HttpServletRequest request, HttpServletResponse response)
            throws NotFoundException, ServletException, IOException {
        Class<?> pageClass;
        try {
            pageClass = Class.forName(route.getClassName());
        } catch (ClassNotFoundException e) {
            throw new NotFoundException();
        }

        Method method = findActionMethod(pageClass, route.getAction());
        if (method == null) {
            throw new NotFoundException();
        }

        Object page;
        try {
            page = pageClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                 | NoSuchMethodException | InvocationTargetException e) {
            throw new ServletException("Can't create page [pageClass="
                    + pageClass + "]");
        }

        Map<String, Object> view = new HashMap<>();
        method.setAccessible(true);
        try {
            method.invoke(page, newArguments(method, request, view));
        } catch (IllegalAccessException e) {
            throw new ServletException("Can't invoke action method [pageClass="
                    + pageClass + ", method=" + method + "]");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RedirectException) {
                RedirectException redirectException = (RedirectException) cause;
                response.sendRedirect(redirectException.getTarget());
                return;
            } else {
                throw new ServletException("Can't invoke action method [pageClass="
                        + pageClass + ", method=" + method + "]", cause);
            }
        }

        Template template = newTemplate(pageClass.getSimpleName(), getLanguage(request));
        response.setContentType("text/html");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            template.process(view, response.getWriter());
        } catch (TemplateException e) {
            if (sourceConfiguration == null) {
                throw new ServletException("Can't render template [pageClass="
                        + pageClass + ", action=" + method + "]", e);
            }
        }
    }

    private Method findActionMethod(Class<?> pageClass, String actionName) {
        // выделил в отдельный метод
        // Object пропуск, на сайте его по сути нету, и смысла его вызывать тоже
        for (Class<?> clazz = pageClass; clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
            Method method = findInClass(clazz, actionName);
            if (method != null) {
                return method;
            }
        }

        // Интерфейсы страницы и лесенка родителей родителей
        for (Class<?> clazz = pageClass; clazz != null; clazz = clazz.getSuperclass()) {
            Method method = findInInterfaces(clazz, actionName);
            if (method != null) {
                return method;
            }
        }
        return null;
    }

    private Method findInInterfaces(Class<?> clazz, String actionName) {
        for (Class<?> anInterface : clazz.getInterfaces()) {
            Method method = findInClass(anInterface, actionName);
            if (method == null) {
                method = findInInterfaces(anInterface, actionName);   // интерфейсы самого интерфейса :)
            }
            if (method != null) {
                return method;
            }
        }
        return null;
    }

    private Method findInClass(Class<?> clazz, String actionName) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(actionName)
                    && !Modifier.isStatic(method.getModifiers())
                    && hasSupportedParameters(method)) {
                return method;
            }
        }
        return null;
    }

    private boolean hasSupportedParameters(Method method) {
        for (Class<?> type : method.getParameterTypes()) {
            if (type != HttpServletRequest.class && type != Map.class) {
                return false;
            }
        }
        return true;
    }

    private Object[] newArguments(Method method, HttpServletRequest request, Map<String, Object> view) {
    // вынес для удобства проверки на то что это класс, и правильный порядок стави в параметрах метода который мы применяем
        Class<?>[] types = method.getParameterTypes();
        Object[] arguments = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            if (types[i] == HttpServletRequest.class) {
                arguments[i] = request;
            } else {
                arguments[i] = view;
            }
        }
        return arguments;
    }

    // для сессии запоминаем lang
    private void saveLanguage(HttpServletRequest request) {
        String lang = request.getParameter("lang");
        if (lang != null && lang.matches("[a-z]{2}")) {
            request.getSession().setAttribute("lang", lang);
        }
    }

    private String getLanguage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);   // false: не создаём сессию только ради чтения
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute("lang");
    }

    // Если язык задан, ищем IndexPage_ru.ftlh. Иначе(нету файла или en) ищем IndexPage.ftlh.
    private Template newTemplate(String pageName, String lang) throws ServletException {
        Template template = null;

        if (lang != null && !lang.equals("en")) {
            template = findTemplate(pageName + "_" + lang + ".ftlh");
        }

        if (template == null) {
            template = findTemplate(pageName + ".ftlh");
        }

        if (template == null) {
            throw new ServletException("Can't find template [pageName=" + pageName + "]");
        }

        return template;
    }

    // Ищет шаблон по точному имени: сначала в исходниках, потом в собранном приложении. Нигде нет: null.
    private Template findTemplate(String templateName) throws ServletException {
        Template template = null;

        if (sourceConfiguration != null) {
            try {
                template = sourceConfiguration.getTemplate(templateName);
            } catch (TemplateNotFoundException ignored) {
                // No operations.
            } catch (IOException e) {
                throw new ServletException("Can't load template [templateName=" + templateName + "]", e);
            }
        }

        if (template == null && targetConfiguration != null) {
            try {
                template = targetConfiguration.getTemplate(templateName);
            } catch (TemplateNotFoundException ignored) {
                // No operations.
            } catch (IOException e) {
                throw new ServletException("Can't load template [templateName=" + templateName + "]", e);
            }
        }

        return template;
    }

    private static class Route {
        private final String className;
        private final String action;

        private Route(String className, String action) {
            this.className = className;
            this.action = action;
        }

        private String getClassName() {
            return className;
        }

        private String getAction() {
            return action;
        }

        private static Route newNotFoundRoute() {
            return new Route(NotFoundPage.class.getName(), DEFAULT_ACTION);
        }

        private static Route newIndexRoute() {
            return new Route(IndexPage.class.getName(), DEFAULT_ACTION);
        }

        private static Route newRoute(HttpServletRequest request) {
            String uri = request.getRequestURI();

            List<String> classNameParts = Arrays.stream(uri.split("/"))
                    .filter(part -> !part.isEmpty())
                    .collect(Collectors.toList());

            if (classNameParts.isEmpty()) {
                return newIndexRoute();
            }

            StringBuilder simpleClassName = new StringBuilder(classNameParts.get(classNameParts.size() - 1));
            int lastDotIndex = simpleClassName.lastIndexOf(".");
            simpleClassName.setCharAt(lastDotIndex + 1,
                    Character.toUpperCase(simpleClassName.charAt(lastDotIndex + 1)));
            classNameParts.set(classNameParts.size() - 1, simpleClassName.toString());

            String className = BASE_PACKAGE + "." + String.join(".", classNameParts) + "Page";

            String action = request.getParameter("action");
            if (action == null || action.isEmpty()) {
                action = DEFAULT_ACTION;
            }

            return new Route(className, action);
        }
    }
}
