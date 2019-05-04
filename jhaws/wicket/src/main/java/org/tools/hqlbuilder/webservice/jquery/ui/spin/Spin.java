package org.tools.hqlbuilder.webservice.jquery.ui.spin;

import java.util.Arrays;
import java.util.List;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

// http://www.raphaelfabeni.com.br/css-loader/
// https://github.com/raphaelfabeni/css-loader
public class Spin {
	private static final String HTML = "<span id=\"spinnercontainer\" class=\"visible\"><div class=\"loader loader-{type} is-active\"></div></span>";

	public static final String HIDE = ";$('#spinnercontainer').removeClass('visible').addClass('invisible');";

	public static final String SHOW = ";$('#spinnercontainer').removeClass('invisible').addClass('visible');";

	public static List<String> types = Arrays.asList("default", "ball", "bar", "bar-ping-pong", "border", "bouncing",
			"clock", "curtain", "double", "music", "pokeball", "smartphone");

	public static CssResourceReference SPIN_CSS_BALL = new CssResourceReference(Spin.class, "loader-ball.css");
	public static CssResourceReference SPIN_CSS_BAR = new CssResourceReference(Spin.class, "loader-bar.css");
	public static CssResourceReference SPIN_CSS_PING_PONG = new CssResourceReference(Spin.class,
			"loader-bar-ping-pong.css");
	public static CssResourceReference SPIN_CSS_BORDER = new CssResourceReference(Spin.class, "loader-border.css");
	public static CssResourceReference SPIN_CSS_BOUNCING = new CssResourceReference(Spin.class, "loader-bouncing.css");
	public static CssResourceReference SPIN_CSS_CLOCK = new CssResourceReference(Spin.class, "loader-clock.css");
	public static CssResourceReference SPIN_CSS_CURTAIN = new CssResourceReference(Spin.class, "loader-curtain.css");
	public static CssResourceReference SPIN_CSS_DEFAULT = new CssResourceReference(Spin.class, "loader-default.css");
	public static CssResourceReference SPIN_CSS_DOULE = new CssResourceReference(Spin.class, "loader-double.css");
	public static CssResourceReference SPIN_CSS_MUSIC = new CssResourceReference(Spin.class, "loader-music.css");
	public static CssResourceReference SPIN_CSS_POKEBALL = new CssResourceReference(Spin.class, "loader-pokeball.css");
	public static CssResourceReference SPIN_CSS_CELL = new CssResourceReference(Spin.class, "loader-smartphone.css");

	public static List<CssResourceReference> css = Arrays.asList(SPIN_CSS_DEFAULT, SPIN_CSS_BALL, SPIN_CSS_BAR,
			SPIN_CSS_PING_PONG, SPIN_CSS_BORDER, SPIN_CSS_BOUNCING, SPIN_CSS_CLOCK, SPIN_CSS_CURTAIN, SPIN_CSS_DOULE,
			SPIN_CSS_MUSIC, SPIN_CSS_POKEBALL, SPIN_CSS_CELL);

	public static String show() {
		return SHOW;
	}

	public static String hide() {
		return HIDE;
	}

	public static String html(String type) {
		type(type);
		return HTML.replace("{type}", type);
	}

	protected static int type(String type) {
		int i = types.indexOf(type);
		if (i == -1)
			throw new IllegalArgumentException(types + " - " + types);
		return i;
	}

	public static CssResourceReference css(String type) {
		return css.get(type(type));
	}
}
