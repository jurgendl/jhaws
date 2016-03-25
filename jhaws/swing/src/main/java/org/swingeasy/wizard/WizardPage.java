package org.swingeasy.wizard;

import javax.swing.JComponent;

/**
 * @author Jurgen
 */
public abstract class WizardPage {
    private String title;

    private String description;

    public WizardPage() {
        super();
    }

    public WizardPage(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public abstract JComponent createComponent();

    public String getDescription() {
        return this.description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.title;
    }

    /**
     * return false to make going forward/finishing the wizard impossible
     */
    public abstract boolean validate();
}
