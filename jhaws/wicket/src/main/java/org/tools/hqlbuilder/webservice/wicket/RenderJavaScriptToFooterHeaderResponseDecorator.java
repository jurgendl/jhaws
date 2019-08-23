package org.tools.hqlbuilder.webservice.wicket;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.head.filter.AbstractHeaderResponseFilter;
import org.apache.wicket.markup.head.filter.FilteringHeaderResponse;
import org.apache.wicket.markup.head.filter.OppositeHeaderResponseFilter;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;
import org.apache.wicket.util.lang.Args;

public class RenderJavaScriptToFooterHeaderResponseDecorator implements IHeaderResponseDecorator {

    final List<FilteringHeaderResponse.IHeaderResponseFilter> filters;

    public RenderJavaScriptToFooterHeaderResponseDecorator(final String filterName) {
        Args.notEmpty(filterName, "filterName");

        filters = new ArrayList<>();

        final AbstractHeaderResponseFilter jsAcceptingFilter = new AbstractHeaderResponseFilter(filterName) {
            @Override
            public boolean accepts(HeaderItem item) {
                return item instanceof JavaScriptHeaderItem || item instanceof OnDomReadyHeaderItem || item instanceof OnLoadHeaderItem;
            }
        };

        filters.add(jsAcceptingFilter);
        filters.add(new OppositeHeaderResponseFilter("headBucket", jsAcceptingFilter));
    }

    @Override
    public IHeaderResponse decorate(final IHeaderResponse response) {
        return new FilteringHeaderResponse(response, "headBucket", filters);
    }
}
