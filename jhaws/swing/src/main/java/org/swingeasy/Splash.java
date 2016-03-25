package org.swingeasy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.WindowConstants;

public class Splash extends JComponent {
	private static final long serialVersionUID = -2472231955593958968L;

	private String text;

	private String version;

	private float progress;

	private final BufferedImage logo;

	private Color color = Color.black;

	private Color colorInv = Color.white;

	private Point textLocation;

	private Point versionLocation;

	private Rectangle progressBarLocation;

	private Font versionFont;

	private Color versionColor;

	private Color versionColorInv;

	private int INS = 10; // inset bar

	private int INSINT = 1; // inset inner bar

	private int H = 10; // height

	private boolean frame = false;

	public Splash(BufferedImage logo) {
		this.logo = logo;
	}

	public Color getColor() {
		return this.color;
	}

	/**
	 * @see javax.swing.JComponent#getMaximumSize()
	 */
	@Override
	public Dimension getMaximumSize() {
		return this.getPreferredSize();
	}

	/**
	 * @see javax.swing.JComponent#getMinimumSize()
	 */
	@Override
	public Dimension getMinimumSize() {
		return this.getPreferredSize();
	}

	/**
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.logo.getWidth(), this.logo.getHeight());
	}

	public Rectangle getProgressBarLocation() {
		return this.progressBarLocation;
	}

	/**
	 * @see java.awt.Component#getSize()
	 */
	@Override
	public Dimension getSize() {
		return this.getPreferredSize();
	}

	public Point getTextLocation() {
		return this.textLocation;
	}

	/**
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.drawImage(this.logo, 0, 0, this);

		if (this.progressBarLocation == null) {
			this.progressBarLocation = new Rectangle(this.INS, this.getHeight() - (this.INS / 2) - this.H, this.getWidth() - (2 * this.INS), this.H);
		}

		g2.setColor(this.color);
		g2.fillRect(this.progressBarLocation.x, this.progressBarLocation.y, this.progressBarLocation.width, this.progressBarLocation.height);
		g2.fillOval(this.progressBarLocation.x - (this.progressBarLocation.height / 2), this.progressBarLocation.y, this.progressBarLocation.height, this.progressBarLocation.height);
		g2.fillOval((this.progressBarLocation.x + this.progressBarLocation.width) - (this.progressBarLocation.height / 2), this.progressBarLocation.y, this.progressBarLocation.height, this.progressBarLocation.height);
		g2.setColor(this.colorInv);
		int rx = this.progressBarLocation.x + this.INSINT;
		int ry = this.progressBarLocation.y + this.INSINT;
		int rw = this.progressBarLocation.width - (this.INSINT * 2);
		int rh = this.progressBarLocation.height - (this.INSINT * 2);
		g2.fillRect(rx, ry, (int) (rw * this.progress), rh);
		int or = this.progressBarLocation.height - (this.INSINT * 2);
		int oy = this.progressBarLocation.y + this.INSINT;
		int ox1 = (this.progressBarLocation.x - (this.progressBarLocation.height / 2)) + this.INSINT;
		g2.fillOval(ox1, oy, or, or);
		int ox2 = ((this.progressBarLocation.x + this.progressBarLocation.width) - (this.progressBarLocation.height / 2)) + this.INSINT;
		int ox = ox1 + (int) ((ox2 - ox1) * this.progress);
		g2.fillOval(ox, oy, or, or);

		if (this.progressBarLocation.height >= 10) {
			g2.setColor(this.color);
			g2.fillOval(ox + (this.INSINT * 1), oy + (this.INSINT * 1), or - (this.INSINT * 2), or - (this.INSINT * 2));
			g2.setColor(this.colorInv);
			g2.fillOval(ox + (this.INSINT * 2), oy + (this.INSINT * 2), or - (this.INSINT * 4), or - (this.INSINT * 4));
		}

		if (this.text != null) {
			if (this.textLocation == null) this.textLocation = new Point(this.progressBarLocation.x, this.progressBarLocation.y - (this.getFont().getSize() / 2));
			g2.setColor(this.color);
			g2.drawString(this.text, this.textLocation.x, this.textLocation.y + 1);
			g2.drawString(this.text, this.textLocation.x + 1, this.textLocation.y);
			g2.drawString(this.text, this.textLocation.x + 1, this.textLocation.y + 1);
			g2.setColor(this.colorInv);
			g2.drawString(this.text, this.textLocation.x, this.textLocation.y);
		}

		if (this.version != null) {
			if (this.versionLocation == null) this.versionLocation = new Point(getWidth() - 50, 10);
			if (versionFont != null) g2.setFont(this.versionFont);
			g2.setColor(this.versionColor);
			g2.drawString(this.version, this.versionLocation.x, this.versionLocation.y + 1);
			g2.drawString(this.version, this.versionLocation.x + 1, this.versionLocation.y);
			g2.drawString(this.version, this.versionLocation.x + 1, this.versionLocation.y + 1);
			g2.setColor(this.versionColorInv);
			g2.drawString(this.version, this.versionLocation.x, this.versionLocation.y);
		}
	}

	public void setColor(Color color) {
		this.color = color;
		this.colorInv = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
	}

	public void setProgress(float f) {
		if (f < 0.0) {
			throw new IllegalArgumentException();
		}
		if (f > 1.0) {
			throw new IllegalArgumentException();
		}
		this.progress = f;
		this.repaint();
	}

	public void setProgressBarLocation(Rectangle progressBarLocation) {
		this.progressBarLocation = progressBarLocation;
	}

	public void setText(String string) {
		this.text = string;
		this.repaint();
	}

	public void setTextLocation(Point textLocation) {
		this.textLocation = textLocation;
	}

	public Window showSplash() {
		Window f;
		if (this.frame) {
			f = new JFrame();
			JFrame jf = (JFrame) f;
			jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			jf.setUndecorated(true);
			jf.setResizable(true);
		} else {
			f = new JWindow();
		}
		f.add(this);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		return f;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Point getVersionLocation() {
		return this.versionLocation;
	}

	public void setVersionLocation(Point versionLocation) {
		this.versionLocation = versionLocation;
	}

	public String getText() {
		return this.text;
	}

	public float getProgress() {
		return this.progress;
	}

	public Font getVersionFont() {
		return this.versionFont;
	}

	public void setVersionFont(Font versionFont) {
		this.versionFont = versionFont;
	}

	public Color getVersionColor() {
		return this.versionColor;
	}

	public void setVersionColor(Color versionColor) {
		this.versionColor = versionColor;
		this.versionColorInv = new Color(255 - versionColor.getRed(), 255 - versionColor.getGreen(), 255 - versionColor.getBlue());
	}

	public Color getVersionColorInv() {
		return this.versionColorInv;
	}

	public void setVersionColorInv(Color versionColorInv) {
		this.versionColorInv = versionColorInv;
	}
}
