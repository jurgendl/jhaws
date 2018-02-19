package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import org.apache.wicket.feedback.IFeedbackMessageFilter;

@SuppressWarnings("serial")
public class FeedbackPanel extends org.apache.wicket.markup.html.panel.FeedbackPanel {
    public FeedbackPanel(String id, IFeedbackMessageFilter filter) {
        super(id, filter);
    }

    public FeedbackPanel(String id) {
        super(id);
    }
}
