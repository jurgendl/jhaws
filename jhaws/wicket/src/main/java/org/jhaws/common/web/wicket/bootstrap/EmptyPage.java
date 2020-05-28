package org.jhaws.common.web.wicket.bootstrap;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@SuppressWarnings("serial")
public class EmptyPage extends DefaultWebPage {
    public EmptyPage(PageParameters parameters) {
        super(parameters);
    }

    private String toString(PageParameters params) {
        Map<String, Object> map = new HashMap<>();
        for (String name : params.getNamedKeys()) {
            map.put(name, params.getValues(name));
        }
        return map.toString();
    }

    private String toString(IRequestParameters params) {
        Map<String, Object> map = new HashMap<>();
        for (String name : params.getParameterNames()) {
            map.put(name, params.getParameterValue(name));
        }
        return map.toString();
    }

    @Override
    protected void addComponents(PageParameters parameters, MarkupContainer html) {
        add(new Label("PageParameters", toString(parameters)));
        add(new Label("PostParameters", toString(getPage().getRequest().getPostParameters())));
        add(new Label("QueryParameters", toString(getPage().getRequest().getQueryParameters())));
        add(new Label("RequestParameters", toString(getPage().getRequest().getRequestParameters())));
        if (getPage().getRequest() instanceof ServletWebRequest) {
            ServletWebRequest servletWebRequest = (ServletWebRequest) getPage().getRequest();
            // System.out.println(servletWebRequest.getHeader());
            System.out.println(servletWebRequest.getContextPath());
            System.out.println(servletWebRequest.getFilterPath());
            System.out.println(servletWebRequest.getFilterPrefix());
            System.out.println(servletWebRequest.getPrefixToContextPath());
            System.out.println(servletWebRequest.getCharset());
            System.out.println(servletWebRequest.getClientUrl());
            System.out.println(servletWebRequest.getContainerRequest());
            System.out.println(servletWebRequest.getCookies());
            // System.out.println(servletWebRequest.getDateHeader());
            // System.out.println(servletWebRequest.getHeaders());
            System.out.println(servletWebRequest.getIfModifiedSinceHeader());
            System.out.println(servletWebRequest.getLocale());
            System.out.println(servletWebRequest.getOriginalUrl());
            System.out.println(servletWebRequest.getUrl());
        }
    }
}
