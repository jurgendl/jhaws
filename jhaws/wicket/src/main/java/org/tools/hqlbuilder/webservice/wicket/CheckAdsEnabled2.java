package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class CheckAdsEnabled2 extends Panel {
    private static final long serialVersionUID = -5845042169704523961L;

    public static JavaScriptResourceReference JS;

    public static final String IMG_NAME = "ads.gif";

    public static PackageResourceReference IMG;

    static {
        try {
            CheckAdsEnabled2.JS = new JavaScriptResourceReference(CheckAdsEnabled2.class, "CheckAdsEnabled2.js");
            CheckAdsEnabled2.IMG = new PackageResourceReference(CheckJavaScriptEnabled.class, CheckAdsEnabled2.IMG_NAME) {
                private static final long serialVersionUID = 4214735061850976515L;

                @Override
                protected String getMinifiedName() {
                    return this.getName();
                }

                @Override
                public String getName() {
                    return CheckAdsEnabled2.IMG_NAME;
                };

                @Override
                public String getStyle() {
                    return null;
                };

                @Override
                public String getVariation() {
                    return null;
                };
            };
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public CheckAdsEnabled2() {
        super("check.ads.enabled");
        Image image = new Image("check.ads.img") {
            private static final long serialVersionUID = 9061259704592785374L;

            @Override
            protected ResourceReference getImageResourceReference() {
                return CheckAdsEnabled2.IMG;
            }
        };
        image.setMarkupId(image.getId());
        this.add(image);
        this.setVisible(WicketApplication.get().isCheckAdsEnabled());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!this.isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(CheckAdsEnabled2.JS));
    }
}
