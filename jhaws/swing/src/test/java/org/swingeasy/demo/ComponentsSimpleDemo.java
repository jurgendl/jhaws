package org.swingeasy.demo;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

import org.swingeasy.EButton;
import org.swingeasy.EButtonConfig;
import org.swingeasy.ECheckBox;
import org.swingeasy.ECheckBoxConfig;
import org.swingeasy.ECheckBoxList;
import org.swingeasy.ECheckBoxList.ECheckBoxListRecord;
import org.swingeasy.EComboBox;
import org.swingeasy.EComboBoxConfig;
import org.swingeasy.EDateEditor;
import org.swingeasy.EDateTimeChooser;
import org.swingeasy.EDateTimeEditor;
import org.swingeasy.EFormattedTextField;
import org.swingeasy.EFormattedTextFieldConfig;
import org.swingeasy.ELabel;
import org.swingeasy.EList;
import org.swingeasy.EListConfig;
import org.swingeasy.EListRecord;
import org.swingeasy.EProgressBar;
import org.swingeasy.ERadioButton;
import org.swingeasy.ERadioButtonConfig;
import org.swingeasy.ESpinner;
import org.swingeasy.ESpinnerDateModel;
import org.swingeasy.ETable;
import org.swingeasy.ETableConfig;
import org.swingeasy.ETableRecordCollection;
import org.swingeasy.ETextArea;
import org.swingeasy.ETextAreaConfig;
import org.swingeasy.ETextField;
import org.swingeasy.ETextFieldConfig;
import org.swingeasy.ETextPane;
import org.swingeasy.ETextPaneConfig;
import org.swingeasy.EToggleButton;
import org.swingeasy.EToolBarButtonConfig;
import org.swingeasy.EToolBarButtonCustomizer;
import org.swingeasy.EToolBarToggleButton;
import org.swingeasy.ETree;
import org.swingeasy.ETreeConfig;
import org.swingeasy.ETreeNode;
import org.swingeasy.EURILabel;
import org.swingeasy.FileSelection;
import org.swingeasy.FileSelectionConfig;
import org.swingeasy.GradientPanel;
import org.swingeasy.TristateCheckBox;
import org.swingeasy.UIUtils;
import org.swingeasy.formatters.NumberFormatBuilder;

/**
 * @author Jurgen
 */
public class ComponentsSimpleDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("Components Demo");

        JPanel cp = new JPanel(new MigLayout("wrap 1", "[]", ""));
        frame.getContentPane().add(new JScrollPane(cp));

        cp.add(new ELabel("EButton"), "growx");
        cp.add(new EButton(new EButtonConfig("EButton")), "growx");

        cp.add(new ELabel("ECheckBox"), "growx");
        cp.add(new ECheckBox(new ECheckBoxConfig()), "growx");

        cp.add(new ELabel("EComboBox"), "growx");
        cp.add(new EComboBox<String>(new EComboBoxConfig()), "growx");

        cp.add(new ELabel("EFormattedTextField"), "growx");
        cp.add(new EFormattedTextField<Integer>(new EFormattedTextFieldConfig(new NumberFormatBuilder(NumberFormatBuilder.Type.Integer)), 100),
                "growx");

        cp.add(new ELabel("EList"), "growx");
        EList<String> elist = new EList<String>(new EListConfig());
        cp.add(elist, "growx");

        cp.add(new ELabel("EProgressBar"), "growx");
        cp.add(new EProgressBar(), "growx");

        cp.add(new ELabel("ERadioButton"), "growx");
        cp.add(new ERadioButton(new ERadioButtonConfig()), "growx");

        cp.add(new ELabel("ESpinner"), "growx");
        cp.add(new ESpinner<Integer>(new ESpinnerDateModel()), "growx");

        cp.add(new ELabel("ETable"), "growx");
        ETable<List<String>> etable = new ETable<List<String>>(new ETableConfig());
        cp.add(etable, "growx");

        cp.add(new ELabel("ETextArea"), "growx");
        cp.add(new ETextArea(new ETextAreaConfig("ETextArea")), "growx");

        cp.add(new ELabel("ETextField"), "growx");
        cp.add(new ETextField(new ETextFieldConfig("ETextField")), "growx");

        cp.add(new ELabel("EToggleButton"), "growx");
        cp.add(new EToggleButton(new EButtonConfig("EToggleButton")), "growx");

        cp.add(new ELabel("EToggleToolBarButton"), "growx");
        cp.add(new EToolBarToggleButton(new EToolBarButtonConfig(new EToolBarButtonCustomizer() {
            @Override
            public void customize(AbstractButton button) {
                button.setText("EToggleToolBarButton");
            }
        })), "growx");

        cp.add(new ELabel("ETree"), "growx");
        cp.add(new ETree<String>(new ETreeConfig(), new ETreeNode<String>("root")), "growx");

        // cp.add(new ETreeTable<String>(new ETreeTableConfig(), new ETreeTableHeaders<String>()));

        cp.add(new ELabel("ETextPane"), "growx");
        cp.add(new ETextPane(new ETextPaneConfig()), "growx");

        // cp.add(new ECheckboxTree(new ECheckBoxTreeConfig(), new ECheckBoxTreeNode<String>("root")));

        cp.add(new ELabel("EDateChooser"), "growx");
        cp.add(new EDateTimeChooser(), "growx");

        cp.add(new ELabel("EDateEditor"), "growx");
        cp.add(new EDateEditor(), "growx");

        cp.add(new ELabel("EDateTimeEditor"), "growx");
        cp.add(new EDateTimeEditor(), "growx");

        cp.add(new ELabel("TristateCheckBox"), "growx");
        cp.add(new TristateCheckBox(""), "growx");

        cp.add(new ELabel("GradientPanel"), "growx");
        cp.add(new GradientPanel(), "growx");

        cp.add(new ELabel("EURILabel"), "growx");
        cp.add(new EURILabel(URI.create("http://www.google.com")), "growx");

        cp.add(new ELabel("FileSelection"), "growx");
        cp.add(new FileSelection(new FileSelectionConfig()), "growx");

        cp.add(new ELabel("ECheckBoxList"), "growx");
        ECheckBoxList echeckboxlist = new ECheckBoxList(new EListConfig());
        cp.add(echeckboxlist, "growx");

        frame.pack();
        frame.setSize(400, frame.getWidth());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        elist.stsi().addRecord(new EListRecord<String>("record"));
        etable.stsi().addRecord(new ETableRecordCollection<String>(Arrays.asList("record")));
        echeckboxlist.stsi().addRecord(new ECheckBoxListRecord(true));
    }
}
