package org.jhaws.common.web.servlets;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

@SuppressWarnings("serial")
public abstract class NoCacheServlet extends HttpServlet {
    protected static final int DEFAULT_BUFFER_SIZE = 2 << 12; // ..bytes = 8KB.

    public static String decode(String name) {
        try {
            return URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static String encode(String name) {
        try {
            return URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Process HEAD request. This returns the same headers as GET request, but without content.
     *
     * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse).
     */
    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Process request without content.
        processRequest(request, response, false);
    }

    /**
     * Process GET request.
     *
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Process request with content.
        processRequest(request, response, true);
    }

    /**
     * Process the actual request.
     *
     * @param request The request to be processed.
     * @param response The response to be created.
     * @param content Whether the request body should be written (GET) or not (HEAD).
     * @throws IOException If something fails at I/O level.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, boolean content) throws ServletException, IOException {
        // Get requested file by path info.
        String requestedFile = request.getPathInfo();
        requestedFile = request.getRequestURI().replaceFirst(request.getContextPath(), "").replaceFirst("/nocache", "");

        // Check if file is actually supplied to the request URL.
        if (requestedFile == null) {
            // Do your thing if the file is not supplied to the request URL.
            // Throw an exception, or send 404, or show default/warning page, or
            // just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND, requestedFile);
            return;
        }

        // url decodede filename (can contain '/')
        String relativeFileName = decode(requestedFile);
        if (relativeFileName.startsWith("/")) {
            relativeFileName = relativeFileName.substring(1);
        }
        // filename without relative path
        String shortFileName = relativeFileName.contains("/") ? relativeFileName.substring(relativeFileName.lastIndexOf('/') + 1) : relativeFileName;

        Path file;
        try {
            file = getStream(relativeFileName);
        } catch (IOException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, relativeFileName);
            return;
        }

        // Check if file actually exists in filesystem.
        if (!Files.exists(file)) {
            // Do your thing if the file appears to be non-existing.
            // Throw an exception, or send 404, or show default/warning page, or
            // just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND, relativeFileName);
            return;
        }

        // Prepare some variables.
        long length = Files.size(file);
        long lastModified = Files.getLastModifiedTime(file).toMillis();
        long expires = 0;

        // Prepare and initialize response
        // --------------------------------------------------------

        // Get content type by file name and set default GZIP support and
        // content disposition.
        String contentType = getServletContext().getMimeType(shortFileName);
        boolean acceptsGzip = false;
        String disposition = "inline";

        // If content type is unknown, then set the default value.
        // For all content types, see:
        // http://www.w3schools.com/media/media_mimeref.asp
        // To add new content types, add new mime-mapping entry in web.xml.
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // If content type is text, then determine whether GZIP content encoding
        // is supported by
        // the browser and expand content type with the one and right character
        // encoding.
        if (contentType.startsWith("text")) {
            String acceptEncoding = request.getHeader("Accept-Encoding");
            acceptsGzip = acceptEncoding != null && accepts(acceptEncoding, "gzip");
            contentType += ";charset=" + "UTF-8";
        }

        // Else, expect for images, determine content disposition. If content
        // type is supported by
        // the browser, then set to inline, else attachment which will pop a
        // 'save as' dialogue.
        else if (!contentType.startsWith("image")) {
            String accept = request.getHeader("Accept");
            disposition = accept != null && accepts(accept, contentType) ? "inline" : "attachment";
        }

        // Initialize response.
        response.reset();
        response.setBufferSize(getBufferSize());
        response.setHeader("Content-Disposition", disposition + ";filename=\"" + shortFileName + "\"");
        response.setDateHeader("Last-Modified", lastModified);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", expires);

        // Send requested file to client
        // ------------------------------------------------

        // Prepare streams.
        InputStream input = null;
        OutputStream output = null;

        try {
            // Open streams.
            input = Files.newInputStream(file);
            output = response.getOutputStream();

            // Return full file.
            response.setContentType(contentType);
            if (content) {
                if (acceptsGzip) {
                    // The browser accepts GZIP, so GZIP the content.
                    response.setHeader("Content-Encoding", "gzip");
                    output = new GZIPOutputStream(output, getBufferSize());
                } else {
                    // Content length is not directly predictable in case of
                    // GZIP.
                    // So only add it if there is no means of GZIP, else
                    // browser will hang.
                    response.setHeader("Content-Length", String.valueOf(length));
                }

                // Copy full range
                IOUtils.copy(input, output);
            }
        } finally {
            // Gently close streams.
            close(output);
            close(input);
        }
    }

    /**
     * Close the given resource.
     *
     * @param resource The resource to be closed.
     */
    protected void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException ignore) {
                // Ignore IOException. If you want to handle this anyway, it
                // might be useful to know
                // that this will generally only be thrown when the client
                // aborted the request.
            }
        }
    }

    protected int getBufferSize() {
        return DEFAULT_BUFFER_SIZE;
    }

    /**
     * Returns true if the given accept header accepts the given value.
     *
     * @param acceptHeader The accept header.
     * @param toAccept The value to be accepted.
     * @return True if the given accept header accepts the given value.
     */
    protected boolean accepts(String acceptHeader, String toAccept) {
        String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
        Arrays.sort(acceptValues);
        return Arrays.binarySearch(acceptValues, toAccept) > -1 || Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1 || Arrays.binarySearch(acceptValues, "*/*") > -1;
    }

    protected abstract Path getStream(String requestedFile) throws UnsupportedEncodingException, IOException;

    /**
     * Initialize the servlet.
     *
     * @see HttpServlet#init().
     */
    @Override
    public void init() throws ServletException {
        //
    }
}
