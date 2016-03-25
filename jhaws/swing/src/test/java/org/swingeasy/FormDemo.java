package org.swingeasy;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.swingeasy.form.FormBuilder;

/**
 * @author Jurgen
 */
public class FormDemo {
    private static void addComponents(Container container) {
        FormBuilder builder = new FormBuilder(2);
        builder.setDebug(true);

        builder.addTitle("Somewhat longer than label - title 1", 2);
        builder.addComponent("Label1", new ETextField(new ETextFieldConfig()), null);
        builder.addComponent("Label2", new ETextField(new ETextFieldConfig()), null);

        builder.addTitle("Somewhat longer than label - title 2");
        builder.addTitle("Somewhat longer than label - title 3");
        builder.addComponent("Label3", new ETextField(new ETextFieldConfig()), 2, null);

        builder.addComponent("Label4", new JScrollPane(new ETextArea(new ETextAreaConfig())), 1, 3, null);
        builder.addTitle("Somewhat longer than label - title 4");
        builder.addComponent("Label5", new ETextField(new ETextFieldConfig()), null);
        builder.addComponent("Label6", new ETextField(new ETextFieldConfig()), null);

        EComboBoxConfig cfg = new EComboBoxConfig();
        EComboBox<String> component = new EComboBox<String>(cfg).stsi();
        component
                .addRecord(new EComboBoxRecord<String>(
                        "aaaaaaaaazzzzzzzzzzeeeeeeeeerrrrrrrrrtttttttttttttyyyyyyyyyyyy111111111222222223333333334444444444455555555566666666777777788888888889999999"));
        Dimension preferredSize = component.getPreferredSize();
        System.out.println(preferredSize);
        component.setPreferredSize(new Dimension(222, preferredSize.height));
        component.setSize(new Dimension(222, preferredSize.height));
        component.setMaximumSize(new Dimension(222, preferredSize.height));
        builder.addComponent("combo", component.getParentComponent(), null);

        builder.addComponent("", new JLabel(), null);

        container.add(builder.getContainer());
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        FormDemo.addComponents(frame.getContentPane());
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setTitle("FormDemo");
        frame.setVisible(true);
    }
}
