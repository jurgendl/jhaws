package org.tools.hqlbuilder.webservice.wicket.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;

//
// deleteLink.add(new AttributeModifier("onclick",
// "if(!confirm('Do you really want to perform this action?')) return false;"));
// AjaxCallListener ajaxCallListener = new AjaxCallListener();
// ajaxCallListener.onPrecondition( "return confirm('" + text + "');" );
// attributes.getAjaxCallListeners().add( ajaxCallListener );
//
//
//
//
// @Override
// protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
// {
// String confirmJs =
// "if (!confirm('Do you really want to perform this action')) return false;"
// AjaxCallListener listener = new AjaxCallListener();
// listener.onPrecondition(confirmJs);
// attributes.getAjaxCallListeners().add(listener);
// }
// resetLink.add(new AttributeModifier("onclick",
/**
 * @see http://api.jqueryui.com/dialog/
 * @see https://cwiki.apache.org/confluence/display/WICKET/Getting+user+confirmation
 * @see http://tomaszdziurko.pl/2010/02/wicket-ajax-modal-are-you-sure-window/
 * @see http://apache-wicket.1842946.n4.nabble.com/Javascript-confirm-with-condition-before-submit-td4659672.html
 */
public class JqueryUIConfirmationDialog extends Panel {
    protected static class ConfirmationEvent extends AttributeModifier {
        private static final long serialVersionUID = -4206560140670731402L;

        private String title;

        private String text;

        public ConfirmationEvent(String title, String text) {
            super("onclick", "");
            this.title = title;
            this.text = text;
        }

        @Override
        protected String newValue(final String currentValue, final String replacementValue) {
            return "showConfirmationDialog('" + this.title + "','" + this.text + "', function(){ " + currentValue
                    + " ;}, function(){;});return false;";
        }
    }

    private static final long serialVersionUID = 7011309436337649432L;

    public JqueryUIConfirmationDialog(String id) {
        super(id);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(JQueryUI.getJQueryUIReference()));
    }

    public static void addConfirmationEvent(AbstractLink link, String title, String text) {
        link.add(new ConfirmationEvent(title, text));
    }
}
