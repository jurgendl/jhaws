package org.swingeasy;

import java.awt.Container;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;

/**
 * @author Jurgen
 */
public class TextFieldDemo {
    private static ETextField addComponents(Container container) throws IOException {
        ETextField jtf = new ETextField(new ETextFieldConfig("text"));
        container.add(new JScrollPane(jtf));
        EComponentPopupMenu.debug(jtf);
        jtf.addDocumentKeyListener(new DocumentKeyListener() {
            @Override
            public void update(Type type, DocumentEvent e) {
                System.out.println(type + ":" + e);
            }
        });
        return jtf;
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        ETextField tc = null;
        try {
            tc = TextFieldDemo.addComponents(frame.getContentPane());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        frame.setSize(400, 100);
        frame.setLocationRelativeTo(null);
        frame.setTitle("TextFieldDemo");
        frame.setVisible(true);
    }
}
