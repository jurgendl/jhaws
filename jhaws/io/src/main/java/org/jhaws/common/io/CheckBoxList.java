package org.jhaws.common.io;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class CheckBoxList extends JList<JCheckBox> {
    protected class CellRenderer implements ListCellRenderer<JCheckBox> {
        @Override
        public Component getListCellRendererComponent(JList<? extends JCheckBox> list, JCheckBox checkbox, int index, boolean isSelected, boolean cellHasFocus) {
            checkbox.setBackground(isSelected ? CheckBoxList.this.getSelectionBackground() : CheckBoxList.this.getBackground());
            checkbox.setForeground(isSelected ? CheckBoxList.this.getSelectionForeground() : CheckBoxList.this.getForeground());
            checkbox.setEnabled(CheckBoxList.this.isEnabled());
            checkbox.setFont(CheckBoxList.this.getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(true);
            checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : CheckBoxList.noFocusBorder);
            return checkbox;
        }
    }

    private static final long serialVersionUID = -6288650056105581228L;

    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public CheckBoxList() {
        this.setCellRenderer(new CellRenderer());

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = CheckBoxList.this.locationToIndex(e.getPoint());

                if (index != -1) {
                    JCheckBox checkbox = CheckBoxList.this.getModel().getElementAt(index);
                    checkbox.setSelected(!checkbox.isSelected());
                    CheckBoxList.this.repaint();
                }
            }
        });

        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void addCheckbox(JCheckBox checkBox) {
        ListModel<JCheckBox> currentList = this.getModel();
        JCheckBox[] newList = new JCheckBox[currentList.getSize() + 1];
        for (int i = 0; i < currentList.getSize(); i++) {
            newList[i] = currentList.getElementAt(i);
        }
        newList[newList.length - 1] = checkBox;
        this.setListData(newList);
    }
}