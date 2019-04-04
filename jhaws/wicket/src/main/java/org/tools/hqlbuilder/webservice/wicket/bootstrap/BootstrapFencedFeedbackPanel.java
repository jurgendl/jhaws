package org.tools.hqlbuilder.webservice.wicket.bootstrap;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.feedback.FeedbackCollector;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessagesModel;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
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
/**
 * bootstrap 4 ready (including being closable), reimplements {@link org.apache.wicket.feedback.FencedFeedbackPanel}
 */
@SuppressWarnings("serial")
public class BootstrapFencedFeedbackPanel extends FeedbackPanel {
    private static final MetaDataKey<Integer> FENCE_KEY = new MetaDataKey<Integer>() {};

    private final Component fence;

    public BootstrapFencedFeedbackPanel(String id, Component fence, IFeedbackMessageFilter filter) {
        super(id, filter);
        this.fence = fence;
        if (fence != null) {
            incrementFenceCount();
        }
        setEscapeModelStrings(false);
    }

    public BootstrapFencedFeedbackPanel(String id, IFeedbackMessageFilter filter) {
        this(id, null, filter);
    }

    public BootstrapFencedFeedbackPanel(String id, Component fence) {
        this(id, fence, null);
    }

    public BootstrapFencedFeedbackPanel(String id) {
        this(id, null, null);
    }

    protected void incrementFenceCount() {
        Integer count = fence.getMetaData(FENCE_KEY);
        count = count == null ? 1 : count + 1;
        fence.setMetaData(FENCE_KEY, count);
    }

    @Override
    protected void onRemove() {
        super.onRemove();
        if (fence != null) {
            // decrement the fence count
            decrementFenceCount();
        }
    }

    protected void decrementFenceCount() {
        Integer count = fence.getMetaData(FENCE_KEY);
        count = (count == null || count == 1) ? null : count - 1;
        fence.setMetaData(FENCE_KEY, count);
    }

    @Override
    protected FeedbackMessagesModel newFeedbackMessagesModel() {
        return new FeedbackMessagesModel(this) {
            @Override
            protected List<FeedbackMessage> collectMessages(Component panel, IFeedbackMessageFilter filter) {
                if (fence == null) {
                    // this is the catch-all panel
                    return new FeedbackCollector(panel.getPage()) {
                        @Override
                        protected boolean shouldRecurseInto(Component component) {
                            return !componentIsMarkedAsFence(component);
                        }
                    }.collect(filter);
                } else {
                    // this is a fenced panel
                    return new FeedbackCollector(fence) {
                        @Override
                        protected boolean shouldRecurseInto(Component component) {
                            // only recurse into components that are not fences
                            return !componentIsMarkedAsFence(component);
                        }
                    }.setIncludeSession(false).collect(filter);
                }
            }
        };
    }

    protected boolean componentIsMarkedAsFence(Component component) {
        return component.getMetaData(FENCE_KEY) != null;
    }

    @Override
    protected void onReAdd() {
        if (this.fence != null) {
            // The fence mark is removed when the feedback panel is removed from the hierarchy.
            // see onRemove().
            // when the panel is re-added, we recreate the fence mark.
            incrementFenceCount();
        }
        super.onReAdd();
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
        label.setEscapeModelStrings(getEscapeModelStrings());
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
