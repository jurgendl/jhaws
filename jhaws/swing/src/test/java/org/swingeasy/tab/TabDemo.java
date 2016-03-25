package org.swingeasy.tab;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

import org.swingeasy.ETabbedPane;
import org.swingeasy.ETabbedPane.ETabToolbar;
import org.swingeasy.ETabbedPaneConfig;
import org.swingeasy.Rotation;
import org.swingeasy.UIUtils;

/**
 * @see http://java-swing-tips.blogspot.com/2010/02/tabtransferhandler.html
 * @see http://www.exampledepot.com/taxonomy/term/319
 */
public class TabDemo {

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        TabDemo demo = new TabDemo();
        demo.addComponents(frame.getContentPane());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("TabDemo");
        frame.setVisible(true);
        demo.TL.setDividerLocation(.4);
        demo.TR.setDividerLocation(.6);
        demo.TM.setDividerLocation(.3);
    }

    private ETabbedPane TLT = new ETabbedPane(new ETabbedPaneConfig(false, true));

    private ETabbedPane TLB = new ETabbedPane(new ETabbedPaneConfig(Rotation.COUNTER_CLOCKWISE, true, false));

    private ETabbedPane TRT = new ETabbedPane(new ETabbedPaneConfig(Rotation.CLOCKWISE, false, true));

    private ETabbedPane TRB = new ETabbedPane(new ETabbedPaneConfig(true, false));

    private JSplitPane TL = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.TLT, this.TLB);

    private JSplitPane TR = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.TRT, this.TRB);

    private JSplitPane TM = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.TL, this.TR);

    private void add(JTabbedPane tab) {
        for (int i = 0; i < 5; i++) {
            tab.addTab("tab " + tab.getName() + " - " + i, FileSystemView.getFileSystemView().getSystemIcon(new File(".")), new JTextField("content "
                    + tab.getName() + " - " + i), "tooltip " + tab.getName() + " - " + i);
            // tab.setEnabledAt(i, (i % 2) == 0);
        }
    }

    private void addComponents(Container container) {
        container.add(this.TM);

        this.TL.setDividerSize(2);
        this.TR.setDividerSize(2);
        this.TM.setDividerSize(2);

        this.TRT.setTabPlacement(SwingConstants.RIGHT);
        this.TRT.setName("TRT");

        this.TLT.setTabPlacement(SwingConstants.TOP);
        this.TLT.setName("TLT");

        this.TRB.setTabPlacement(SwingConstants.BOTTOM);
        this.TRB.setName("TRB");

        this.TLB.setTabPlacement(SwingConstants.LEFT);
        this.TLB.setName("TLB");

        this.add(this.TLT);
        this.add(this.TLB);
        this.add(this.TRT);
        this.add(this.TRB);

        ETabToolbar minimized = new ETabToolbar();
        container.add(minimized, BorderLayout.NORTH);

        this.TRB.setMinimizeTo(minimized);
        this.TRT.setMinimizeTo(minimized);
        this.TLB.setMinimizeTo(minimized);
        this.TLT.setMinimizeTo(minimized);
    }
}
