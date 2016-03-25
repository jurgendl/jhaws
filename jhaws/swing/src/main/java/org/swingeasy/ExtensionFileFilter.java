package org.swingeasy;

import java.io.File;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * @author Jurgen
 */
public class ExtensionFileFilter extends FileFilter {
    protected static String descriptionFromExtensions(String[] exts) {
        if ((exts == null) || (exts.length == 0)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (String extension : exts) {
            sb.append(extension).append(", ");
        }

        sb.deleteCharAt(sb.length() - 1);

        String string = sb.deleteCharAt(sb.length() - 1).toString();

        return string;
    }

    protected String description;

    protected String[] extensions;

    public ExtensionFileFilter(final List<String> extensions) {
        this.extensions = extensions.toArray(new String[extensions.size()]);
        this.description = ExtensionFileFilter.descriptionFromExtensions(this.extensions);
    }

    public ExtensionFileFilter(final String description, final List<String> extensions) {
        this.extensions = extensions.toArray(new String[extensions.size()]);
        this.description = description;
    }

    public ExtensionFileFilter(final String description, final String... extensions) {
        if (extensions.length == 0) {
            this.extensions = new String[] { description };
            this.description = ExtensionFileFilter.descriptionFromExtensions(extensions);
        } else {
            this.extensions = extensions;
            this.description = description;
        }
    }

    /**
     * 
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        for (String extension : this.extensions) {
            String _extension = extension.replaceAll("\\[", "").replaceAll("\\]", "");
            if (f.getName().toLowerCase().endsWith("." + _extension.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    public String[] getExtensions() {
        return this.extensions;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }
}
