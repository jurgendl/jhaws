package org.jhaws.common.web.wicket.bootstrap;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;

@SuppressWarnings("serial")
public class CheckAdsEnabled extends Panel {
	public static JavaScriptResourceReference JS;

	public static final String IMG_NAME = "ads.gif";

	public static PackageResourceReference IMG;

	static {
		try {
			CheckAdsEnabled.JS = new JavaScriptResourceReference(CheckAdsEnabled.class, "CheckAdsEnabled.js");
			CheckAdsEnabled.IMG = new PackageResourceReference(CheckJavaScriptEnabled.class, CheckAdsEnabled.IMG_NAME) {
				@Override
				protected String getMinifiedName() {
					return this.getName();
				}

				@Override
				public String getName() {
					return CheckAdsEnabled.IMG_NAME;
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

	public CheckAdsEnabled() {
		super("check.ads.enabled");
		Image image = new Image("check.ads.img") {
			@Override
			protected ResourceReference getImageResourceReference() {
				return CheckAdsEnabled.IMG;
			}
		};
		image.setMarkupId(image.getId());
		this.add(image);
		this.setVisible(WicketApplication.get().getSettings().isCheckAdsEnabled());
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		if (!this.isEnabledInHierarchy()) {
			return;
		}
		response.render(JavaScriptHeaderItem.forReference(CheckAdsEnabled.JS));
	}
}
