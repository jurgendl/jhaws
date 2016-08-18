package org.tools.hqlbuilder.webservice.wicket.components;

import java.net.URI;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ExternalLink extends org.apache.wicket.markup.html.link.ExternalLink {
    private static final long serialVersionUID = 822282823515079064L;

    public static IModel<String> convertModelForURI(final IModel<URI> uri) {
        return new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 3823865517405954216L;

            @Override
            public String getObject() {
                return uri == null || uri.getObject() == null ? null : uri.getObject().toASCIIString();
            }
        };
    }

    public static IModel<String> convertModelForURL(final IModel<URL> url) {
        return new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 620100245769705448L;

            @Override
            public String getObject() {
                return url == null || url.getObject() == null ? null : url.getObject().toExternalForm();
            }
        };
    }

    public static IModel<String> createModel(final URI uri) {
        return new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 89848643726769637L;

            @Override
            public String getObject() {
                return uri == null ? null : uri.toASCIIString();
            }
        };
    }

    public static IModel<String> createModel(final URL url) {
        return new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = -3280636915902490355L;

            @Override
            public String getObject() {
                return url == null ? null : url.toExternalForm();
            }
        };
    }

    /** wat doen wanneer link waarde null is: opties */
    public static enum WhenNull {
        /** gebruik dit om de link weg te doen: visible=false */
        HIDE,
        /** gebruik dit om de link weg te doen (de 'a' tag wordt een 'span' tag) maar niet de componenten die er in zitten: disabled=true */
        DISABLE;
    }

    /** wat doen wanneer link waarde null is */
    protected WhenNull whenNull = WhenNull.DISABLE;

    public ExternalLink(String id, IModel<String> href, IModel<String> label) {
        super(id, href, label);
        construction();
    }

    public ExternalLink(String id, IModel<String> href) {
        super(id, href);
        construction();
    }

    /**
     * erf url over als label (shortUrlAsLabel=true: zonder protocol indicatie)
     */
    public ExternalLink(String id, final IModel<String> href, final boolean shortUrlAsLabel) {
        this(id, href, new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 1838103890589747027L;

            @Override
            public String getObject() {
                String url = href.getObject();
                if (url != null && shortUrlAsLabel) {
                    url = url.replaceFirst("http://", "").replaceFirst("https://", "").replaceFirst("ftp://", "");
                }
                return url;
            }
        });
    }

    public ExternalLink(String id, String href, String label) {
        this(id, Model.<String> of(href), Model.<String> of(label));
    }

    public ExternalLink(String id, String href) {
        this(id, Model.<String> of(href));
    }

    public ExternalLink(String id, URI href, String label) {
        this(id, createModel(href), Model.<String> of(label));
    }

    public ExternalLink(String id, URI href) {
        this(id, createModel(href));
    }

    public ExternalLink(String id, URL href, String label) {
        this(id, createModel(href), Model.<String> of(label));
    }

    public ExternalLink(String id, URL href) {
        this(id, createModel(href));
    }

    public ExternalLink setOpenInNewPage(boolean b) {
        add(new AttributeModifier("target", b ? new Model<String>("_blank") : null));
        return this;
    }

    protected void construction() {
        setOutputMarkupPlaceholderTag(true);
        setRenderBodyOnly(false);
        setBeforeDisabledLink("<span>");
        setAfterDisabledLink("</span>");
    }

    @Override
    protected boolean isLinkEnabled() {
        if (whenNull == WhenNull.DISABLE) {
            return super.isLinkEnabled() && getDefaultModel() != null && getDefaultModelObject() != null
                    && StringUtils.isNotBlank(getDefaultModelObjectAsString());
        }
        return super.isLinkEnabled();
    }

    @Override
    public boolean isVisible() {
        if (whenNull == WhenNull.HIDE) {
            return super.isVisible() && getDefaultModel() != null && getDefaultModelObject() != null
                    && StringUtils.isNotBlank(getDefaultModelObjectAsString());
        }
        return super.isVisible();
    }

    public WhenNull getWhenNull() {
        return this.whenNull;
    }

    public ExternalLink setWhenNull(WhenNull whenNull) {
        this.whenNull = whenNull;
        return this;
    }
}
