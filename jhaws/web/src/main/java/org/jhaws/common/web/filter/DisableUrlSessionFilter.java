package org.jhaws.common.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

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
 * @see https://randomcoder.org/articles/jsessionid-considered-harmful
 */
public class DisableUrlSessionFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.isRequestedSessionIdFromURL()) {
            HttpSession session = httpRequest.getSession();
            if (session != null)
                session.invalidate();
        }

        HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(httpResponse) {
            public String encodeRedirectUrl(String url) {
                return url;
            }

            public String encodeRedirectURL(String url) {
                return url;
            }

            public String encodeUrl(String url) {
                return url;
            }

            public String encodeURL(String url) {
                return url;
            }
        };

        chain.doFilter(request, wrappedResponse);
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }
}