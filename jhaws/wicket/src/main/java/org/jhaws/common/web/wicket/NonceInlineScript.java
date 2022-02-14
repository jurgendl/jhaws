package org.jhaws.common.web.wicket;

import org.apache.wicket.markup.ComponentTag;

@SuppressWarnings("serial")
public class NonceInlineScript extends org.apache.wicket.MarkupContainer {
    public NonceInlineScript(String id) {
        super(id);
    }

    public static String getNonce(org.apache.wicket.Component component) {
        return org.apache.wicket.protocol.http.WebApplication.get().getCspSettings().getNonce(component.getRequestCycle());
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        checkComponentTag(tag, "script");
        super.onComponentTag(tag);
        String nonce = getNonce(this);
        tag.getAttributes().put("nonce", nonce);
        tag.getAttributes().put("type", "text/javascript");
    }
}