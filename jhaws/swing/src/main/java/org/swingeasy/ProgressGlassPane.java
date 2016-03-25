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

    protected int direction = 1;

    protected int scale = 95;

    protected int old_scale = 0;

    protected int speed = 70;

    protected int startalpha = 0;

    protected Color diskColor = Color.gray;

    protected Color blockingColor = Color.BLUE;

    protected Font font;

    protected String message = null;

    protected float[] ad = { 0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 7f, 6f, 5f, 4f, 3f, 2f, 1f };

    protected Boolean running = Boolean.FALSE;

    public ProgressGlassPane(Window parent) {
        super(parent);
        this.parent = parent;
        this.font = this.getFont().deriveFont(22f).deriveFont(Font.BOLD);
    }

    public Color getBlockingColor() {
        return this.blockingColor;
    }

    public int getCount() {
        return this.count;
    }

    public int getDirection() {
        return this.direction;
    }

    public Color getDiskColor() {
        return this.diskColor;
    }

    public String getMessage() {
        return this.message;
    }

    public int getScale() {
        return this.scale;
    }

    protected Polygon getShape() {
        if (this.scale != this.old_scale) {
            this.shape = new Polygon();
            int x1 = (this.scale * 8) / 100;
            int x2 = (this.scale * 50) / 100;
            int x3 = (this.scale * 55) / 100;
            int y1 = (this.scale * 2) / 100;
            int y2 = (this.scale * 10) / 100;
            int y3 = (this.scale * 4) / 100;
            this.shape.addPoint(x1, -y1);
            this.shape.addPoint(x2, -y2);
            this.shape.addPoint(x3, -y3);
            this.shape.addPoint(x3, y3);
            this.shape.addPoint(x2, y2);
            this.shape.addPoint(x1, y1);
        }

        return this.shape;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getStartalpha() {
        return this.startalpha;
    }

    /**
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = Graphics2D.class.cast(g);
        g2d.setColor(this.blockingColor);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
        if (this.blockMe != null) {
            g2d.fillRect(this.blockMe.getLocation().x, this.blockMe.getLocation().y, this.blockMe.getWidth(), this.blockMe.getHeight());
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (this.parent instanceof JFrame) {
            JFrame pf = JFrame.class.cast(this.parent);
            g2d.translate(pf.getContentPane().getWidth() / 2, pf.getContentPane().getHeight() / 2);
        } else {
            g2d.translate(this.parent.getLocation().x + (this.parent.getWidth() / 2), this.parent.getLocation().y + (this.parent.getHeight() / 2));
        }

        if (this.message != null) {
            g2d.setColor(Color.black);
            float a = .8f - (this.ad[this.progress % 16] * .05f);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
            g2d.setFont(this.font);
            int mw = g2d.getFontMetrics(this.font).stringWidth(this.message);
            g2d.translate(-mw / 2, 75);
            g2d.drawString(this.message, 0, 0);
            g2d.translate(+mw / 2, -75);
        }

        g2d.setColor(this.diskColor);
        int nr2 = 2 * this.count;
        double rad = (this.direction * Math.PI) / this.count;
        double breakoff = 1.0 / nr2;
        g2d.rotate(rad * this.progress);
        for (int i = 0; i < nr2; i++) {
            double alpha = breakoff * i;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            g2d.fillPolygon(this.getShape());
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

    public void setStartalpha(int startalpha) {
        this.startalpha = startalpha;
    }

    /**
     * 
     * @see javax.swing.JComponent#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.toggle(visible);
    }

    protected void toggle(boolean enabled) {
        if (this.isVisible() != enabled) {
            return;
        }

        // synchronized (this.running) {
        if (enabled) {
            if (this.thread == null) {
                this.thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (ProgressGlassPane.this.running) {
                            try {
                                Thread.sleep(ProgressGlassPane.this.speed);
                            } catch (InterruptedException ex) {
                                //
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (ProgressGlassPane.this.isVisible()) {
                                        ProgressGlassPane.this.progress++;
                                        ProgressGlassPane.this.repaint();
                                    }
                                }
                            });
                        }
                    }
                });
                this.thread.setDaemon(true);
                this.thread.start();
                this.running = Boolean.TRUE;
            } else {
                // throw new IllegalArgumentException();
            }
        } else {
            if (this.thread != null) {
                this.thread.interrupt();
                this.thread = null;
                this.running = Boolean.FALSE;
            } else {
                // throw new IllegalArgumentException();
            }
        }
        // }
    }
}
