package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

    public static void main(String[] args) {
        try {
            UIUtils.systemLookAndFeel();
            final JFrame frame = new JFrame();
            final ProgressGlassPane glassPane = new ProgressGlassPane(frame);
            {
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                glassPane.setMessage("message"); //$NON-NLS-1$
                frame.setGlassPane(glassPane);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setSize(200, 200);
                        frame.setVisible(true);
                    }
                });
            }
            {
                final JFrame frame2 = new JFrame();
                frame2.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                JPanel panel = new JPanel(new GridLayout(-1, 1));
                {
                    JCheckBox enabled = new JCheckBox("enabled", glassPane.isVisible());
                    enabled.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("en/dis-abling glass");
                            glassPane.setVisible(!glassPane.isVisible());
                        }
                    });
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
                            this.changed();
                        }

                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            this.changed();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            this.changed();
                        }
                    });
                    panel.add(message);
                }
                {
                    final JSlider slider = GlassPaneDemo.addSlider(10, 200, glassPane.getSpeed(), 40, 10, panel);
                    slider.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            glassPane.setSpeed(slider.getValue());
                        }
                    });
                }
                {
                    final JSlider slider = GlassPaneDemo.addSlider(3, 15, glassPane.getCount(), 1, null, panel);
                    slider.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            glassPane.setCount(slider.getValue());
                        }
                    });
                }
                {
                    final JSlider slider = GlassPaneDemo.addSlider(25, 200, glassPane.getScale(), 25, 5, panel);
                    slider.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            glassPane.setScale(slider.getValue());
                        }
                    });
                }
                frame2.getContentPane().add(panel, BorderLayout.SOUTH);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame2.setSize(200, 300);
                        frame2.setLocation(200, 200);
                        frame2.setVisible(true);
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
