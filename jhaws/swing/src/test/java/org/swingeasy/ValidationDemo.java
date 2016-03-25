package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;

import org.apache.commons.lang3.StringUtils;
import org.swingeasy.validation.EValidationMessage;
import org.swingeasy.validation.EValidationMessageI;
import org.swingeasy.validation.EValidationPane;
import org.swingeasy.validation.EmailValidator;
import org.swingeasy.validation.NotNullValidator;
import org.swingeasy.validation.RegexValidator;
import org.swingeasy.validation.ValidationFactory;

/**
 * @author Jurgen
 */
public class ValidationDemo {
    public static void main(String[] args) {
        UIUtils.setLongerTooltips();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel contents = new JPanel(new BorderLayout());

        // validation code ==>
        EValidationPane parent = new EValidationPane(contents);
        // <== validation code

        frame.getContentPane().add(parent, BorderLayout.CENTER);

        final ETextField comp1 = new ETextField(new ETextFieldConfig("invalid1"));
        final ETextField comp2 = new ETextField(new ETextFieldConfig("invalid2"));
        final ETextField comp3 = new ETextField(new ETextFieldConfig(""));
        final ETextField comp4 = new ETextField(new ETextFieldConfig(""));
        final ETextField comp5 = new ETextField(new ETextFieldConfig(""));

        JPanel inner = new JPanel(new GridLayout(-1, 2));
        inner.add(new JLabel("invalid"));
        inner.add(comp1);

        inner.add(new JLabel("     "));
        inner.add(new JLabel("     "));

        inner.add(new JLabel("valid"));
        inner.add(comp2);

        inner.add(new JLabel("     "));
        inner.add(new JLabel("     "));

        inner.add(new JLabel("email (on focus lost)"));
        inner.add(comp3);

        inner.add(new JLabel("     "));
        inner.add(new JLabel("     "));

        inner.add(new JLabel("email (when typing)"));
        inner.add(comp4);

        inner.add(new JLabel("     "));
        inner.add(new JLabel("     "));

        inner.add(new JLabel("test"));
        inner.add(comp5);

        contents.add(inner, BorderLayout.CENTER);
        contents.add(new JLabel("     "), BorderLayout.NORTH);
        contents.add(new JLabel("     "), BorderLayout.EAST);
        contents.add(new JLabel("     "), BorderLayout.SOUTH);
        contents.add(new JLabel("     "), BorderLayout.WEST);

        frame.setSize(340, 240);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Layer & Validation Demo");
        frame.setVisible(true);

        String regex = "(^([a-zA-Z0-9]+([\\.+_-][a-zA-Z0-9]+)*)@(([a-zA-Z0-9]+((\\.|[-]{1,2})[a-zA-Z0-9]+)*)\\.[a-zA-Z]{2,6}))?$";
        final Pattern pattern = Pattern.compile(regex);

        final EValidationMessageI vm3 = new EValidationMessage(parent, comp3).stsi();

        comp3.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (StringUtils.isBlank(comp3.getText()) || pattern.matcher(comp3.getText()).matches()) {
                    vm3.setIsValid();
                } else {
                    vm3.setIsInvalid("email invalid");
                }
            }
        });

        final EValidationMessageI vm4 = new EValidationMessage(parent, comp4).stsi();
        vm4.setShowWhenValid(true);

        comp4.addDocumentKeyListener(new DocumentKeyListener() {
            @Override
            public void update(Type type, DocumentEvent e) {
                if (StringUtils.isBlank(comp4.getText()) || pattern.matcher(comp4.getText()).matches()) {
                    vm4.setIsValid();
                } else {
                    vm4.setIsInvalid("email invalid");
                }
            }
        });

        EValidationMessageI vm1 = new EValidationMessage(parent, comp1).stsi();
        EValidationMessageI vm2 = new EValidationMessage(parent, comp2).stsi();
        vm1.setIsInvalid("validation text 1");
        vm2.setShowWhenValid(true);
        vm2.setIsValid();

        new ValidationFactory().install(parent, comp5, new NotNullValidator<String>(), new EmailValidator(), new RegexValidator(regex))
        .setShowWhenValid(true);
    }
}
