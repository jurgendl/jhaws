package org.swingeasy;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Jurgen
 */
public class ProgressGlassPane extends NonBlockingGlassPane {
    private static final long serialVersionUID = -2002416138162255170L;

    protected Component blockMe = null;

    protected Component parent = null;

    protected int progress = 0;

    protected Polygon shape;

    protected Thread thread;

    protected int count = 8;

    // clockwise
    protected int direction = 1;

    protected int scale = 95;

    protected int old_scale = 0;

    // lower = faster
    protected int speed = 70;

    // protected int startalpha = 0;

    protected Color diskColor = Color.gray;

    // glass transparent color
    protected Color blockingColor = Color.BLUE;

    protected Font font;

    protected String message = null;

    protected float[] ad = { 0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 7f, 6f, 5f, 4f, 3f, 2f, 1f };

    protected Boolean running = Boolean.FALSE;

    public ProgressGlassPane(Window parent) {
        super(parent);
        this.parent = parent;
        font = getFont().deriveFont(22f).deriveFont(Font.BOLD);
    }

    public ProgressGlassPane(Window parent, Font font) {
        super(parent);
        this.parent = parent;
        this.font = font;
    }

    public Color getBlockingColor() {
        return blockingColor;
    }

    public int getCount() {
        return count;
    }

    public int getDirection() {
        return direction;
    }

    public Color getDiskColor() {
        return diskColor;
    }

    public String getMessage() {
        return message;
    }

    public int getScale() {
        return scale;
    }

    protected Polygon getShape() {
        if (scale != old_scale) {
            shape = new Polygon();
            int x1 = scale * 8 / 100;
            int x2 = scale * 50 / 100;
            int x3 = scale * 55 / 100;
            int y1 = scale * 2 / 100;
            int y2 = scale * 10 / 100;
            int y3 = scale * 4 / 100;
            shape.addPoint(x1, -y1);
            shape.addPoint(x2, -y2);
            shape.addPoint(x3, -y3);
            shape.addPoint(x3, y3);
            shape.addPoint(x2, y2);
            shape.addPoint(x1, y1);
        }

        return shape;
    }

    public int getSpeed() {
        return speed;
    }

    // public int getStartalpha() {
    // return startalpha;
    // }

    /**
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (!isVisible()) {
            return;
        }
        Graphics2D g2d = Graphics2D.class.cast(g);
        g2d.setColor(blockingColor);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
        if (blockMe != null) {
            g2d.fillRect(blockMe.getLocation().x, blockMe.getLocation().y, blockMe.getWidth(), blockMe.getHeight());
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (parent instanceof JFrame) {
            JFrame pf = JFrame.class.cast(parent);
            g2d.translate(pf.getContentPane().getWidth() / 2, pf.getContentPane().getHeight() / 2);
        } else {
            g2d.translate(parent.getLocation().x + parent.getWidth() / 2, parent.getLocation().y + parent.getHeight() / 2);
        }

        if (message != null) {
            g2d.setColor(Color.black);
            float a = .8f - ad[progress % 16] * .05f;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
            g2d.setFont(font);
            int mw = g2d.getFontMetrics(font).stringWidth(message);
            g2d.translate(-mw / 2, 75);
            g2d.drawString(message, 0, 0);
            g2d.translate(+mw / 2, -75);
        }

        g2d.setColor(diskColor);
        int nr2 = 2 * count;
        double rad = direction * Math.PI / count;
        double breakoff = 1.0 / nr2;
        g2d.rotate(rad * progress);
        for (int i = 0; i < nr2; i++) {
            double alpha = breakoff * i;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            g2d.fillPolygon(getShape());
            g2d.rotate(rad);
        }
        g2d.dispose();
    }

    public void setBlockingColor(Color blockingColor) {
        this.blockingColor = blockingColor;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setDiskColor(Color diskColor) {
        this.diskColor = diskColor;
    }

    public void setlocationRelativeTo(Component parent) {
        this.parent = parent;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // public void setStartalpha(int startalpha) {
    // this.startalpha = startalpha;
    // }

    /**
     *
     * @see javax.swing.JComponent#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        toggle(visible);
    }

    protected void toggle(boolean enabled) {
        if (isVisible() != enabled) {
            return;
        }

        // synchronized (this.running) {
        if (enabled) {
            if (thread == null) {
                thread = new Thread(() -> {
                    while (running) {
                        try {
                            Thread.sleep(speed);
                        } catch (InterruptedException ex) {
                            //
                        }
                        SwingUtilities.invokeLater(() -> {
                            if (ProgressGlassPane.this.isVisible()) {
                                progress++;
                                ProgressGlassPane.this.repaint();
                            }
                        });
                    }
                });
                thread.setDaemon(true);
                thread.start();
                running = Boolean.TRUE;
            } else {
                // throw new IllegalArgumentException();
            }
        } else {
            if (thread != null) {
                thread.interrupt();
                thread = null;
                running = Boolean.FALSE;
            } else {
                // throw new IllegalArgumentException();
            }
        }
        // }
    }
}
