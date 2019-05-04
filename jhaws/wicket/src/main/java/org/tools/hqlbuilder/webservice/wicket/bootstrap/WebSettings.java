package org.tools.hqlbuilder.webservice.wicket.bootstrap;

import static org.jhaws.common.lang.CollectionUtils8.optional;

import java.io.Serializable;
import java.util.prefs.Preferences;

@SuppressWarnings("serial")
public class WebSettings implements Serializable {
	private static final String SETTINGS_ROOT = "jahws";

	private static final String SPINNER = "spinner";

	private transient Preferences prefs;

	private String spinner;

	private Preferences getPrefs() {
		return optional(prefs, () -> prefs = Preferences.userRoot().node(SETTINGS_ROOT));
	}

	public String getSpinner() {
		return optional(spinner, () -> spinner = getPrefs().get(SPINNER, "default"));
	}

	public void setSpinner(String s) {
		this.spinner = optional(this.spinner, s, v -> getPrefs().put(SPINNER, optional(v, () -> "default")));
	}
}
