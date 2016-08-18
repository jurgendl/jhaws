package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class FontChooserDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("Demo");
        frame.setSize(400, 400);
        frame.setLocation(400, 400);

        EButton fc = new EButton(new EButtonConfig("Font Chooser Dialog"));
        fc.addActionListener(e -> showChooser(frame));
        frame.getContentPane().add(fc, BorderLayout.CENTER);

        frame.setVisible(true);
        showChooser(frame);
    }

    private static void showChooser(JFrame frame) {
        Font font = EFontChooser.showDialog(JComponent.class.cast(frame.getContentPane()), frame.getFont());
        if (font == null) {
            return;
        }
        frame.setFont(font);
    }
}
