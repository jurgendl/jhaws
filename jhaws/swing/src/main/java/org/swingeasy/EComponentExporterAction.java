package org.swingeasy;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import org.swingeasy.EComponentPopupMenu.CheckEnabled;
import org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction;
import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public class EComponentExporterAction<T extends JComponent & EComponentI & ReadableComponent> extends EComponentPopupMenuAction<T> {
    private static final long serialVersionUID = 5801982050032014321L;

    protected final EComponentExporter<T> exporter;

    public EComponentExporterAction(EComponentExporter<T> exporter, T component) {
        super(component, exporter.getAction(), exporter.getIcon());
        this.exporter = exporter;
    }

    /**
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.exporter.export(this.delegate);
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
     */
    @Override
    public boolean checkEnabled(CheckEnabled cfg) {
        this.setEnabled(cfg.hasText);
        return cfg.hasText;
    }
}
