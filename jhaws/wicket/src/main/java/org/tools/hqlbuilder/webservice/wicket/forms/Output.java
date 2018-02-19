package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;

public class Output extends WebComponent {
    private static final long serialVersionUID = -7834417794355701445L;

    public Output(String id, final IModel<?> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);
        setRenderBodyOnly(false);
        setOutputMarkupId(true);
    }

    @Override
    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
        replaceComponentTagBody(markupStream, openTag, getDefaultModelObjectAsString());
    }
}
