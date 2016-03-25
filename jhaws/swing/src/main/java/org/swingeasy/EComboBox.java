package org.swingeasy;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;
import org.swingeasy.system.SystemSettings;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.DefaultEventComboBoxModel;

/**
 * @author Jurgen
 */
public class EComboBox<T> extends JComboBox<T> implements EComboBoxI<T>, Iterable<EComboBoxRecord<T>>, ReadableComponent, HasValue<T> {
    protected class MouseValueScroller implements MouseWheelListener {
        @Override
        public synchronized void mouseWheelMoved(MouseWheelEvent e) {
            int currentSelected = EComboBox.this.getSelectedIndex();
            final boolean nullFound = EComboBox.this.getModel().getElementAt(0) == null;
            if (currentSelected == -1) {
                currentSelected = nullFound ? 1 : 0;
                EComboBox.this.setSelectedIndex(currentSelected);
                return;
            }
            final int max = EComboBox.this.getItemCount() - 1;
            final boolean down = e.getWheelRotation() > 0;
            if (down) {
                currentSelected++;
                if (currentSelected > max) {
                    currentSelected = 0;
                }
                if (nullFound && (currentSelected == 0)) {
                    currentSelected = 1;
                }
            } else {
                currentSelected--;
                if (currentSelected < 0) {
                    currentSelected = max;
                }
                if (nullFound && (currentSelected == 0)) {
                    currentSelected = max;
                }
            }
            EComboBox.this.setSelectedIndex(currentSelected);
        }
    }

    /**
     * convert
     */
    public static <T> List<EComboBoxRecord<T>> convert(Collection<T> records) {
        List<EComboBoxRecord<T>> list = new ArrayList<EComboBoxRecord<T>>();
        for (T r : records) {
            list.add(new EComboBoxRecord<T>(r));
        }
        return list;
    }

    /**
     * convert
     */
    public static <T> List<EComboBoxRecord<T>> convert(T[] records) {
        return EComboBox.convert(Arrays.asList(records));
    }

    /**
     * convert
     */
    public static <T> List<T> convertRecords(Collection<EComboBoxRecord<T>> records) {
        List<T> list = new ArrayList<T>();
        for (EComboBoxRecord<T> r : records) {
            list.add(r.get());
        }
        return list;
    }

    /**
     * convert
     */
    public static <T> List<T> convertRecords(EComboBoxRecord<T>[] records) {
        return EComboBox.convertRecords(Arrays.asList(records));
    }

    private static final long serialVersionUID = -3602504810131193505L;

    protected EComboBoxConfig cfg;

    protected EventList<EComboBoxRecord<T>> records;

    protected MouseValueScroller mouseValueScroller = null;

    protected final List<ValueChangeListener<T>> valueChangeListeners = new ArrayList<ValueChangeListener<T>>();

    protected boolean layingOut = false;

    protected EComboBox<T> stsi;

    protected EComboBox() {
        super();
        this.cfg = null;
    }

    public EComboBox(EComboBoxConfig cfg) {
        this.init(cfg = cfg.lock());
    }

    /**
     *
     * @see org.swingeasy.EComboBoxI#activateAutoCompletion()
     */
    @Override
    public void activateAutoCompletion() {
        AutoCompleteSupport<EComboBoxRecord<T>> support = AutoCompleteSupport.install(this, this.records, new TextFilterator<EComboBoxRecord<T>>() {
            @Override
            public void getFilterStrings(List<String> baseList, EComboBoxRecord<T> element) {
                if (element == null) {
                    return;
                }
                baseList.add(element.getStringValue());
            }
        });
        support.setFilterMode(TextMatcherEditor.CONTAINS);
    }

    /**
     *
     * @see org.swingeasy.EComboBoxI#activateScrolling()
     */
    @Override
    public void activateScrolling() {
        if (this.mouseValueScroller == null) {
            this.mouseValueScroller = new MouseValueScroller();
            this.addMouseWheelListener(new MouseValueScroller());
        }
    }

    /**
     *
     * @see org.swingeasy.EComboBoxI#addRecord(org.swingeasy.EComboBoxRecord)
     */
    @Override
    public void addRecord(EComboBoxRecord<T> record) {
        this.records.add(record);
    }

    /**
     *
     * @see org.swingeasy.EComboBoxI#addRecords(java.util.Collection)
     */
    @Override
    public void addRecords(Collection<EComboBoxRecord<T>> eComboBoxRecords) {
        this.records.addAll(eComboBoxRecords);
    }

    /**
     *
     * @see org.swingeasy.HasValue#addValueChangeListener(org.swingeasy.ValueChangeListener)
     */
    @Override
    public void addValueChangeListener(ValueChangeListener<T> listener) {
        this.valueChangeListeners.add(listener);
    }

    /**
     *
     * @see org.swingeasy.HasValue#clearValueChangeListeners()
     */
    @Override
    public void clearValueChangeListeners() {
        this.valueChangeListeners.clear();
    }

    /**
     *
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
     */
    @Override
    public void copy(ActionEvent e) {
        StringBuilder sb = new StringBuilder();
        for (EComboBoxRecord<T> record : this) {
            sb.append(record.getStringValue()).append(SystemSettings.getNewline());
        }
        EComponentPopupMenu.copyToClipboard(sb.toString());
    }

