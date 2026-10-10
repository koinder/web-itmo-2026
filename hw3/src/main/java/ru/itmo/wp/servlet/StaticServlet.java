package ru.itmo.wp.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class StaticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<File> files = new ArrayList<>();
        for (String uri : request.getRequestURI().split("\\+")) {
            if (!uri.startsWith("/")) {
                uri = "/" + uri;
            }
            File file = findFile(uri);
            if (file == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            files.add(file);
        }

        response.setContentType(getServletContext().getMimeType(files.get(0).getName()));
        try (OutputStream outputStream = response.getOutputStream()) {
            for (File file : files) {
                Files.copy(file.toPath(), outputStream);
            }
        }
    }

    private File findFile(String uri) throws IOException {
        File srcDir = new File(getServletContext().getRealPath("/"), "../../src/main/webapp/static");
        File file = findInDir(srcDir, uri);
        if (file == null) {
            file = findInDir(new File(getServletContext().getRealPath("/static")), uri);
        }
        return file;
    }

    private File findInDir(File dir, String uri) throws IOException {
        File file = new File(dir, uri);
        String dirPath = dir.getCanonicalPath() + File.separator;
        if (file.isFile() && file.getCanonicalPath().startsWith(dirPath)) {
            return file;
        }
        return null;
    }
}
