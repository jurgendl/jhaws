package org.tools.hqlbuilder.webservice.wicket.bootstrap;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;

// https://wicket.apache.org/learn/examples/usingfragments.html
// https://getbootstrap.com/docs/4.0/components/alerts/
//
// class="alert-link" in content
//
// complex content:
// <h4 class="alert-heading">Well done!</h4>
// <p>Aww yeah, you successfully read this important alert message. This example text is going to run a bit longer so that you can see how spacing
// within an alert works with this kind of content.</p>
// <hr>
// <p class="mb-0">Whenever you need to, be sure to use margin utilities to keep things nice and tidy.</p>
//
// see org.apache.wicket.feedback.FencedFeedbackPanel
@SuppressWarnings("serial")
public class FeedbackPanel extends org.apache.wicket.markup.html.panel.FeedbackPanel {
    public FeedbackPanel(String id, IFeedbackMessageFilter filter) {
        super(id, filter);
        setEscapeModelStrings(false);
    }

    public FeedbackPanel(String id, Component filter) {
        this(id, new ComponentFeedbackMessageFilter(filter));
    }

    public FeedbackPanel(String id) {
        this(id, (IFeedbackMessageFilter) null);
    }

    @Override
    public boolean isVisible() {
        return super.isVisible() && this.anyMessage();
    }

    @Override
    protected Component newMessageDisplayComponent(String id, FeedbackMessage message) {
        Fragment fragment = new Fragment(id, "feedbackinternal", this);
        Serializable serializable = message.getMessage();
        Label label = new Label(id, (serializable == null) ? "" : serializable.toString());
        label.setEscapeModelStrings(FeedbackPanel.this.getEscapeModelStrings());
        label.setRenderBodyOnly(true);
        fragment.add(label);
        return fragment;
    }

    protected String getCSSClass(org.apache.wicket.feedback.FeedbackMessage message) {
        String feedbackCss = getFeedbackCss(message);
        return feedbackCss == null ? super.getCSSClass(message) : feedbackCss;
    }

    protected String getFeedbackCss(org.apache.wicket.feedback.FeedbackMessage message) {
        return "alert-" + getFeedbackCssInternal(message);
    }

    protected String getFeedbackCssInternal(org.apache.wicket.feedback.FeedbackMessage message) {
        if (FeedbackMessage.DEBUG == message.getLevel()) {
            return "info";
        }
        if (FeedbackMessage.INFO == message.getLevel()) {
            return "info";
        }
        if (FeedbackMessage.SUCCESS == message.getLevel()) {
            return "success";
        }
        if (FeedbackMessage.WARNING == message.getLevel()) {
            return "warning";
        }
        if (FeedbackMessage.ERROR == message.getLevel()) {
            return "danger";
        }
        if (FeedbackMessage.FATAL == message.getLevel()) {
            return "danger";
        }
        return "dark";
    }
}
