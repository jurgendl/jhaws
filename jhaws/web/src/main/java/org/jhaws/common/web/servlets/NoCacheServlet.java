package org.jhaws.common.web.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public abstract class NoCacheServlet extends HttpServlet {
    private static final long serialVersionUID = -6607610686532682909L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestedFile = request.getPathInfo();
        requestedFile = request.getRequestURI().replaceFirst(request.getContextPath(), "").replaceFirst("/nocache", "");
        String relativeFileName = StreamingServlet.decode(requestedFile);
        if (relativeFileName.startsWith("/")) {
            relativeFileName = relativeFileName.substring(1);
        }
        String shortFileName = relativeFileName.contains("/") ? relativeFileName.substring(relativeFileName.lastIndexOf('/') + 1) : relativeFileName;
        Path file = getStream(relativeFileName);
        if (!Files.exists(file)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, relativeFileName);
            return;
        }
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        ServletOutputStream output = response.getOutputStream();
        InputStream input = Files.newInputStream(file);
        IOUtils.copy(input, output);
        input.close();
        output.close();
    }

    protected abstract Path getStream(String requestedFile) throws UnsupportedEncodingException, IOException;
}
