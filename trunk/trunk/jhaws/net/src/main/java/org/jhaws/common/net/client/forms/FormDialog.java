package org.jhaws.common.net.client.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;

/**
 * FormDialog
 */
public class FormDialog extends JDialog {

    private static final long serialVersionUID = -8912722198073790498L;

    /** form */
    private final Form form;

    /** cancelled */
    private boolean cancelled = true;

    /**
     * Creates a new FormDialog object.
     * 
     * @param form
     */
    public FormDialog(final Form form) {
        super((Frame) null, true);

        this.form = form;

        getContentPane().setLayout(new BorderLayout());

        JPanel mainpanel = new JPanel(new GridLayout(0, 2));
        JPanel actionpanel = new JPanel(new FlowLayout());

        JButton ok = new JButton("ok");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelled = false;
                dispose();
            }
        });

        JButton cancel = new JButton("cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        actionpanel.add(ok);
        actionpanel.add(cancel);

        getContentPane().add(mainpanel, BorderLayout.CENTER);
        getContentPane().add(actionpanel, BorderLayout.SOUTH);

        for (InputElement element : form.getInputElements()) {
            // System.out.println(element);

            switch (element.getType()) {
                case button:

                    Input button = (Input) element;
                    mainpanel.add(new JLabel(element.getName()), null);
                    mainpanel.add(new JButton(button.getValue()), null);

                    break;

                case checkbox:

                    InputSelection checkbox = (InputSelection) element;
                    mainpanel.add(new JLabel(checkbox.getName()), null);

                    boolean selected = checkbox.getOptions().get(0).equals(checkbox.getValue());

                    JCheckBox jCheckBox = new JCheckBox();
                    jCheckBox.setSelected(selected);

                    mainpanel.add(jCheckBox, null);

                    break;

                case file:

                    FileInput fileInput = (FileInput) element;
                    mainpanel.add(new JLabel(element.getType().toString()), null);
                    mainpanel.add(new JLabel(fileInput.getName()), null);

                    break;

                case hidden:

                    // Input hidden = (Input) element;

                    // do nothing
                    break;

                case image:

                    Input image = (Input) element;
                    mainpanel.add(new JLabel(element.getType().toString()), null);
                    mainpanel.add(new JLabel(image.getName()), null);

                    break;

                case password:

                    Password password = (Password) element;
                    mainpanel.add(new JLabel(password.getName()), null);
                    mainpanel.add(addListener(password, new JPasswordField(password.getValue())), null);

                    break;

                case radio:

                    InputSelection radio = (InputSelection) element;
                    mainpanel.add(new JLabel(radio.getName()), null);

                    JPanel optionpanel = new JPanel(new GridLayout(radio.getOptions().size(), 1));
                    ButtonGroup bg = new ButtonGroup();

                    for (String option : radio.getOptions()) {
                        JRadioButton jRadioButton = new JRadioButton(option);
                        optionpanel.add(jRadioButton);
                        bg.add(jRadioButton);

                        if (option.equals(radio.getValue())) {
                            jRadioButton.setSelected(true);
                        }
                    }

                    mainpanel.add(optionpanel, null);

                    break;

                case reset:

                    // Input reset = (Input) element;
                    // do nothing
                    break;

                case select:

                    Selection selection = (Selection) element;
                    mainpanel.add(new JLabel(selection.getName()), null);

                    DefaultListModel model = new DefaultListModel();

                    for (String option : selection.getOptions()) {
                        model.addElement(option);
                    }

                    JList jList = new JList(model);

                    jList.setSelectionMode(selection.isMultiple() ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
                            : ListSelectionModel.SINGLE_SELECTION);

                    if (StringUtils.isNotBlank(selection.getValue())) {
                        jList.setSelectedValue(selection.getValue(), true);
                    }

                    mainpanel.add(new JScrollPane(jList), null);

                    break;

                case submit:

                    // Input submit = (Input) element;
                    // do nothing
                    break;

                case text:

                    Input text = (Input) element;

                    if ("username".equals(text.getId()) && StringUtils.isBlank(text.getValue())) {
                        text.setValue(System.getProperty("user.name"));
                    }

                    mainpanel.add(new JLabel(text.getName()));
                    mainpanel.add(addListener(text, new JTextField(text.getValue())));

                    break;

                case textarea:

                    TextArea textArea = (TextArea) element;
                    mainpanel.add(new JLabel(textArea.getName()), null);

                    JTextArea jTextArea = new JTextArea(textArea.getValue());
                    mainpanel.add(new JScrollPane(jTextArea), null);

                    break;

                default:
                    throw new IllegalArgumentException("" + element);
            }
        }

        setSize(new Dimension(400, 400));
        setLocationRelativeTo(null);
    }

    private JComponent addListener(final Input input, final JTextComponent jTextComponent) {
        if (jTextComponent instanceof JPasswordField) {
            jTextComponent.enableInputMethods(true);
        }

        jTextComponent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            private void changed() {
                input.setValue(jTextComponent.getText());
            }
        });

        return jTextComponent;
    }

    /**
     * isCancelled
     * 
     * @return the cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * getForm
     * 
     * @return the form
     */
    public Form getForm() {
        return form;
    }
}
