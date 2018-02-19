package org.tools.hqlbuilder.webservice.wicket.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * <span wicket:id="notify"></span>
 *
 * @see http://www.primefaces.org/primeui/notify.html
 */
public class Notify extends Panel {
    private static final long serialVersionUID = 8477795062120562100L;

    public static final String ID = "notify";

    public static final String NOTIFY_BOTTOM = "notifybottom";

    public static final String NOTIFY_TOP = "notifytop";

    public Notify() {
        super(ID);
        setRenderBodyOnly(true);
        setOutputMarkupPlaceholderTag(false);
        setOutputMarkupId(false);
        {
            Label notifytop = new Label(NOTIFY_TOP, Model.of(""));
            notifytop.setMarkupId(NOTIFY_TOP);
            notifytop.setOutputMarkupId(true);
            notifytop.add(new CssClassNameAppender(PrimeUI.puinotifytop));
            add(notifytop);
        }
        {
            Label notifybottom = new Label(NOTIFY_BOTTOM, Model.of(""));
            notifybottom.setMarkupId(NOTIFY_BOTTOM);
            notifybottom.setOutputMarkupId(true);
            notifybottom.add(new CssClassNameAppender(PrimeUI.puinotifybottom));
            add(notifybottom);
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
    }

    public void bottomMessage(AjaxRequestTarget target, String message) {
        message(target, NOTIFY_BOTTOM, message);
    }

    public void topMessage(AjaxRequestTarget target, String message) {
        message(target, NOTIFY_TOP, message);
    }

    protected void message(AjaxRequestTarget target, String id, String message) {
        target.appendJavaScript(";$('#" + Notify.this.get(id).getMarkupId() + "').puinotify('show','" + message + "');");
    }
}
