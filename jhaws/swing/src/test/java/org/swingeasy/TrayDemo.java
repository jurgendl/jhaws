package org.swingeasy;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.swingeasy.UIUtils.SystemTrayConfig;

/**
 * @author Jurgen
 */
public class TrayDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        final JFrame frame = new JFrame("Tray Demo");
        frame.setIconImage(Resources.getImageResource("date.png").getImage());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        UIUtils.center(frame);
        frame.setSize(100, 100);
        frame.setVisible(true);
        UIUtils.createSystemTray(frame, new SystemTrayConfig());
    }
}
