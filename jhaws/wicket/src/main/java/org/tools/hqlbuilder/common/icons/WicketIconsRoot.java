package org.tools.hqlbuilder.common.icons;

import java.net.URL;

import javax.swing.ImageIcon;

public class WicketIconsRoot {
	public static String CALENDER = "calendar.png";

	public static String BULLET_STAR = "bullet_star.png";

	public static String BULLET_ARROW_TOP = "bullet_arrow_top.png";

	public static String BULLET_ARROW_BOTTOM = "bullet_arrow_bottom.png";

	public static String BULLET_ARROW_DOWN = "bullet_arrow_down.png";

	public static String BULLET_ARROW_UP = "bullet_arrow_up.png";

	public static ImageIcon getIcon(String path) {
		if (path == null) {
			return null;
		}
		path = WicketIconsRoot.class.getPackage().getName().replace('.', '/') + '/' + path;
		URL resource = WicketIconsRoot.class.getClassLoader().getResource(path);
		if (resource == null) {
			System.err.println("resource not found: " + path);
			return null;
		}
		return new ImageIcon(resource);
	}
}
