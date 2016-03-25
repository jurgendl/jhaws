package org.swingeasy.tab;

/**
 * released under 'CC0 1.0 Universal' http://creativecommons.org/publicdomain/zero/1.0/legalcode
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @see http://java-swing-tips.blogspot.com/2010/02/tabtransferhandler.html
 */
public class GhostGlassPane extends JPanel {
    
    private static final long serialVersionUID = 7483905354859468870L;

    private DnDTabbedPane tabbedPane;

    private BufferedImage draggingGhost = null;

    public GhostGlassPane(DnDTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        // System.out.println("new GhostGlassPane");
        this.setOpaque(false);
        // http://bugs.sun.com/view_bug.do?bug_id=6700748
        // setCursor(null); //XXX
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        DnDTabbedPane.DropLocation dl = this.tabbedPane.getDropLocation();
        Point p = this.getMousePosition(true); // dl.getDropPoint();
        if ((this.draggingGhost != null) && (dl != null) && (p != null)) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            Rectangle rect = this.tabbedPane.getDropLineRect();
            if ((rect != null) && dl.isDropable()) {
                Rectangle r = SwingUtilities.convertRectangle(this.tabbedPane, rect, this);
                g2.setColor(Color.RED);
                g2.fill(r);
                // tabbedPane.paintDropLine(g2);
            }
            // Point p = SwingUtilities.convertPoint(tabbedPane, dl.getDropPoint(), this);
            double xx = p.getX() - (this.draggingGhost.getWidth(this) / 2d);
            double yy = p.getY() - (this.draggingGhost.getHeight(this) / 2d);
            g2.drawImage(this.draggingGhost, (int) xx, (int) yy, this);
        }
    }

    public void setImage(BufferedImage draggingGhost) {
        this.draggingGhost = draggingGhost;
    }

    public void setTargetTabbedPane(DnDTabbedPane tab) {
        this.tabbedPane = tab;
    }
}
