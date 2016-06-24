package org.swingeasy.validation;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.swingeasy.EButton;
import org.swingeasy.EButtonConfig;
import org.swingeasy.EIconButtonCustomizer;
import org.swingeasy.EventThreadSafeWrapper;
import org.swingeasy.Resources;

/**
 * @author Jurgen
 */
public class EValidationMessage extends EButton implements EValidationMessageI {
	private static final long serialVersionUID = 2641254029112205898L;

	protected final JComponent component;

	protected final Container parent;

	protected boolean installed = false;

	protected boolean valid = true;

	protected boolean showWhenValid = false;

	protected Icon invalidIcon;

	protected Icon validIcon;

	protected EValidationMessage stsi;

	protected EValidationMessage() {
		component = null;
		parent = null;
	}

	public EValidationMessage(final EValidationPane parent, final JComponent component) {
		super(new EButtonConfig(new EIconButtonCustomizer(Resources.getImageResource("bullet_red_small.png"))));

		invalidIcon = getIcon();
		validIcon = Resources.getImageResource("bullet_green_small.png");

		this.component = component;
		this.parent = parent.frontPanel;

		setOpaque(false);

		component.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {
				EValidationMessage.this.setVisible(false);
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				EValidationMessage.this.setLocationRelativeToComponent("moved");
			}

			@Override
			public void componentResized(ComponentEvent e) {
				EValidationMessage.this.setLocationRelativeToComponent("resized");
			}

			@Override
			public void componentShown(ComponentEvent e) {
				EValidationMessage.this.setVisible(true);
			}
		});

		setVisible(false);
	}

	protected Point calcRelativeLocation(Component c) {
		int x = 0;
		int y = 0;
		Component current = c;
		while (!(current instanceof Window)) {
			x += current.getLocation().x;
			y += current.getLocation().y;
			current = current.getParent();
		}
		return new Point(x, y);
	}

	public JComponent getComponent() {
		return component;
	}

	public EValidationMessageI getOriginal() {
		return this;
	}

	/**
	 * JDOC
	 * 
	 * @return
	 */
	public EValidationMessage getSimpleThreadSafeInterface() {
		try {
			if (stsi == null) {
				stsi = EventThreadSafeWrapper.getSimpleThreadSafeInterface(EValidationMessage.class, this, EValidationMessageI.class);
			}
			return stsi;
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	public boolean isInstalled() {
		return installed;
	}

	public boolean isShowWhenValid() {
		return showWhenValid;
	}

	/**
	 * install
	 */
	protected void lazyInstall() {
		if (installed || parent == null) {
			return;
		}

		parent.add(this);
		installed = true;
	}

	public void setInvalidIcon(Icon invalidIcon) {
		this.invalidIcon = invalidIcon;
	}

	/**
	 * 
	 * @see org.swingeasy.validation.EValidationMessageI#setIsInvalid(java.lang.String)
	 */
	@Override
	public void setIsInvalid(String message) {
		setLocationRelativeToComponent("invalid");
		setToolTipText(message);
		setVisible(true);
	}

	/**
	 * 
	 * @see org.swingeasy.validation.EValidationMessageI#setIsValid()
	 */
	@Override
	public void setIsValid() {
		setLocationRelativeToComponent("valid");
		setText(null);
		if (showWhenValid) {
			setIcon(validIcon);
			setVisible(true);
		} else {
			setVisible(false);
		}
	}

	protected void setLocationRelativeToComponent(String id) {
		if (!installed) {
			lazyInstall();
		}
		Point p_comp = calcRelativeLocation(component);
		Point p_this = calcRelativeLocation(getParent());
		Dimension d_comp = component.getSize();
		int x = p_comp.x + d_comp.width - p_this.x;
		int y = p_comp.y + d_comp.height - p_this.y;
		int iw2 = getIcon().getIconWidth();
		int ih2 = getIcon().getIconHeight();
		int px = x - iw2 + 2;
		int py = y - ih2 + 2;
		Rectangle r = new Rectangle(px, py, iw2, ih2);
		this.setBounds(r);

	}

	/**
	 * 
	 * @see org.swingeasy.validation.EValidationMessageI#setShowWhenValid(boolean)
	 */
	@Override
	public void setShowWhenValid(boolean b) {
		showWhenValid = b;
	}

	public void setValidIcon(Icon validIcon) {
		this.validIcon = validIcon;
	}

	/**
	 * @see #getSimpleThreadSafeInterface()
	 */
	public EValidationMessage stsi() {
		return getSimpleThreadSafeInterface();
	}

	/**
	 * @see #getSimpleThreadSafeInterface()
	 */
	public EValidationMessage STSI() {
		return getSimpleThreadSafeInterface();
	}
}