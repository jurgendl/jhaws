package org.jhaws.common.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * <pre>
 * <filter>
 *   <filter-name>
 *     DisableUrlSessionFilter
 *   </filter-name>
 *   <filter-class>
 *     com.randomcoder.security.DisableUrlSessionFilter
 *   </filter-class>
 * </filter>
 * <filter-mapping>
 *   <filter-name>DisableUrlSessionFilter</filter-name>
 *   <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * </pre>
 *
 * @see <a href="https://randomcoder.org/articles/jsessionid-considered-harmful">...</a>
 */
public class DisableUrlSessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.isRequestedSessionIdFromURL()) {
            HttpSession session = httpRequest.getSession();
            if (session != null) session.invalidate();
        }

        HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(httpResponse) {
            //@Override
            //public String encodeRedirectUrl(String url) {
            //    return url;
            //}

            @Override
            public String encodeRedirectURL(String url) {
                return url;
            }

            //@Override
            //public String encodeUrl(String url) {
            //    return url;
            //}

            @Override
            public String encodeURL(String url) {
                return url;
            }
        };

        chain.doFilter(request, wrappedResponse);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}