package org.swingeasy;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Jurgen
 */
public class PopupDemo {
    private static void addComponents(Container container) {
        EListConfig cfg = new EListConfig();
        cfg.setFilterable(true);
        final EList<String> options = new EList<String>(cfg);
        for (int i = 0; i < 26; i++) {
            options.stsi().addRecord(new EListRecord<String>((char) ('a' + i) + " option " + i));
        }
        options.setDefaultRenderer(Object.class, new DefaultListCellRenderer() {
            private static final long serialVersionUID = -2332228089292536681L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                value = "<html><i>" + value + "</i><html>";
                DefaultListCellRenderer listCellRendererComponent = (DefaultListCellRenderer) super.getListCellRendererComponent(list, value, index,
                        isSelected, cellHasFocus);
                return listCellRendererComponent;
            }
        });
        EListFilterComponent<String> filtercomponent = options.getFiltercomponent();
        filtercomponent.setLive(true);
        final ETextField input = filtercomponent.getInput();
        final JPopupMenu popup = new JPopupMenu() {
            private static final long serialVersionUID = 2839148329518264654L;

            @Override
            public void setVisible(boolean b) {
                if (b) {
                    new RuntimeException().printStackTrace(System.out);
                }
                super.setVisible(b);
            }
        };
        options.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if ((ev.getClickCount() == 1) && (ev.getButton() == MouseEvent.BUTTON1)) {
                    input.setText(String.valueOf(options.getSelectedValue()));
                    popup.setVisible(false);
                }
            }
        });
        popup.setBorderPainted(false);
        popup.add(new JScrollPane(options));
        popup.setLightWeightPopupEnabled(true);
        container.add(input);
        input.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ev) {
                if ((ev.getClickCount() == 1) && (ev.getButton() == MouseEvent.BUTTON1)) {
                    PopupDemo.show(options, popup);
                }
            }
        });
        input.getDocument().addDocumentListener(new DocumentListener() {
            public void change() {
                PopupDemo.show(options, popup);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                this.change();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                this.change();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                this.change();
            }
        });
        input.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                PopupDemo.show(options, popup);
            }

            @Override
            public void focusLost(FocusEvent e) {
                popup.setVisible(false);
            }
        });
        input.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                //
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                PopupDemo.reloc(input, popup);
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                //
            }
        });
        input.addComponentListener(new ComponentListener() {
            @Override
            public void componentHidden(ComponentEvent e) {
                //
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                PopupDemo.reloc(input, popup);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                PopupDemo.reloc(input, popup);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                //
            }
        });
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container cp = frame.getContentPane();
        cp.setLayout(new GridLayout(-1, 1));
        final JTextField dummy = new JTextField("whut?");
        cp.add(dummy);
        PopupDemo.addComponents(cp);
        for (int i = 0; i < 8; i++) {
            cp.add(new JTextField("whut?"));
        }
        frame.setTitle("PopupDemo");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.pack();
                frame.setSize(new Dimension(Math.max(400, frame.getWidth()), frame.getHeight()));
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    private static void reloc(ETextField input, final JPopupMenu popup) {
        if (popup.isVisible()) {
            try {
                Dimension dim = new Dimension(input.getWidth(), popup.getPreferredSize().height);
                popup.setSize(dim);
                popup.setPreferredSize(dim);
                int x = (input.getLocationOnScreen().x + input.getWidth()) - (int) popup.getPreferredSize().getWidth();
                int y = input.getLocationOnScreen().y + input.getHeight();
                Point loc = new Point(x, y);
                popup.setLocation(loc);
                popup.setVisible(false);
                popup.setVisible(true);
            } catch (Exception ex) {
                //
            }
        }
    }

    private static void show(final EList<String> options, final JPopupMenu popup) {
        if (!popup.isVisible() && (options.getRecordCount() > 1)) {
            popup.setVisible(true);
        }
    }
}
