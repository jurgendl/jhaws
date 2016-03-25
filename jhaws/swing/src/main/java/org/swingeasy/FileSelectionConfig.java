package org.swingeasy;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @author Jurgen
 */
public class FileSelectionConfig implements FileChooserCustomizer {
    public static final String ALL_EXT = "*";

    protected String[] ext = null;

    protected Icon icon = null;

    protected boolean directory = true;

    protected File defaultSelected;

    protected String description;

    protected FileFilter filter;

    protected boolean editable = false;

    /**
     * select directory; default directory to open = current directory
     */
    public FileSelectionConfig() {
        this((File) null);
    }

    /**
     * select directory; default directory to open = defaultSelected which can be null
     */
    public FileSelectionConfig(File defaultSelected) {
        this.defaultSelected = defaultSelected;
        this.directory = true;
        this.ext = null;
        this.icon = UIUtils.getIconForDirectory();

        this.description = UIUtils.getDescriptionForDirectory();

        this.filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return UIUtils.getDescriptionForDirectory();
            }
        };
    }

    /**
     * select file with extension (use {@link #ALL_EXT} for all files); defaulf file to open = defaultSelected which can be null
     */
    public FileSelectionConfig(File defaultSelected, String ext) {
        this.defaultSelected = defaultSelected;
        this.directory = false;
        this.ext = new String[] { ext };
        this.icon = UIUtils.getIconForFileType(ext);

        this.description = UIUtils.getDescriptionForFileType(ext);

        this.filter = new ExtensionFileFilter(UIUtils.getDescriptionForFileType(ext) + " (" + ext + ")", ext);
    }

    /**
     * select file with any extension; default directory to open = defaultSelected which can be null
     */
    public FileSelectionConfig(File defaultSelected, String... ext) {
        this.defaultSelected = defaultSelected;
        this.directory = false;
        this.ext = ext;
        this.icon = UIUtils.getIconForFile();

        StringBuilder sb = new StringBuilder();
        for (String element : ext) {
            sb.append(UIUtils.getDescriptionForFileType(element)).append(" (").append(element).append(")").append(", ");
        }
        this.description = sb.substring(0, sb.length() - 2);

        this.filter = new ExtensionFileFilter(Arrays.asList(ext));
    }

    /**
     * select file with any extension
     */
    public FileSelectionConfig(String... ext) {
        this((File) null, ext);
    }

    /**
     * select file with extension (use {@link #ALL_EXT} for all files)
     */
    public FileSelectionConfig(String ext) {
        this((File) null, ext);
    }

    @Override
    public void customize(Component parentComponent, JDialog dialog) {
        dialog.setIconImage(this.iconToImage(this.icon));
    }

    @Override
    public void customize(JFileChooser fileChooser) {
        fileChooser.resetChoosableFileFilters();
        fileChooser.addChoosableFileFilter(this.filter);
        fileChooser.setFileSelectionMode(this.directory ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(this.defaultSelected);
    }

    public File getDefaultSelected() {
        return this.defaultSelected;
    }

    public String getDescription() {
        return this.description;
    }

    public String[] getExt() {
        return this.ext;
    }

    public FileFilter getFilter() {
        return this.filter;
    }

    public Icon getIcon() {
        return this.icon;
    }

    protected Image iconToImage(Icon i) {
        if (i instanceof ImageIcon) {
            return ((ImageIcon) i).getImage();
        }
        int w = i.getIconWidth();
        int h = i.getIconHeight();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        Graphics2D g = image.createGraphics();
        i.paintIcon(null, g, 0, 0);
        g.dispose();
        return image;
    }

    public boolean isDirectory() {
        return this.directory;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public FileSelectionConfig setDefaultSelected(File defaultSelected) {
        this.defaultSelected = defaultSelected;
        return this;
    }

    public FileSelectionConfig setDescription(String description) {
        this.description = description;
        return this;
    }

    public FileSelectionConfig setDirectory(boolean directory) {
        this.directory = directory;
        return this;
    }

    public FileSelectionConfig setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public FileSelectionConfig setExt(String ext) {
        this.ext = ext == null ? null : new String[] { ext };
        return this;
    }

    public FileSelectionConfig setExt(String[] ext) {
        this.ext = ext;
        return this;
    }

    public FileSelectionConfig setFilter(FileFilter filter) {
        this.filter = filter;
        return this;
    }

    public FileSelectionConfig setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }
}
