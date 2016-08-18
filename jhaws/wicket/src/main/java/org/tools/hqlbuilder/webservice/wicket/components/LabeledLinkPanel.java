package org.tools.hqlbuilder.webservice.wicket.components;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.wicket.actions.OnAction;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

public class LabeledLinkPanel extends Panel {
    public static final String LINK_ID = "link";

    public static final String LABEL_ID = "label";

    public static enum LinkType {
        page, submit, link;
    }

    private static final long serialVersionUID = -7352081661850450279L;

    public LabeledLinkPanel(String id, final IModel<String> labelModel, final Class<Page> page, PageParameters pageParameters) {
        this(id, labelModel, LinkType.page, page, pageParameters, null);
    }

    public LabeledLinkPanel(String id, final IModel<String> labelModel, boolean submit, OnAction onAction) {
        this(id, labelModel, submit ? LinkType.submit : LinkType.link, null, null, onAction);
    }

    protected LabeledLinkPanel(String id, final IModel<String> labelModel, LinkType linkType, final Class<Page> page, PageParameters pageParameters,
            OnAction onClick) {
        super(id);
        switch (linkType) {
            case page:
                add(getBookmarkablePageLink(page, pageParameters, labelModel));
                break;
            case submit:
                add(getSubmitLink(onClick, labelModel));
                break;
            case link:
                add(getLink(onClick, labelModel));
                break;
        }
    }

    protected AbstractLink getSubmitLink(final OnAction onAction, IModel<String> labelModel) {
        SubmitLink submitLink = new SubmitLink(LINK_ID) {
            private static final long serialVersionUID = 5824948229357499707L;

            @Override
            public void onSubmit() {
                onAction.doAction(null);
            }
        };
        submitLink.add(new CssClassNameAppender(JQueryUI.jquibutton));
        Component labelFragment = getLabel(labelModel).setOutputMarkupPlaceholderTag(false);
        submitLink.add(labelFragment);
        return submitLink;
    }

    protected <T> AbstractLink getBookmarkablePageLink(final Class<Page> page, final PageParameters pageParameters, final IModel<String> labelModel) {
        BookmarkablePageLink<T> bookmarkablePageLink = new BookmarkablePageLink<T>(LINK_ID, page, pageParameters);
        Component labelFragment = getLabel(labelModel);
        bookmarkablePageLink.add(labelFragment);
        return bookmarkablePageLink;
    }

    protected AbstractLink getLink(final OnAction onAction, final IModel<String> labelModel) {
        @SuppressWarnings({ "rawtypes" })
        Link link = new Link(LINK_ID) {
            private static final long serialVersionUID = 8265794917625787726L;

            @Override
            public void onClick() {
                onAction.doAction(null);
            }
        };
        Component labelFragment = getLabel(labelModel);
        link.add(labelFragment);
        return link;
    }

    protected <T> AbstractLink getAjaxLink(final OnAction onAction, final IModel<String> labelModel) {
        AjaxFallbackLink<T> link = new AjaxFallbackLink<T>(LINK_ID) {
            private static final long serialVersionUID = 8594068227797702608L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                onAction.doAction(target);
            }
        };
        Component labelFragment = getLabel(labelModel);
        link.add(labelFragment);
        return link;
    }

    protected Component getLabel(final IModel<String> labelModel) {
        return new Label(LABEL_ID, labelModel);
    }
}