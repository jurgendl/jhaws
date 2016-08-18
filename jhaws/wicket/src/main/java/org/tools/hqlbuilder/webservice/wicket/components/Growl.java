package org.tools.hqlbuilder.webservice.wicket.components;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * <span wicket:id="growl"></span>
 *
 * @see http://www.primefaces.org/primeui/growl.html
 */
public class Growl extends Panel {
    private static final long serialVersionUID = 8477795062120562100L;

    public static final String ID = "growl";

    public static final String GROWL_MESSAGE = "growlmessage";

    public Growl() {
        super(ID);
        setRenderBodyOnly(true);
        setOutputMarkupPlaceholderTag(false);
        setOutputMarkupId(false);
        Label growler = new Label(GROWL_MESSAGE, Model.of(""));
        growler.setMarkupId(GROWL_MESSAGE);
        growler.add(new CssClassNameAppender(PrimeUI.puigrowl));
        add(growler);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
    }

    public String message(AjaxRequestTarget target, GrowlMessage... message) {
        StringBuilder msg = new StringBuilder();
        for (GrowlMessage element : message) {
            msg.append(element.toString()).append(",");
        }
        String javascript = ";$('#" + get(GROWL_MESSAGE).getMarkupId() + "').puigrowl({life:60000}).puigrowl('show',["
                + msg.delete(msg.length(), msg.length()).toString() + "]);";
        if (target != null) {
            target.appendJavaScript(javascript);
        }
        return javascript;
    }

    public static class GrowlMessage implements Serializable {
        private static final long serialVersionUID = -3655733150961814228L;

        private String message;

        private String title;

        private GrowlSeverity severity;

        public GrowlMessage() {
            super();
        }

        public GrowlMessage(GrowlSeverity severity, String title, String message) {
            this.severity = severity;
            this.title = title;
            this.message = message;
        }

        @Override
        public String toString() {
            return "{severity:'" + severity + "',summary:'" + title + "',detail:'" + message + "'}";
        }

        public String getMessage() {
            return this.message;
        }

        public GrowlSeverity getSeverity() {
            return this.severity;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setSeverity(GrowlSeverity severity) {
            this.severity = severity;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static enum GrowlSeverity {
        info, warn, error;
    }
}
