package org.swingeasy.tab;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

import org.swingeasy.ETabbedPane;
import org.swingeasy.ETabbedPane.ETabToolbar;
import org.swingeasy.ETabbedPaneConfig;
import org.swingeasy.UIUtils;

/**
 * @see http://java-swing-tips.blogspot.com/2010/02/tabtransferhandler.html
 * @see http://www.exampledepot.com/taxonomy/term/319
 */
public class TabDemo2 {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        TabDemo2 demo = new TabDemo2();
        demo.addComponents(frame.getContentPane());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("TabDemo");
        frame.setVisible(true);
        demo.TL.setDividerLocation(.4);
        demo.TR.setDividerLocation(.6);
        demo.TM.setDividerLocation(.3);
    }

    private ETabbedPane TLT = new ETabbedPane(new ETabbedPaneConfig(true, true));

    private ETabbedPane TLB = new ETabbedPane(new ETabbedPaneConfig(true, true));

    private ETabbedPane TRT = new ETabbedPane(new ETabbedPaneConfig(true, true));

    private ETabbedPane TRB = new ETabbedPane(new ETabbedPaneConfig(true, true));

    private JSplitPane TL = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.TLT, this.TRT);

    private JSplitPane TR = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.TLB, this.TRB);

    private JSplitPane TM = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.TL, this.TR);

    private void addComponents(Container container) {
        container.add(this.TM);

        this.TL.setDividerSize(2);
        this.TR.setDividerSize(2);
        this.TM.setDividerSize(2);

        this.TLT.addTab("HQL", new JTextPane());
        this.TLT.addTab("Favorites", new JTextPane());

        this.TRT.addTab("Parameters", new JTextPane());

        this.TLB.addTab("Results", new JTextPane());
        this.TLB.addTab("SQL", new JTextPane());
        this.TLB.addTab("Formatted SQL", new JTextPane());

        this.TRB.addTab("Properties", new JTextPane());

        ETabToolbar minimized = new ETabToolbar();
        container.add(minimized, BorderLayout.SOUTH);

        this.TRB.setMinimizeTo(minimized);
        this.TRT.setMinimizeTo(minimized);
        this.TLB.setMinimizeTo(minimized);
        this.TLT.setMinimizeTo(minimized);
    }
}
