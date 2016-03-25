package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class TableDemoBooleanOrdering {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        ETableConfig configuration = new ETableConfig(true);
        configuration.setVertical(false);
        final JFrame frame = new JFrame();

        ETable<Object[]> table = new ETable<Object[]>(configuration);
        ETableHeaders<Object[]> headers = new ETableHeaders<Object[]>();
        headers.add("string"); //$NON-NLS-1$
        headers.add("boolean", Boolean.class); //$NON-NLS-1$
        headers.add("info"); //$NON-NLS-1$
        table.setHeaders(headers);

        table.stsi().addRecord(new ETableRecordArray<Object>("kol.a", true, "1(a)")); //$NON-NLS-1$ //$NON-NLS-2$
        table.stsi().addRecord(new ETableRecordArray<Object>("kol.b", false, "0(b)")); //$NON-NLS-1$ //$NON-NLS-2$
        table.stsi().addRecord(new ETableRecordArray<Object>("kol.c", true, "1(c)")); //$NON-NLS-1$ //$NON-NLS-2$
        table.stsi().addRecord(new ETableRecordArray<Object>("kol.d", false, "0(d)")); //$NON-NLS-1$ //$NON-NLS-2$

        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
