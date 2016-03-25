package org.swingeasy;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class UIUtils {
	/**
	 * {@link MouseListener} that moves around a {@link Window} when dragged
	 */
	public static class MoveMouseListener implements MouseListener, MouseMotionListener {
		Point start_drag;

		Point start_loc;

		JComponent target;

		public MoveMouseListener(JComponent target) {
			this.target = target;
			target.addMouseListener(this);
			target.addMouseMotionListener(this);
		}

		public Window getFrame(Container container) {
			if (container == null) {
				return null;
			}
			if (container instanceof Window) {
				return (Window) container;
			}
			return this.getFrame(container.getParent());
		}

		Point getScreenLocation(MouseEvent e) {
			Point cursor = e.getPoint();
			Point target_location = this.target.getLocationOnScreen();
			return new Point((int) (target_location.getX() + cursor.getX()), (int) (target_location.getY() + cursor.getY()));
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			//
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point current = this.getScreenLocation(e);
			Point offset = new Point((int) current.getX() - (int) this.start_drag.getX(), (int) current.getY() - (int) this.start_drag.getY());
			Window frame = this.getFrame(this.target);
			Point new_location = new Point((int) (this.start_loc.getX() + offset.getX()), (int) (this.start_loc.getY() + offset.getY()));
			frame.setLocation(new_location);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			//
		}

		@Override
		public void mousePressed(MouseEvent e) {
			this.start_drag = this.getScreenLocation(e);
			Window frame = this.getFrame(this.target);
			if (frame == null) {
				return;
			}
			this.start_loc = frame.getLocation();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//
		}
	}

	protected static class PropertyChangeListenerDelegate implements PropertyChangeListener {
		protected Component weakReferencedComponent;

		protected EComponentI weakReferencedEComponentI;

		public PropertyChangeListenerDelegate(Component component) {
			this.weakReferencedComponent = component;
		}

		public PropertyChangeListenerDelegate(EComponentI component) {
			this.weakReferencedEComponentI = component;
		}

		public Object getDelageting() {
			if (this.weakReferencedComponent != null) {
				return this.weakReferencedComponent;
			} else if (this.weakReferencedEComponentI != null) {
				return this.weakReferencedEComponentI;
			} else {
				return null;
			}
		}

		/**
		 *
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (this.weakReferencedComponent != null) {
				Component component = this.weakReferencedComponent;
				if (component != null) {
					component.setLocale(Locale.class.cast(evt.getNewValue()));
				}
			}
			if (this.weakReferencedEComponentI != null) {
				EComponentI component = this.weakReferencedEComponentI;
				if (component != null) {
					component.setLocale(Locale.class.cast(evt.getNewValue()));
				}
			}
		}

		/**
		 *
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "PropertyChangeListener[" + SystemSettings.LOCALE + "]@" + Integer.toHexString(this.hashCode());
		}

	}

	private static class StaticPropertyChangeListener implements PropertyChangeListener {
		/**
		 *
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			UIUtils.setUILanguage(Locale.class.cast(evt.getNewValue()));
		}
	}

	public static class SystemTrayConfig {
		/** null: use icon from parent */
		private Image trayIcon = null;

		/** null: use title from parent frame */
		private String trayTitle = null;

		private AbstractAction exitAction;

		private PopupMenu popupmenu;

		private TrayIcon trayItem;

		private boolean showToggle = true;

		public AbstractAction getExitAction() {
			return this.exitAction;
		}

		public PopupMenu getPopupmenu() {
			return this.popupmenu;
		}

		public Image getTrayIcon() {
			return this.trayIcon;
		}

		public TrayIcon getTrayItem() {
			return this.trayItem;
		}

		public String getTrayTitle() {
			return this.trayTitle;
		}

		public boolean isShowToggle() {
			return this.showToggle;
		}

		public SystemTrayConfig setExitAction(AbstractAction exitAction) {
			this.exitAction = exitAction;
			return this;
		}

		public SystemTrayConfig setShowToggle(boolean showToggle) {
			this.showToggle = showToggle;
			return this;
		}

		public SystemTrayConfig setTrayIcon(Image trayIcon) {
			this.trayIcon = trayIcon;
			return this;
		}

		public SystemTrayConfig setTrayTitle(String trayTitle) {
			this.trayTitle = trayTitle;
			return this;
		}
	}

	/**
	 * UncaughtExceptionHandler delegating to given instance
	 */
	public static class UncaughtExceptionHandlerDelegate implements UncaughtExceptionHandler {
		private static UncaughtExceptionHandler delegate;

		public UncaughtExceptionHandlerDelegate() {
			super();
		}

		/**
		 *
		 * @see org.swingeasy.UIUtils.UncaughtExceptionHandler#handle(java.lang.Throwable)
		 */
		public void handle(Throwable t) {
			UncaughtExceptionHandlerDelegate.delegate.uncaughtException(Thread.currentThread(), t);
		}

		/**
		 *
		 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
		 */
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			UncaughtExceptionHandlerDelegate.delegate.uncaughtException(t, e);
		}
	}

	/**
	 * put window on bottomright, the size of the window must be set<br>
	 * TODO check on multi-monitor setup
	 */
	public static void bottomRight(Window w) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final Rectangle bounds = ge.getMaximumWindowBounds(); // native window bounds
		int x = (int) bounds.getMaxX() - w.getWidth();
		int y = (int) bounds.getMaxY() - w.getHeight();
		w.setLocation(x, y);
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			UIUtils.toHex(hexChars, v, j);
		}
		return new String(hexChars);
	}

	public static String bytesToHex(Byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			UIUtils.toHex(hexChars, v, j);
		}
		return new String(hexChars);
	}

	/**
	 * center window on screen
	 */
	public static void center(Window w) {
		w.setLocationRelativeTo(null);
	}

	/**
	 * create simple tray menu (exit and toggle visibility), returns popupmenu to add more menu items (or remove)
	 */
	public static PopupMenu createSystemTray(final JFrame frame, final SystemTrayConfig cfg) {
		if (!SystemTray.isSupported()) {
			return null;
		}
		if (cfg.getExitAction() == null) {
			cfg.setExitAction(new AbstractAction(Messages.getString((Locale) null, "tray.exit.title")) {
				private static final long serialVersionUID = 8188465232565268116L;

				@Override
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(frame, Messages.getString((Locale) null, "tray.exit.confirmation"),
							Messages.getString((Locale) null, "tray.exit.title"), JOptionPane.YES_NO_OPTION)) {
						System.exit(0);
					}
				}
			});
		}
		if (frame != null) {
			if (cfg.getTrayIcon() == null) {
				cfg.setTrayIcon(frame.getIconImage() == null ? new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB) : frame.getIconImage());
			}
			if (cfg.getTrayTitle() == null) {
				cfg.setTrayTitle(frame.getTitle());
			}
		}
		cfg.popupmenu = new PopupMenu(cfg.getTrayTitle());
		cfg.trayItem = new TrayIcon(cfg.getTrayIcon(), cfg.getTrayTitle(), cfg.popupmenu);
		cfg.trayItem.setImageAutoSize(true);
		if ((frame != null) && cfg.isShowToggle()) {
			cfg.trayItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if ((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1)) {
						UIUtils.toggleVisibility(frame);
					}
				}
			});
			cfg.trayItem.setToolTip(Messages.getDefaultString("tray.toggle.tooltip"));
			AbstractAction toggle = new AbstractAction(Messages.getString((Locale) null, "tray.toggle.title")) {
				private static final long serialVersionUID = -58991936990224202L;

				@Override
				public void actionPerformed(ActionEvent e) {
					UIUtils.toggleVisibility(frame);
				}
			};
			MenuItem emi = new MenuItem((String) toggle.getValue(Action.NAME));
			emi.addActionListener(toggle);
			cfg.popupmenu.add(emi);
		}
		{
			MenuItem emi = new MenuItem((String) cfg.getExitAction().getValue(Action.NAME));
			emi.addActionListener(cfg.getExitAction());
			cfg.popupmenu.add(emi);
		}
		try {
			SystemTray.getSystemTray().add(cfg.trayItem);
		} catch (AWTException ex) {
			ex.printStackTrace();
			return null;
		}
		if (frame != null) {
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowIconified(WindowEvent e) {
					UIUtils.toggleVisibility(frame);
				}
			});
		}
		return cfg.popupmenu;
	}

	public static void debugFocusChanges() {
		// Obtain a reference to the logger
		Logger focusLog = Logger.getLogger("java.awt.focus.Component");

		// The logger should log all messages
		focusLog.setLevel(Level.ALL);

		// Create a new handler
		ConsoleHandler handler = new ConsoleHandler();

		// The handler must handle all messages
		handler.setLevel(Level.ALL);

		// Add the handler to the logger
		focusLog.addHandler(handler);
	}

	public static void debugFocusTraversal(Window window, PrintStream out) {
		FocusTraversalPolicy ft = window.getFocusTraversalPolicy();
		Component first = ft.getInitialComponent(window);
		Component current = first;
		do {
			out.println(current);
			current = ft.getComponentAfter(window, current);
		} while ((current != null) && !first.equals(current));
	}

	/**
	 * lists all current locale change listeners
	 */
	public static void debugLocaleChangeListeners(PrintStream out) {
		for (Object o : SystemSettings.getSingleton().propertyChangeSupport.getPropertyChangeListeners()) {
			try {
				PropertyChangeListenerProxy pclp = (PropertyChangeListenerProxy) o;
				if (!org.swingeasy.WeakReferencedListener.isWrapped(pclp.getListener())) {
					continue;
				}
				Object component = WeakReferencedListener.<PropertyChangeListenerDelegate>unwrap(pclp.getListener()).getReference().getDelageting();
				out.println((component == null ? null : component.hashCode()) + " : " + component);
			} catch (Exception ex) {
				out.println(ex);
			}
		}
	}

	/**
	 * gets first displayable {@link JFrame}
	 */
	public static JFrame getCurrentFrame() {
		for (Frame frame : Frame.getFrames()) {
			if ((frame instanceof JFrame) && frame.isDisplayable()) {
				return JFrame.class.cast(frame);
			}
		}
		return null;
	}

	public static String getDescriptionForDirectory() {
		String dirdesc = UIUtils.cachedDescriptions.get(".");
		if (dirdesc == null) {
			dirdesc = FileSystemView.getFileSystemView().getSystemTypeDescription(SystemSettings.getTmpdir());
			UIUtils.cachedDescriptions.put(".", dirdesc);
		}
		return dirdesc;
	}

	/**
	 * gets file system description for file type
	 */
	public static String getDescriptionForFileType(String ext) {
		try {
			ext = ext.toLowerCase();
			String description = UIUtils.cachedDescriptions.get(ext);
			if (description == null) {
				File createTempFile = File.createTempFile("test", "." + ext);
				description = FileSystemView.getFileSystemView().getSystemTypeDescription(createTempFile);
				createTempFile.delete();
				UIUtils.cachedDescriptions.put(ext, description);
			}
			return description;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Icon getIconForDirectory() {
		Icon diricon = UIUtils.cachedIcons.get(".");
		if (diricon == null) {
			diricon = FileSystemView.getFileSystemView().getSystemIcon(SystemSettings.getTmpdir());
			UIUtils.cachedIcons.put(".", diricon);
		}
		return diricon;
	}

	public static Icon getIconForFile() {
		return UIUtils.getIconForFileType("-_-");
	}

	/**
	 * get filesystem icon for file
	 */
	public static Icon getIconForFileType(String ext) {
		try {
			ext = ext.toLowerCase();
			Icon icon = UIUtils.cachedIcons.get(ext);
			if (icon == null) {
				File createTempFile = File.createTempFile("test", "." + ext);
				icon = FileSystemView.getFileSystemView().getSystemIcon(createTempFile);
				createTempFile.delete();
				UIUtils.cachedIcons.put(ext, icon);
			}
			return icon;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * get rootwindow for a component
	 */
	public static Window getRootWindow(Component component) {
		Component root = SwingUtilities.getRoot(component);
		if (root instanceof Window) {
			return (Window) root;
		}
		return null;
	}

	/**
	 * prints exception to system error outputstream
	 */
	private static void log(Throwable ex) {
		ex.printStackTrace();
	}

	/**
	 * makes a window draggable by adding a listener as a {@link MouseListener} and {@link MouseMotionListener} to a {@link JComponent} in that window
	 */
	public static MoveMouseListener makeDraggable(JComponent target) {
		return new MoveMouseListener(target);
	}

	public static void maximize(Frame f) {
		f.setExtendedState(f.getExtendedState() | Frame.MAXIMIZED_BOTH);
	}

	/**
	 * activate Nimbus look and feel (returns true) or system look and feel (returns false) if not java 1.7 (7) or higher
	 */
	public static String niceLookAndFeel() {
		try {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); //$NON-NLS-1$
				return "javax.swing.plaf.nimbus.NimbusLookAndFeel";
			} catch (Exception ex) {
				return UIUtils.systemLookAndFeel();
			}
		} catch (Exception ex) {
			UIUtils.log(ex);
		}
		return UIManager.getLookAndFeel().getName();
	}

	/**
	 * register default uncaught exception handler thats logs exceptions to system error outputstream and quits only if Error
	 */
	public static void registerDefaultUncaughtExceptionHandler() {
		UIUtils.registerUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				try {
					System.out.println(t);
					UIUtils.log(e);
					// insert your exception handling code here
					// or do nothing to make it go away
				} catch (Exception tt) {
					// don't let the exception get thrown out, will cause infinite looping!
				} catch (Throwable tt) {
					System.exit(-1);
				}
			}
		});
	}

	/**
	 * register component as a locale change listener
	 */
	public static boolean registerLocaleChangeListener(final Component component) {
		component.setLocale(SystemSettings.getCurrentLocale());
		SystemSettings.getSingleton().addPropertyChangeListener(SystemSettings.LOCALE,
				WeakReferencedListener.wrap(PropertyChangeListener.class, new PropertyChangeListenerDelegate(component)));
		return true;
	}

	/**
	 * register component as a locale change listener
	 */
	public static boolean registerLocaleChangeListener(final EComponentI component) {
		for (Object o : SystemSettings.getSingleton().propertyChangeSupport.getPropertyChangeListeners()) {
			try {
				PropertyChangeListenerProxy pclp = (PropertyChangeListenerProxy) o;
				if (!org.swingeasy.WeakReferencedListener.isWrapped(pclp.getListener())) {
					continue;
				}
				PropertyChangeListenerDelegate reference = WeakReferencedListener.<PropertyChangeListenerDelegate>unwrap(pclp.getListener()).getReference();
				if (reference == null) {
					continue;
				}
				Object component0 = reference.getDelageting();
				if (component == component0) {
					return false;
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}

		component.setLocale(SystemSettings.getCurrentLocale());
		SystemSettings.getSingleton().addPropertyChangeListener(SystemSettings.LOCALE,
				WeakReferencedListener.wrap(PropertyChangeListener.class, new PropertyChangeListenerDelegate(component)));
		return true;
	}

	/**
	 * register uncaught exception handler
	 */
	public static void registerUncaughtExceptionHandler(Class<? extends UncaughtExceptionHandler> clazz) {
		try {
			UIUtils.registerUncaughtExceptionHandler(clazz.newInstance());
		} catch (InstantiationException ex) {
			throw new IllegalArgumentException(ex);
		} catch (IllegalAccessException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	/**
	 * register uncaught exception handler
	 */
	@SuppressWarnings("unchecked")
	public static void registerUncaughtExceptionHandler(String className) {
		Class<? extends UncaughtExceptionHandler> instance;
		try {
			instance = (Class<? extends UncaughtExceptionHandler>) Class.forName(className);
		} catch (ClassNotFoundException ex) {
			throw new IllegalArgumentException(ex);
		}
		UIUtils.registerUncaughtExceptionHandler(instance);
	}

	/**
	 * register uncaught exception handler
	 */
	public static void registerUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
		UncaughtExceptionHandlerDelegate.delegate = handler;
		System.setProperty("sun.awt.exception.handler", UncaughtExceptionHandlerDelegate.class.getName()); //$NON-NLS-1$
	}

	/**
	 * center window on top of component
	 */
	public static void relative(Window w, Component c) {
		w.setLocationRelativeTo(c);
	}

	/**
	 * sets rounded borders with arc of 20
	 *
	 * @see http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
	 */
	public static void rounded(Window w) {
		UIUtils.rounded(w, 20);
	}

	/**
	 * sets rounded borders with given arc
	 *
	 * @see http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
	 */
	public static void rounded(Window w, float arc) {
		if (AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.PERPIXEL_TRANSPARENT)) {
			try {
				Shape shape = new RoundRectangle2D.Float(0, 0, w.getWidth(), w.getHeight(), arc, arc);
				AWTUtilitiesWrapper.setWindowShape(w, shape);
			} catch (Exception ex) {
				UIUtils.log(ex);
			}
		}
	}

	/**
	 * (re-)shows tooltips earlier (10 milliseconds) and longer (20 seconds)
	 */
	public static void setLongerTooltips() {
		ToolTipManager sharedInstance = ToolTipManager.sharedInstance();
		sharedInstance.setReshowDelay(10);
		sharedInstance.setInitialDelay(10);
		sharedInstance.setDismissDelay(20000);
	}

	/**
	 * @see http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingaJFileChooserLookandFeel.htm
	 * @see http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingaJOptionPaneLookandFeel.htm
	 * @see http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingaJColorChooserLookandFeel.htm
	 */
	private static final void setUILanguage(Locale locale) {
		UIUtils.setUILanguage(locale, JOptionPane.class);
		UIUtils.setUILanguage(locale, JFileChooser.class);
		UIUtils.setUILanguage(locale, JColorChooser.class);

		// BufferedReader br = new BufferedReader(new InputStreamReader(ListInDialogDemo.class.getClassLoader().getResourceAsStream(
		// "javax/swing/"+simpleClassName+".keys.properties")));
		// String line;
		// while ((line = br.readLine()) != null) {
		// String key = line.split("\t")[0];
		// try {
		// System.out.println(key + "=" + UIManager.getString(key).toString());
		// } catch (Exception ex) {
		// //
		// }
		// }
	}

	private static final void setUILanguage(Locale locale, Class<? extends JComponent> componentClass) {
		String p1 = componentClass.getName().replace('.', '/') + ".keys.properties";
		String p2 = componentClass.getName();
		Locale p3 = locale == null ? SystemSettings.getCurrentLocale() : locale;
		String p4 = componentClass.getSimpleName();
		UIUtils.setUILanguage(p1, p2, p3, p4);
	}

	/**
	 * sets localization, expects a properties file with name {prefix}_{locale.toString()}.properties in the directory javax/swing; for all possible keys, see source documentation
	 *
	 * @param resource
	 * @param baseName
	 * @param locale
	 *            Locale
	 * @param prefix
	 */
	private static final void setUILanguage(String resource, String baseName, Locale locale, String prefix) {
		if (locale == null) {
			locale = SystemSettings.getCurrentLocale();
		}

		PropertyResourceBundle rb = (PropertyResourceBundle) ResourceBundle.getBundle(baseName, locale);

		try (InputStream in = UIUtils.class.getClassLoader().getResourceAsStream(resource)) {
			Properties props = new Properties();
			props.load(in);
			in.close();

			Set<String> missing = new HashSet<String>();

			for (Object k : props.keySet()) {
				String key = (String) k;
				String type = props.getProperty(key);
				String value = null;

				try {
					value = rb.getString(key);
				} catch (MissingResourceException ex) {
					//
				}

				if (key.startsWith(prefix + ".") && "String".equals(type)) {
					if (value == null) {
						missing.add(key);
					}
				}
			}

			if (missing.size() > 0) {
				System.err.println("missing translations for " + baseName + " for locale " + locale);

				for (String key : missing) {
					System.err.println(key + "=" + UIManager.get(key));
				}
			}
		} catch (Exception ex) {
			// kon niet controleren
			ex.printStackTrace();
		}

		final Enumeration<String> keys = rb.getKeys();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			UIManager.put(key.toString(), rb.getString(key));
		}
	}

	/**
	 * give window a shape
	 *
	 * @see http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
	 */
	public static void shaped(Window w, Shape shape) {
		if (AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.PERPIXEL_TRANSPARENT)) {
			try {
				AWTUtilitiesWrapper.setWindowShape(w, shape);
			} catch (Exception ex) {
				UIUtils.log(ex);
			}
		}
	}

	/**
	 * activate system look and feel
	 */
	public static String systemLookAndFeel() {
		try {
			String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(systemLookAndFeelClassName);
			return systemLookAndFeelClassName;
		} catch (Exception ex) {
			UIUtils.log(ex);
		}
		return UIManager.getLookAndFeel().getName();
	}

	/**
	 * toggle visibility of window
	 */
	public static void toggleVisibility(final Window frame) {
		frame.setVisible(!frame.isVisible());
		if (frame.isVisible()) {
			if (frame instanceof JFrame) {
				JFrame.class.cast(frame).setState(Frame.NORMAL);
			}
			frame.toFront();
			frame.repaint();
		}
	}

	public static void toHex(char[] hexChars, int v, int j) {
		hexChars[j * 2] = UIUtils.hexArray[v >>> 4];
		hexChars[(j * 2) + 1] = UIUtils.hexArray[v & 0x0F];
	}

	/**
	 * sets translucency with default value of .93
	 *
	 * @see http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
	 */
	public static void translucent(Window w) {
		UIUtils.translucent(w, .93f);
	}

	/**
	 * sets window translucency value
	 *
	 * @see http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean translucent(Window w, Float f) {
		try {
			if (AWTUtilitiesWrapper.isTranslucencySupported(Enum.valueOf((Class<? extends Enum>) Class.forName("com.sun.awt.AWTUtilities$Translucency"), "TRANSLUCENT"))) {
				try {
					AWTUtilitiesWrapper.setWindowOpacity(w, f);
					return true;
				} catch (Exception ex) {
					UIUtils.log(ex);
				}
			}
		} catch (Exception ex) {
			UIUtils.log(ex);
		}
		return false;
	}

	{
		SystemSettings.getSingleton().addPropertyChangeListener(SystemSettings.LOCALE, new StaticPropertyChangeListener());
	}

	static {
		// http://tips4java.wordpress.com/2008/10/25/enter-key-and-button/
		// TODO
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
	}

	static {
		UIUtils.setUILanguage(null);
	}

	protected static final UIUtils singleton = new UIUtils();

	protected static Map<String, Icon> cachedIcons = new HashMap<String, Icon>();

	protected static Map<String, String> cachedDescriptions = new HashMap<String, String>();

	public static final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * singleton
	 */
	private UIUtils() {
		super();
	}
}
