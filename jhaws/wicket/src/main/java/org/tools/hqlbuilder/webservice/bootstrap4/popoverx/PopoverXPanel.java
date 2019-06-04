package org.tools.hqlbuilder.webservice.bootstrap4.popoverx;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

@SuppressWarnings("serial")
public class PopoverXPanel extends Panel {
    protected static final String POPOVER_X_TRIGGER_FRAGMENT = "popover-x-trigger-fragment";

    public static final String POPOVER_X_FOOTER = "popover-x-footer";

    public static final String POPOVER_X_CONTENT = "popover-x-content";

    public static final String POPOVER_X_CLOSE = "popover-x-close";

    public static final String POPOVER_X_HEADER_CLOSE = "popover-x-header-close";

    public static final String POPOVER_X_HEADER_TEXT = "popover-x-header-text";

    public static final String POPOVER_X_HEADER = "popover-x-header";

    public static final String POPOVER_X_PANEL = "popover-x-panel";

    public static final String POPOVER_X_TRIGGER = "popover-x-trigger";

    protected WebMarkupContainer popoverXPanel;

    public PopoverXPanel(String id) {
        super(id);
        add(createTrigger(POPOVER_X_TRIGGER));
        add(createPopover(POPOVER_X_PANEL));
    }

    protected WebMarkupContainer createPopover(String id) {
        IModel<Boolean> headerVisible = createHeaderVisibibleModel();
        popoverXPanel = new WebMarkupContainer(id);
        popoverXPanel.setOutputMarkupId(true);
        popoverXPanel.add(createHeader(POPOVER_X_HEADER, headerVisible));
        popoverXPanel.add(createCloseNoHeader(POPOVER_X_CLOSE, headerVisible));
        popoverXPanel.add(createContent(POPOVER_X_CONTENT));
        popoverXPanel.add(createFooter(POPOVER_X_FOOTER));
        return popoverXPanel;
    }

    protected WebMarkupContainer createHeader(String id, IModel<Boolean> headerVisible) {
        WebMarkupContainer header = new WebMarkupContainer(id) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(Boolean.TRUE.equals(headerVisible.getObject()));
            }
        };
        header.add(createHeaderTitle(POPOVER_X_HEADER_TEXT));
        header.add(createCloseInHeader(POPOVER_X_HEADER_CLOSE));
        return header;
    }

    protected Component createTrigger(String id) {
        Fragment fragment = new Fragment(id, POPOVER_X_TRIGGER_FRAGMENT, this);
        Button triggeredBy = new Button(id) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put("data-target", getDataTargetAttribute());
            }
        };
        fragment.add(triggeredBy);
        return fragment;
    }

    protected Component createHeaderTitle(String id) {
        Label headerTitle = new Label(id, createHeaderTitleModel());
        return headerTitle;
    }

    protected Component createCloseInHeader(String id) {
        WebMarkupContainer closeInHeader = new WebMarkupContainer(id);
        return closeInHeader;
    }

    protected Component createCloseNoHeader(String id, IModel<Boolean> headerVisible) {
        WebMarkupContainer closeNoHeader = new WebMarkupContainer(id) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(!Boolean.TRUE.equals(headerVisible.getObject()));
            }
        };
        return closeNoHeader;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(PopoverX.JS));
        response.render(CssHeaderItem.forReference(PopoverX.CSS));
    }

    protected Model<Boolean> createHeaderVisibibleModel() {
        return Model.of(Boolean.TRUE);
    }

    protected Model<String> createHeaderTitleModel() {
        return Model.of("");
    }

    protected Component createContent(String id) {
        WebMarkupContainer content = new WebMarkupContainer(id);
        content.add(new Label("content", Model.of("")).setRenderBodyOnly(true));
        return content;
    }

    protected Component createFooter(String id) {
        WebMarkupContainer footer = new WebMarkupContainer(id);
        footer.add(new Label("footer", Model.of("")).setRenderBodyOnly(true));
        return footer;
    }

    protected String getDataTargetAttribute() {
        return "#" + popoverXPanel.getMarkupId();
    }
}
