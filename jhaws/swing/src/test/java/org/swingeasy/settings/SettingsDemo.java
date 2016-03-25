package org.swingeasy.settings;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.swingeasy.ELabel;
import org.swingeasy.ETextField;
import org.swingeasy.ETextFieldConfig;
import org.swingeasy.ETree;
import org.swingeasy.ETreeConfig;
import org.swingeasy.ETreeNode;
import org.swingeasy.UIUtils;

public class SettingsDemo {
    public static class Settings extends HashMap<String, Object> {
        private static final long serialVersionUID = 5620205649746314072L;
    }

    public static interface SettingsMenu {
        public List<SettingsMenu> getChildren();

        public String getName();

        public SettingsMenu getParent();

        public Settings getSettings();
    }

    public static class SettingsMenuImpl implements SettingsMenu {
        private SettingsMenu parent;

        private List<SettingsMenu> children = new ArrayList<SettingsMenu>();

        private Settings settings = new Settings();

        private final String name;

        public SettingsMenuImpl(String name) {
            this.name = name;
        }

        public void addChild(SettingsMenuImpl child) {
            this.children.add(child);
            child.parent = this;
        }

        @Override
        public List<SettingsMenu> getChildren() {
            return this.children;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public SettingsMenu getParent() {
            return this.parent;
        }

        @Override
        public Settings getSettings() {
            return this.settings;
        }

        public void set(String key, String value) {
            this.settings.put(key, value);
        }

        public void setChildren(List<SettingsMenu> children) {
            this.children = children;
        }

        public void setParent(SettingsMenu parent) {
            this.parent = parent;
        }

        public void setSettings(Settings settings) {
            this.settings = settings;
        }

        @Override
        public String toString() {
            return this.getName();
        }
    }

    private static class SettingsNode extends ETreeNode<SettingsMenu> {
        private static final long serialVersionUID = -2196823311153561046L;

        public SettingsNode(SettingsMenu menu) {
            super(menu);
            for (SettingsMenu child : menu.getChildren()) {
                this.add(new SettingsNode(child));
            }
        }
    }

    private static JSplitPane addComponents(Container p) {
        SettingsMenuImpl topsettings = new SettingsMenuImpl("top");

        SettingsMenuImpl item1 = new SettingsMenuImpl("item1");
        topsettings.addChild(item1);
        for (int i = 0; i < 10; i++) {
            item1.set("key" + i, "value" + i);
        }

        SettingsMenuImpl item2 = new SettingsMenuImpl("item2");
        topsettings.addChild(item2);
        for (int i = 0; i < 100; i++) {
            item2.set("key" + i, "value" + i);
        }

        ETreeConfig config = new ETreeConfig();
        SettingsNode top = new SettingsNode(topsettings);
        final ETree<SettingsMenu> tree = new ETree<SettingsMenu>(config, top);
        tree.setRootVisible(false);
        tree.setEditable(false);

        final JPanel right = new JPanel(new BorderLayout());

        final TreeSelectionModel selectionModel = tree.getSelectionModel();
        selectionModel.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (selectionModel.getSelectionPath() == null) {
                    return;
                }
                SettingsNode node = (SettingsNode) selectionModel.getSelectionPath().getLastPathComponent();
                SettingsMenuImpl settings = (SettingsMenuImpl) node.getUserObject();

                right.removeAll();

                JPanel settingspanel = new JPanel(new GridLayout(0, 2));
                JScrollPane jsp = new JScrollPane(settingspanel);
                right.add(jsp, BorderLayout.CENTER);

                for (Map.Entry<String, Object> setting : settings.getSettings().entrySet()) {
                    right.add(new ELabel(setting.getKey()));
                    right.add(new ETextField(new ETextFieldConfig(String.valueOf(setting.getValue()))));
                }
            }
        });

        JPanel left = new JPanel(new BorderLayout());
        left.add(new JScrollPane(tree), BorderLayout.CENTER);
        left.add(tree.getSearchComponent(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        p.add(split);
        return split;
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JSplitPane split = SettingsDemo.addComponents(frame.getContentPane());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("SettingsDemo");
        frame.setVisible(true);
        split.setDividerLocation(0.3d);
    }
}
