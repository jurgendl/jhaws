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
        return color;
    }

    /**
     * @see javax.swing.JComponent#getMaximumSize()
     */
    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    /**
     * @see javax.swing.JComponent#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(logo.getWidth(), logo.getHeight());
    }

    public Rectangle getProgressBarLocation() {
        return progressBarLocation;
    }

    /**
     * @see java.awt.Component#getSize()
     */
    @Override
    public Dimension getSize() {
        return getPreferredSize();
    }

    public Point getTextLocation() {
        return textLocation;
    }

    /**
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawImage(logo, 0, 0, this);

        if (progressBarLocation == null) {
            progressBarLocation = new Rectangle(INS, getHeight() - INS / 2 - H, getWidth() - 2 * INS, H);
        }

        g2.setColor(color);
        g2.fillRect(progressBarLocation.x, progressBarLocation.y, progressBarLocation.width, progressBarLocation.height);
        g2.fillOval(progressBarLocation.x - progressBarLocation.height / 2, progressBarLocation.y, progressBarLocation.height,
                progressBarLocation.height);
        g2.fillOval(progressBarLocation.x + progressBarLocation.width - progressBarLocation.height / 2, progressBarLocation.y,
                progressBarLocation.height, progressBarLocation.height);
        g2.setColor(colorInv);
        int rx = progressBarLocation.x + INSINT;
        int ry = progressBarLocation.y + INSINT;
        int rw = progressBarLocation.width - INSINT * 2;
        int rh = progressBarLocation.height - INSINT * 2;
        g2.fillRect(rx, ry, (int) (rw * progress), rh);
        int or = progressBarLocation.height - INSINT * 2;
        int oy = progressBarLocation.y + INSINT;
        int ox1 = progressBarLocation.x - progressBarLocation.height / 2 + INSINT;
        g2.fillOval(ox1, oy, or, or);
        int ox2 = progressBarLocation.x + progressBarLocation.width - progressBarLocation.height / 2 + INSINT;
        int ox = ox1 + (int) ((ox2 - ox1) * progress);
        g2.fillOval(ox, oy, or, or);

        if (progressBarLocation.height >= 10) {
            g2.setColor(color);
            g2.fillOval(ox + INSINT * 1, oy + INSINT * 1, or - INSINT * 2, or - INSINT * 2);
            g2.setColor(colorInv);
            g2.fillOval(ox + INSINT * 2, oy + INSINT * 2, or - INSINT * 4, or - INSINT * 4);
        }

        if (text != null) {
            if (textLocation == null) {
                textLocation = new Point(progressBarLocation.x, progressBarLocation.y - getFont().getSize() / 2);
            }
            g2.setColor(color);
            g2.drawString(text, textLocation.x, textLocation.y + 1);
            g2.drawString(text, textLocation.x + 1, textLocation.y);
            g2.drawString(text, textLocation.x + 1, textLocation.y + 1);
            g2.setColor(colorInv);
            g2.drawString(text, textLocation.x, textLocation.y);
        }

        if (version != null) {
            if (versionLocation == null) {
                versionLocation = new Point(getWidth() - 50, 10);
            }
            if (versionFont != null) {
                g2.setFont(versionFont);
            }
            g2.setColor(versionColor);
            g2.drawString(version, versionLocation.x, versionLocation.y + 1);
            g2.drawString(version, versionLocation.x + 1, versionLocation.y);
            g2.drawString(version, versionLocation.x + 1, versionLocation.y + 1);
            g2.setColor(versionColorInv);
            g2.drawString(version, versionLocation.x, versionLocation.y);
        }
    }

    public void setColor(Color color) {
        this.color = color;
        colorInv = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    }

    public void setProgress(float f) {
        if (f < 0.0) {
            throw new IllegalArgumentException();
        }
        if (f > 1.0) {
            throw new IllegalArgumentException();
        }
        progress = f;
        this.repaint();
    }

    public void setProgressBarLocation(Rectangle progressBarLocation) {
        this.progressBarLocation = progressBarLocation;
    }

    public void setText(String string) {
        text = string;
        this.repaint();
    }

    public void setTextLocation(Point textLocation) {
        this.textLocation = textLocation;
    }

    public Window showSplash() {
        Window f;
        if (frame) {
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
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Point getVersionLocation() {
        return versionLocation;
    }

    public void setVersionLocation(Point versionLocation) {
        this.versionLocation = versionLocation;
    }

    public String getText() {
        return text;
    }

    public float getProgress() {
        return progress;
    }

    public Font getVersionFont() {
        return versionFont;
    }

    public void setVersionFont(Font versionFont) {
        this.versionFont = versionFont;
    }

    public Color getVersionColor() {
        return versionColor;
    }

    public void setVersionColor(Color versionColor) {
        this.versionColor = versionColor;
        versionColorInv = new Color(255 - versionColor.getRed(), 255 - versionColor.getGreen(), 255 - versionColor.getBlue());
    }

    public Color getVersionColorInv() {
        return versionColorInv;
    }

    public void setVersionColorInv(Color versionColorInv) {
        this.versionColorInv = versionColorInv;
    }
}
