package org.swingeasy;

import java.awt.Component;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * @author Jurgen
 */
public class EComponentExporterFileChooserCustomizer<T extends JComponent & EComponentI> implements FileChooserCustomizer {
    protected final EComponentExporterImpl<T> componentExporter;

    protected static File lastFile = null;

    public EComponentExporterFileChooserCustomizer(EComponentExporterImpl<T> componentExporter) {
        this.componentExporter = componentExporter;
    }

    /**
     * 
     * @see org.swingeasy.FileChooserCustomizer#customize(java.awt.Component, javax.swing.JDialog)
     */
    @Override
    public void customize(Component parentComponent, JDialog dialog) {
        dialog.setLocationRelativeTo(null);
    }

    /**
     * 
     * @see org.swingeasy.FileChooserCustomizer#customize(javax.swing.JFileChooser)
     */
    @Override
    public void customize(JFileChooser jfc) {
        if (EComponentExporterFileChooserCustomizer.lastFile != null) {
            jfc.setCurrentDirectory(EComponentExporterFileChooserCustomizer.lastFile.isDirectory() ? EComponentExporterFileChooserCustomizer.lastFile
                    : EComponentExporterFileChooserCustomizer.lastFile.getParentFile());
        }
        jfc.resetChoosableFileFilters();
        jfc.addChoosableFileFilter(new ExtensionFileFilter(UIUtils.getDescriptionForFileType(this.componentExporter.getFileExtension()) + " ("
                + this.componentExporter.getFileExtension() + ")", this.componentExporter.getFileExtension()));
    }
}
