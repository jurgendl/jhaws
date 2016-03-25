package org.swingeasy;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * @author Jurgen
 */
public class ECheckBoxList extends EList<Boolean> {
    public static class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer<EListRecord<Boolean>> {
        private static final long serialVersionUID = -8324269006694944760L;

        @Override
        public Component getListCellRendererComponent(JList<? extends EListRecord<Boolean>> list, EListRecord<Boolean> record, int index,
                boolean isSelected, boolean cellHasFocus) {
            boolean selected = (record != null) && Boolean.TRUE.equals(record.get());
            this.setText(record == null ? null : record.getStringValue());
            this.setSelected(selected);
            this.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            this.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            this.setEnabled(list.isEnabled());
            this.setFont(list.getFont());
            this.setFocusPainted(false);
            this.setBorderPainted(true);
            this.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : new EmptyBorder(1, 1, 1, 1));
            return this;
        }
    }

    public static class ECheckBoxListRecord extends EListRecord<Boolean> {
        protected String stringValue;

        public ECheckBoxListRecord() {
            this(Boolean.FALSE);
        }

        public ECheckBoxListRecord(Boolean b) {
            super(b);
        }

        public ECheckBoxListRecord(String stringValue) {
            this();
            this.stringValue = stringValue;
        }

        public ECheckBoxListRecord(String stringValue, Boolean b) {
            super(b);
            this.stringValue = stringValue;
        }

        /**
         * @see org.swingeasy.EListRecord#getStringValue()
         */
        @Override
        public String getStringValue() {
            return this.stringValue == null ? String.valueOf(this.bean) : this.stringValue;
        }

        /**
         * @see org.swingeasy.EListRecord#getTooltip()
         */
        @Override
        public String getTooltip() {
            return this.stringValue == null ? super.getStringValue() : this.getStringValue() + "=" + this.get();
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }
    }

    private static final long serialVersionUID = -7693596266102649322L;

    protected ECheckBoxList() {
        super();
    }

    public ECheckBoxList(EListConfig cfg) {
        super(cfg);
    }

    /**
     * @see org.swingeasy.EList#init(org.swingeasy.EListConfig)
     */
    @Override
    protected void init(EListConfig config) {
        super.init(config);

        this.setCellRenderer(new CheckBoxListCellRenderer());

        this.setTransferHandler(new EListTransferHandler<Boolean>() {
            private static final long serialVersionUID = 5134392681200624282L;

            // @Override
            // protected EListRecord<Boolean> newEListRecord(Boolean data) {
            // return new ECheckBoxListRecord(data);
            // }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = ECheckBoxList.this.locationToIndex(e.getPoint());
                if (index == -1) {
                    return;
                }
                Object value = ECheckBoxList.this.getModel().getElementAt(index);
                ECheckBoxListRecord record = ECheckBoxListRecord.class.cast(value);
                record.set(!Boolean.TRUE.equals(record.get()));
                ECheckBoxList.this.repaint();
            }
        });
    }
}
