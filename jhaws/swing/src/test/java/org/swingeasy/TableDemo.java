package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.swingeasy.system.SystemSettings;
import org.swingeasy.table.editor.EnumTableCellEditor;

/**
 * @author Jurgen
 */
public class TableDemo {
    public static enum EnumTest {
        VALUE1, VALUE2, VALUE3, VALUE4, VALUE5, VALUE6, VALUE7, VALUE8;
    }

    public static void main(String[] args) {
        try {
            UIUtils.systemLookAndFeel();
            ETableConfig configuration = new ETableConfig(true);
            configuration.setVertical(true);
            final ETable<Object[]> table = new ETable<Object[]>(configuration);
            final ETableHeaders<Object[]> headers = new ETableHeaders<Object[]>();
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        if ((e.getClickCount() == 1) && (e.getButton() == MouseEvent.BUTTON1)) {
                            int row = table.rowAtPoint(e.getPoint());
                            if (row == -1) {
                                return;
                            }
                            int col = table.columnAtPoint(e.getPoint());
                            if (col == -1) {
                                return;
                            }
                            if ("15".equals("" + table.getRecordAtVisualRow(row).get(0))) { //$NON-NLS-1$ //$NON-NLS-2$
                                System.out.println("cellvalue 15"); //$NON-NLS-1$
                            }
                            if ("Integer".equals(table.getColumnValueAtVisualColumn(col))) { //$NON-NLS-1$
                                System.out.println("col Integer"); //$NON-NLS-1$
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            final ETableI<Object[]> safetable = table.getSimpleThreadSafeInterface();
            final JFrame frame = new JFrame();
            JScrollPane jsp = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            table.addRowHeader(jsp);
            Container cp = frame.getContentPane();
            cp.add(jsp, BorderLayout.CENTER);
            JPanel localepanel = new JPanel(new FlowLayout());
            final EButtonGroup localegroup = new EButtonGroup();
            JRadioButton en = new JRadioButton("en");//$NON-NLS-1$
            localepanel.add(en);
            localegroup.add(en);
            JRadioButton nl = new JRadioButton("nl");//$NON-NLS-1$
            localepanel.add(nl);
            localegroup.add(nl);
            localegroup.addPropertyChangeListener(EButtonGroup.SELECTION, new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    Locale l = new Locale(String.valueOf(evt.getNewValue()));
                    SystemSettings.setCurrentLocale(l);
                    table.repaint();
                }
            });
            cp.add(localepanel, BorderLayout.NORTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            final Random r = new Random(256955466579946l);
            headers.add("Integer", Integer.class, true); //$NON-NLS-1$
            headers.add("String", String.class, true); //$NON-NLS-1$
            headers.add("Boolean", Boolean.class, true); //$NON-NLS-1$
            headers.add("Date", Date.class, true); //$NON-NLS-1$
            headers.add("sql.Date", java.sql.Date.class, true); //$NON-NLS-1$
            headers.add("sql.Time", Time.class, true); //$NON-NLS-1$
            headers.add("Double", Double.class, true); //$NON-NLS-1$
            headers.add("Float", Float.class, true); //$NON-NLS-1$
            headers.add("Integer", Integer.class, true); //$NON-NLS-1$
            headers.add("Long", Long.class, true); //$NON-NLS-1$
            headers.add("BigDecimal", BigDecimal.class, true); //$NON-NLS-1$
            headers.add("BigInteger", BigInteger.class, true); //$NON-NLS-1$
            headers.add("Color", Color.class, true); //$NON-NLS-1$
            headers.add("Enum", EnumTest.class, true); //$NON-NLS-1$
            safetable.setHeaders(headers);
            Object[] empty = new Object[headers.getColumnCount()];
            safetable.addRecord(new ETableRecordArray<Object>(empty));
            for (int i = 0; i < 100; i++) {
                int next = r.nextInt(1000);
                long ts = new Date().getTime() + (next * 1000000l);
                safetable.addRecord(new ETableRecordArray<Object>(new Object[] {
                        next,
                        String.valueOf(next),
                        Boolean.TRUE,
                        new Date(ts),
                        new java.sql.Date(ts),
                        new Time(ts),
                        r.nextDouble(),
                        r.nextFloat(),
                        r.nextInt(1000),
                        r.nextLong(),
                        new BigDecimal(String.valueOf(r.nextLong())).multiply(new BigDecimal(String.valueOf(r.nextDouble()))),
                        new BigInteger(String.valueOf(r.nextLong())).multiply(new BigInteger(String.valueOf(r.nextInt(500)))),
                        new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)),
                        EnumTest.VALUE1 }));
            }
            for (int i = 0; i < headers.getColumnCount(); i++) {
                table.packColumn(i, 3);
            }
            table.setDefaultEditor(EnumTest.class, new EnumTableCellEditor<EnumTest>(EnumTest.class));

            JButton selectedButton = new JButton("what is selected?");
            selectedButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(safetable.getSelectedCell());

                    for (Object row : safetable.getSelectedRecords()) {
                        System.out.println(row);
                    }
                }
            });
            cp.add(selectedButton, BorderLayout.SOUTH);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.setVisible(true);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
