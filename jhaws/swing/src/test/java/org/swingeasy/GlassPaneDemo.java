package org.swingeasy;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jhaws.common.lang.BooleanValue;

/**
 * @author Jurgen
 */
public class GlassPaneDemo {
    private static JSlider addSlider(int min, int max, int value, int major, Integer minor, JPanel panel) {
        final JSlider jslider = new JSlider(min, max, value);
        jslider.setMajorTickSpacing(major);
        if (minor != null) {
            jslider.setMinorTickSpacing(minor);
        }
        jslider.setPaintTicks(true);
        jslider.setPaintLabels(true);
        jslider.setPaintTrack(true);
        jslider.setSnapToTicks(true);
        panel.add(jslider);
        return jslider;
    }

    @SuppressWarnings("serial")
    public static void main(String[] args) {
        try {
            UIUtils.systemLookAndFeel();
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            final BooleanValue run = new BooleanValue(true);
            final BooleanValue markers = new BooleanValue(false);
            final ProgressGlassPane glassPane = new ProgressGlassPane(frame) {
                @Override
                protected void progress() {
                    if (run.is()) super.progress();
                }

                @Override
                protected void paintExtra(Graphics2D g2d) {
                    if (markers.is()) {
                        // scale=95 @start
                        int x1 = scale * 8 / 100;
                        int y1 = scale * 2 / 100;

                        int y2 = scale * 10 / 100;
                        int x2 = scale * 50 / 100;

                        int x3 = scale * 55 / 100;
                        int y3 = scale * 4 / 100;

                        Polygon shape = new Polygon();
                        shape.addPoint(x1, -y1);
                        shape.addPoint(x2, -y2);
                        shape.addPoint(x3, -y3);
                        shape.addPoint(x3, y3);
                        shape.addPoint(x2, y2);
                        shape.addPoint(x1, y1);

                        g2d.setColor(Color.red);
                        g2d.setStroke(new BasicStroke(2f));

                        g2d.drawPolygon(shape);

                        g2d.setColor(Color.red);
                        g2d.drawLine(x1 - 3, -y1, x1 + 3, -y1);
                        g2d.drawLine(x1, -y1 - 3, x1, -y1 + 3);

                        g2d.setColor(Color.green);
                        g2d.drawLine(x2 - 3, -y2, x2 + 3, -y2);
                        g2d.drawLine(x2, -y2 - 3, x2, -y2 + 3);

                        g2d.setColor(Color.blue);
                        g2d.drawLine(x3 - 3, -y3, x3 + 3, -y3);
                        g2d.drawLine(x3, -y3 - 3, x3, -y3 + 3);

                        g2d.setColor(Color.black);
                        g2d.drawLine(0, 0, 200, 0);
                        g2d.drawLine(100, 50, 100, -50);
                        g2d.drawLine(0, 25, 0, -25);

                        g2d.setColor(Color.red);
                        int dd = (y2) * 2;
                        int xx = x2 - dd / 2;
                        int yy = -(y2);
                        g2d.fillArc(xx, yy, dd, dd, -90, 180);
                    }
                }
            };
            {
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                glassPane.setMessage("message"); //$NON-NLS-1$
                frame.setGlassPane(glassPane);
                SwingUtilities.invokeLater(() -> {
                    frame.setSize(300, 300);
                    frame.setLocationRelativeTo(null);
                    frame.setLocation(frame.getLocation().x - 200, frame.getLocation().y);
                    frame.setVisible(true);
                    glassPane.setVisible(true);
                });
            }
            {
                final JFrame frame2 = new JFrame();
                frame2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                JPanel panel = new JPanel(new GridLayout(-1, 1));
                {
                    JCheckBox enabled = new JCheckBox("enabled", true);
                    enabled.addActionListener(e -> {
                        System.out.println("en/dis-abling glass");
                        glassPane.setVisible(!glassPane.isVisible());
                    });
                    panel.add(enabled);
                }
                {
                    JCheckBox enabled = new JCheckBox("running", run.is());
                    enabled.addActionListener(e -> {
                        if (!run.toggle()) {
                            glassPane.setProgress(0);
                        }
                    });
                    panel.add(enabled);
                }
                {
                    JCheckBox enabled = new JCheckBox("markers", markers.is());
                    enabled.addActionListener(e -> markers.toggle());
                    panel.add(enabled);
                }
                {
                    final JTextField message = new JTextField(glassPane.getMessage());
                    message.getDocument().addDocumentListener(new DocumentListener() {
                        private void changed() {
                            glassPane.setMessage(message.getText());
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            changed();
                        }

                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            changed();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            changed();
                        }
                    });
                    panel.add(message);
                }
                {
                    panel.add(new ELabel("speed"));
                    final JSlider slider = GlassPaneDemo.addSlider(10, 200, glassPane.getSpeed(), 40, 10, panel);
                    slider.addChangeListener(e -> glassPane.setSpeed(slider.getValue()));
                }
                {
                    panel.add(new ELabel("count"));
                    final JSlider slider = GlassPaneDemo.addSlider(3, 15, glassPane.getCount(), 1, null, panel);
                    slider.addChangeListener(e -> glassPane.setCount(slider.getValue()));
                }
                {
                    panel.add(new ELabel("scale"));
                    final JSlider slider = GlassPaneDemo.addSlider(25, 400, glassPane.getScale(), 25, 5, panel);
                    int scale = glassPane.getScale();
                    int x1 = scale * 8 / 100;
                    int y1 = scale * 2 / 100;
                    int x2 = scale * 50 / 100;
                    int y2 = scale * 10 / 100;
                    int x3 = scale * 55 / 100;
                    int y3 = scale * 4 / 100;
                    ELabel p1 = new ELabel("   ( " + x1 + " , " + y1 + " ) = ( scale * 8 / 100 , scale * 2 / 100 )");
                    ELabel p2 = new ELabel("   ( " + x2 + " , " + y2 + " ) = ( scale * 50 / 100 , scale * 10 / 100 )");
                    ELabel p3 = new ELabel("   ( " + x3 + " , " + y3 + " ) = ( scale * 55 / 100 , scale * 4 / 100 )");
                    slider.addChangeListener(e -> {
                        int s = slider.getValue();
                        glassPane.setScale(s);
                        int x1a = s * 8 / 100;
                        int y1a = s * 2 / 100;
                        int y2a = s * 10 / 100;
                        int x2a = s * 50 / 100;
                        int x3a = s * 55 / 100;
                        int y3a = s * 4 / 100;
                        p1.setText("   ( " + x1a + " , " + y1a + " ) = ( scale * 8 / 100 , scale * 2 / 100 )");
                        p2.setText("   ( " + x2a + " , " + y2a + " ) = ( scale * 50 / 100 , scale * 10 / 100 )");
                        p3.setText("   ( " + x3a + " , " + y3a + " ) = ( scale * 55 / 100 , scale * 4 / 100 )");
                    });
                    panel.add(p1);
                    panel.add(p2);
                    panel.add(p3);
                }
                frame2.getContentPane().add(panel, BorderLayout.SOUTH);
                SwingUtilities.invokeLater(() -> {
                    frame2.setSize(200, 600);
                    frame2.setLocationRelativeTo(null);
                    frame2.setLocation(frame2.getLocation().x + 200, frame2.getLocation().y);
                    frame2.setVisible(true);
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
