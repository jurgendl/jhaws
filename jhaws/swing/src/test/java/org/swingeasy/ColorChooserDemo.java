package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class ColorChooserDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("ColorChooserDemo");
        JTextField value = new JTextField("new Color(255, 255, 255);");
        frame.getContentPane().add(value, BorderLayout.CENTER);
        frame.getContentPane().add(new JButton(new AbstractAction("Select color") {
            private static final long serialVersionUID = 4738796516880687169L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern p = Pattern.compile("new Color\\(\\s{0,}(\\d++)\\s{0,},\\s{0,}(\\d++)\\s{0,},\\s{0,}(\\d++)\\s{0,}\\)[;]{0,1}");
                Matcher m = p.matcher(value.getText());
                Color c = Color.white;
                if (m.find()) {
                    c = new Color(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
                }
                c = JColorChooser.showDialog(frame, "", c);
                if (c != null) value.setText("new Color(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ");");
            }
        }), BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
