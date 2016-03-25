package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.filechooser.FileFilter;

/**
 * @author Jurgen
 */
public class FileSelection extends JPanel implements ActionListener {
    private static final long serialVersionUID = 8303141891829028529L;

    protected FileSelectionConfig cfg;

    protected ETextField textfield;

    protected FileFilter filter;

    public FileSelection(final FileSelectionConfig config) {
        super(new BorderLayout());
        this.cfg = config;
        this.textfield = new ETextField(
                new ETextFieldConfig(config.getDefaultSelected() == null ? "" : config.getDefaultSelected().getAbsolutePath()).setSelectAllOnFocus(
                        true).setEnabled(config.isEditable()));
        this.textfield.setDragEnabled(true);
        this.textfield.setTransferHandler(new TransferHandler("file") {
            private static final long serialVersionUID = 76844729202962516L;

            @Override
            public boolean canImport(TransferSupport support) {
                return Arrays.asList(support.getDataFlavors()).contains(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    @SuppressWarnings("unchecked")
                    List<File> data = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    ETextField component = (ETextField) support.getComponent();
                    for (File f : data) {
                        if ((config.isDirectory() == f.isDirectory()) && FileSelection.this.filter.accept(f)) {
                            component.setText(f.getAbsolutePath());
                            break;
                        }
                    }
                } catch (UnsupportedFlavorException ex) {
                    return false;
                } catch (IOException ex) {
                    return false;
                }
                return false;
            }
        });
        this.add(this.textfield);
        EButton select = new EButton(new EButtonConfig(new EIconButtonCustomizer(config.getIcon())));
        select.setToolTipText("<html>" + config.getDescription() + "</html>");
        select.addActionListener(this);
        this.add(select, BorderLayout.EAST);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.textfield.grabFocus();
        File file = CustomizableOptionPane.showFileChooserDialog(this, FileChooserType.OPEN, this.cfg);
        if (file != null) {
            this.textfield.setText(file.getAbsolutePath());
        }
    }

    public File getSelectedFile() {
        return new File(this.textfield.getText());
    }

    public String getText() {
        return this.textfield.getText();
    }
}
