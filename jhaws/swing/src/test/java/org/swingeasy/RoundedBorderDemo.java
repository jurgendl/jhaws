package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class RoundedBorderDemo {
    private static Timer timer;

    private static JFrame frame;

    private static Timer time;

    private static final DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, SystemSettings.getCurrentLocale());

    private static float max = .65f;

    public static void fadeIn(final Window w) {
        w.setVisible(true);
        UIUtils.translucent(w, 0.0f);

        final int duration = 500;
        final int delay = 1000;
        final long start = delay + (System.nanoTime() / 1000000);
        RoundedBorderDemo.timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long now = System.nanoTime() / 1000000;
                long total = now - start;
                float f = (float) total / duration;
                if (total > duration) {
                    RoundedBorderDemo.timer.stop();
                    RoundedBorderDemo.fadeOut(w);
                }
                UIUtils.translucent(w, Math.min(RoundedBorderDemo.max, f * RoundedBorderDemo.max));
            }
        });
        RoundedBorderDemo.timer.setInitialDelay(delay);
        RoundedBorderDemo.timer.start();
    }

    public static void fadeOut(final Window w) {
        final int duration = 1000;
        final int delay = 10000;
        final long start = delay + (System.nanoTime() / 1000000);
        RoundedBorderDemo.timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long now = System.nanoTime() / 1000000;
                long total = now - start;
                float f = (float) total / duration;
                if (total > duration) {
                    RoundedBorderDemo.timer.stop();
                    w.setVisible(false);
                    RoundedBorderDemo.frame.dispose();
                    RoundedBorderDemo.time.stop();
                }
                UIUtils.translucent(w, Math.max(0.0f, (1.0f - f) * RoundedBorderDemo.max));
            }
        });
        RoundedBorderDemo.timer.setInitialDelay(delay);
        RoundedBorderDemo.timer.start();
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        RoundedBorderDemo.frame = new JFrame("forced exit");
        RoundedBorderDemo.frame.setSize(300, 20);
        RoundedBorderDemo.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RoundedBorderDemo.frame.setVisible(true);
        final Window w = new Window(RoundedBorderDemo.frame);
        w.setLayout(new BorderLayout());
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.lightGray);
        final JLabel timel = new JLabel(RoundedBorderDemo.dtf.format(new Date()), null, SwingConstants.CENTER);
        timel.setFont(timel.getFont().deriveFont(36f));
        RoundedBorderDemo.time = new Timer(990, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timel.setText(RoundedBorderDemo.dtf.format(new Date()));
            }
        });
        RoundedBorderDemo.time.start();
        main.add(timel);
        w.add(main);
        w.setSize(470, 80);
        UIUtils.bottomRight(w);
        UIUtils.makeDraggable(main);
        UIUtils.rounded(w, 40);
        UIUtils.translucent(w);
        RoundedBorderDemo.fadeIn(w);
    }
}
