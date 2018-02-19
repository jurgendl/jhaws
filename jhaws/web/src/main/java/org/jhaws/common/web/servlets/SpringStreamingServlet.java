package org.jhaws.common.web.servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringStreamingServlet extends StreamingServlet {
    private static final long serialVersionUID = 4914047812048630486L;

    protected transient WebApplicationContext webApplicationContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
    }

    protected Map<String, StreamingService> streamingServices;

    protected Map<String, StreamingService> getStreamingServicesService() {
        if (streamingServices == null) {
            streamingServices = webApplicationContext.getBeansOfType(StreamingService.class);
        }
        return this.streamingServices;
    }

    @Override
    protected StreamingSource getStream(String requestedFile) throws UnsupportedEncodingException, IOException {
        for (StreamingService streamingService : getStreamingServicesService().values()) {
            if (requestedFile.startsWith(streamingService.getPath())) {
                String relative = requestedFile.replaceFirst(streamingService.getPath(), "");
                return streamingService.getStream(relative);
            }
        }
        throw new IOException(requestedFile);
    }
}