    /**
     *
     * @see org.swingeasy.EComboBoxI#deactivateScrolling()
     */
    @Override
    public void deactivateScrolling() {
        if (this.mouseValueScroller != null) {
            this.removeMouseWheelListener(this.mouseValueScroller);
            this.mouseValueScroller = null;
        }
    }

    /**
     *
     * @see java.awt.Container#doLayout()
     */
    @Override
    public void doLayout() {
        try {
            this.layingOut = true;
            super.doLayout();
        } finally {
            this.layingOut = false;
        }
    }

    public EComboBox<T> getOriginal() {
        return this;
    }

    /**
     *
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this;
    }

    /**
     *
     * @see org.swingeasy.EComboBoxI#getRecords()
     */
    @Override
    public List<EComboBoxRecord<T>> getRecords() {
        return this.records;
    }

    /**
     *
     * @see org.swingeasy.EComboBoxI#getSelectedRecord()
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EComboBoxRecord<T> getSelectedRecord() {
        return (EComboBoxRecord) this.getSelectedItem();
    }

    /**
     * JDOC
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public EComboBox<T> getSimpleThreadSafeInterface() {
        try {
            if (this.stsi == null) {
                this.stsi = EventThreadSafeWrapper.getSimpleThreadSafeInterface(EComboBox.class, this, EComboBoxI.class);
            }
            return this.stsi;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @see java.awt.Component#getSize()
     */
    @Override
    public Dimension getSize() {
        Dimension dim = super.getSize();
        if ((this.cfg != null) && this.cfg.isAutoResizePopup() && !this.layingOut) {
            dim.width = Math.max(dim.width, this.getPreferredSize().width);
        }
        return dim;
    }

    /**
     *
     * @see javax.swing.JComponent#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        String toolTipText = super.getToolTipText();
        if (toolTipText == null) {
            if (this.getSelectedRecord() == null) {
                return null;
            }

            return this.getSelectedRecord().getTooltip();
        }
        return toolTipText;
    }

    /**
     *
     * @see org.swingeasy.HasValue#getValue()
     */
    @Override
    public T getValue() {
        EComboBoxRecord<T> selectedRecord = this.getSelectedRecord();
        return selectedRecord == null ? null : selectedRecord.get();
    }

    @SuppressWarnings("unchecked")
    protected void init(EComboBoxConfig config) {
        this.records = new BasicEventList<EComboBoxRecord<T>>();

        if (config.isSortable()) {
            this.records = new SortedList<EComboBoxRecord<T>>(this.records);
        }

        if (config.isThreadSafe()) {
            this.records = GlazedLists.threadSafeList(this.records);
        }

        DefaultEventComboBoxModel<EComboBoxRecord<T>> model = new DefaultEventComboBoxModel<EComboBoxRecord<T>>(this.records);

        this.setModel(model);

        if (config.isAutoComplete()) {
            this.stsi().activateAutoCompletion();
        }

        if (config.isScrolling()) {
            this.activateScrolling();
        }

        if (config.isLocalized()) {
            UIUtils.registerLocaleChangeListener((EComponentI) this);
        }

        if (config.isDefaultPopupMenu()) {
            this.installPopupMenuAction(EComponentPopupMenu.installPopupMenu(this));
        }

        if (config.isTooltips()) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        this.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    T value = EComboBox.this.getValue();
                    for (ValueChangeListener<T> valueChangeListener : EComboBox.this.valueChangeListeners) {
                        valueChangeListener.valueChanged(value);
                    }
                }
            }
        });

        if (config.isAutoResizePopup()) {
            this.addPopupMenuListener(new EComboBoxAutoResizingPopupListener());
        }
    }

    protected void installPopupMenuAction( EComponentPopupMenu menu) {
        //
    }

    /**
     * threadsafe unmodifiable iterator
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<EComboBoxRecord<T>> iterator() {
        return Collections.unmodifiableCollection(this.getRecords()).iterator();
    }

    /**
     *
     * @see org.swingeasy.EComboBoxI#removeAllRecords()
     */
    @Override
    public void removeAllRecords() {
        this.records.clear();
    }

    /**
     *
     * @see org.swingeasy.EComboBoxI#removeRecord(org.swingeasy.EComboBoxRecord)
     */
    @Override
    public void removeRecord(EComboBoxRecord<T> record) {
        this.records.remove(record);
    }

    /**
     *
     * @see org.swingeasy.HasValue#removeValueChangeListener(org.swingeasy.ValueChangeListener)
     */
    @Override
    public void removeValueChangeListener(ValueChangeListener<T> listener) {
        this.valueChangeListeners.remove(listener);
    }

    /**
     *
     * @see org.swingeasy.ETableI#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.repaint();
    }

    /**
     *
     * @see org.swingeasy.EComboBoxI#setSelectedRecord(org.swingeasy.EComboBoxRecord)
     */
    @Override
    public void setSelectedRecord(EComboBoxRecord<T> record) {
        this.setSelectedItem(record);
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public EComboBox<T> stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public EComboBox<T> STSI() {
        return this.getSimpleThreadSafeInterface();
    }
}
