package org.tools.hqlbuilder.webservice.wicket.components;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://jqueryui.com/accordion/
 * @see http://www.primefaces.org/primeui/accordion.html
 */
public class Accordion extends Panel {
    private static final long serialVersionUID = -1117839412897961353L;

    public static final String ACCORDION_ID = "accordion";

    private boolean preferPrime = true;

    public Accordion(String id, final List<String> titles, final List<Component> components, boolean preferPrime) {
        this(id, titles, components, null, preferPrime);
    }

    public Accordion(String id, final List<String> titles, boolean preferPrime, final List<String> data) {
        this(id, titles, null, data, preferPrime);
    }

    protected Accordion(String id, final List<String> titles, final List<Component> components, final List<String> data, boolean preferPrime) {
        super(id);

        this.preferPrime = preferPrime;

        WebMarkupContainer accordionparent = new WebMarkupContainer(ACCORDION_ID);
        accordionparent.add(new CssClassNameAppender(PrimeUI.puiaccordion));
        accordionparent.setOutputMarkupId(true);
        add(accordionparent);

        ListView<String> accordion = new ListView<String>("accordion.element", titles) {
            private static final long serialVersionUID = -498565033414119290L;

            @Override
            protected void populateItem(final ListItem<String> item) {
                item.add(new Label("accordion.title", item.getModelObject()));
                WebMarkupContainer container = new WebMarkupContainer("accordion.text") {
                    private static final long serialVersionUID = 79567713291018358L;

                    @Override
                    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
                        if (components != null) {
                            super.onComponentTagBody(markupStream, openTag);
                        } else {
                            String dataString = data.get(titles.indexOf(item.getModelObject()));
                            replaceComponentTagBody(markupStream, openTag, dataString);
                        }
                    }
                };
                container.setOutputMarkupPlaceholderTag(false);
                container.setRenderBodyOnly(true);
                if (components != null) {
                    container.add(components.get(titles.indexOf(item.getModelObject())));
                }
                item.add(container);
                item.setOutputMarkupPlaceholderTag(false);
                item.setRenderBodyOnly(true);
            }
        };
        if (preferPrime) {
            accordion.add(new CssClassNameAppender(PrimeUI.puiaccordion));
        } else {
            accordion.add(new CssClassNameAppender(JQueryUI.jquiaccordion));
        }
        accordionparent.add(accordion);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        if (preferPrime) {
            response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
        } else {
            response.render(JavaScriptHeaderItem.forReference(JQueryUI.getJQueryUIReference()));
        }
    }

    public boolean isPreferPrime() {
        return this.preferPrime;
    }

    public void setPreferPrime(boolean preferPrime) {
        this.preferPrime = preferPrime;
    }
}
