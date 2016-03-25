package org.swingeasy;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TooManyListenersException;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.TransferHandler;

import org.swingeasy.tab.DnDTabbedPane;
import org.swingeasy.tab.DnDTabbedPaneDropTargetListener;
import org.swingeasy.tab.TabTransferHandler;

/**
 * @author Jurgen
 */
public class ETabbedPane extends DnDTabbedPane {
    public static class ETabToolbar extends ETabbedPane {

        private static final long serialVersionUID = -5439842646697045080L;

        public ETabToolbar() {
            super(new ETabbedPaneConfig(false, false));
        }

        @Override
        public void insertTab(String title, Icon icon, Component component, String tip, int index) {
            if (!(component instanceof ETabToolbarComponent)) {
                component = new ETabToolbarComponent(component);
            }
            super.insertTab(title, icon, component, tip, index);
        }
    }

    public static class ETabToolbarComponent extends JComponent {
        private static final long serialVersionUID = 1262015552071744215L;

        public final Component hidden;

        public ETabToolbarComponent(Component hidden) {
            this.hidden = hidden;
        }
    }

    private static final long serialVersionUID = -46108541490198511L;

    protected ETabbedPaneConfig config;

    protected static DropTargetListener DROP_TARGET_LISTENER = new DnDTabbedPaneDropTargetListener();

    protected static TransferHandler TRANSFER_HANDLER = new TabTransferHandler<ETabbedPane>(ETabbedPane.class);

    protected ETabToolbar parent;

    public ETabbedPane() {
        this(new ETabbedPaneConfig());
    }

    public ETabbedPane(ETabbedPaneConfig cfg) {
        this.config = cfg.lock();
        this.register();
        Dimension dim = new Dimension(20, 20); // TODO minimum height (=width for vertical) => how do we calculate this?
        this.setMinimumSize(dim);
        this.setPreferredSize(dim);
        this.setSize(dim);
    }

    /**
     * 
     * @see javax.swing.JTabbedPane#insertTab(java.lang.String, javax.swing.Icon, java.awt.Component, java.lang.String, int)
     */
    @Override
    public void insertTab(final String title, final Icon icon, Component component, final String tip, final int index) {
        if ((component instanceof ETabToolbarComponent) && !(this instanceof ETabToolbar)) {
            component = ETabToolbarComponent.class.cast(component).hidden;
        }
        final Component _component = component;
        super.insertTab(title, icon, _component, tip == null ? title : tip, index);
        this.setTabComponentAt(index, new ETabbedPaneHeader(title, icon, tip, this.config, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ETabbedPaneHeader.ACTION_CLOSE.equals(e.getActionCommand())) {
                    ETabbedPane.this.remove(_component);
                } else if (ETabbedPaneHeader.ACTION_MINIMIZE.equals(e.getActionCommand())) {
                    ETabbedPane.this.remove(_component);
                    ETabbedPane.this.parent.addTab(title, icon, _component, tip);
                } else {
                    throw new UnsupportedOperationException(e.getActionCommand());
                }
            }
        }));
    }

    protected void register() {
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.setTransferHandler(ETabbedPane.TRANSFER_HANDLER);
        try {
            this.getDropTarget().addDropTargetListener(ETabbedPane.DROP_TARGET_LISTENER);
        } catch (TooManyListenersException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setMinimizeTo(ETabToolbar parent) {
        this.parent = parent;
    }
}
