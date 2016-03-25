package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class TextAreaDemo {
    private static ETextArea addComponents(Container container) throws IOException {
        String text = new String(Resources.getResource("org/swingeasy/resources/swing-easy.properties"));
        ETextArea jtf = new ETextArea(new ETextAreaConfig(), text);
        container.add(jtf.withLineNumbers());
        container.add(jtf.getToolbar(), BorderLayout.NORTH);
        EComponentPopupMenu.debug(jtf);
        return jtf;
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        ETextArea tc = null;
        try {
            tc = TextAreaDemo.addComponents(frame.getContentPane());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setTitle("TextAreaDemo");
        frame.setVisible(true);
    }
}
