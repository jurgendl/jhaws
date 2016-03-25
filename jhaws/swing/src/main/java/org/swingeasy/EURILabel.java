package org.swingeasy;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.JLabel;

/**
 * label for url
 * 
 * @author Jurgen
 */
public class EURILabel extends JLabel {
    private static final long serialVersionUID = -4664937991392128340L;

    public EURILabel(URI uri) {
        this(uri, uri.toString());
    }

    public EURILabel(final URI uri, String text) {
        super("<html><a href=\"" + uri + "\">" + text + "</a></html>");
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
