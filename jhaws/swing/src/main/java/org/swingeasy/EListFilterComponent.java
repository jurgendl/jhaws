package org.swingeasy;

import java.util.List;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTextField;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

/**
 * @author Jurgen
 */
public class EListFilterComponent<T> extends ELabeledTextFieldButtonComponent implements TextFilterator<EListRecord<T>> {

    private static final long serialVersionUID = -8699648472825404199L;

    protected EList<T> eList;

    protected EListI<T> sList;

    protected EventList<EListRecord<T>> tmprecords;

    protected TextComponentMatcherEditor<EListRecord<T>> filter;

    public EListFilterComponent(EventList<EListRecord<T>> records) {
        this.createComponent();
        this.filter = new TextComponentMatcherEditor<EListRecord<T>>(JTextField.class.cast(this.getInput()), this, false);
        this.tmprecords = new FilterList<EListRecord<T>>(records, this.filter);
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#doAction()
     */
    @Override
    protected void doAction() {
        this.filter.setFilterText(new String[] { JTextField.class.cast(this.getInput()).getText() });
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#getAction()
     */
    @Override
    protected String getAction() {
        return "filter";
    }

    /**
     * 
     * @see ca.odell.glazedlists.TextFilterator#getFilterStrings(java.util.List, java.lang.Object)
     */
    @Override
    public void getFilterStrings(List<String> baseList, EListRecord<T> element) {
        if (element != null) {
            baseList.add(element.getStringValue());
        }
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#getIcon()
     */
    @Override
    protected Icon getIcon() {
        return Resources.getImageResource("magnifier.png");
    }

    /**
     * 
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this;
    }

    protected EventList<EListRecord<T>> grabRecords() {
        EventList<EListRecord<T>> _records = this.tmprecords;
        this.tmprecords = null;
        return _records;
    }

    /**
     * @see ca.odell.glazedlists.swing.TextComponentMatcherEditor#isLive()
     */
    public boolean isLive() {
        return this.filter.isLive();
    }

    protected void setList(EList<T> eList) {
        this.eList = eList;
        this.sList = eList.stsi();
    }

    /**
     * @see ca.odell.glazedlists.swing.TextComponentMatcherEditor#setLive(boolean)
     */
    public void setLive(boolean live) {
        this.filter.setLive(live);
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.getButton().setToolTipText(Messages.getString(l, "EList.FilterComponent.filter"));//$NON-NLS-1$
        this.getLabel().setText(Messages.getString(l, "EList.FilterComponent.filter") + ": ");//$NON-NLS-1$ //$NON-NLS-2$
    }
}
