package org.jhaws.common.net.client.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
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

import org.apache.commons.lang3.StringUtils;

public class FormDialog extends JDialog {
	private static final long serialVersionUID = -8912722198073790498L;

	private final Form form;

	private boolean cancelled = true;

	public FormDialog(final Form form) {
		this(form, 2);
	}

	public FormDialog(final Form form, int cols) {
		super((Frame) null, true);

		this.form = form;

		this.getContentPane().setLayout(new BorderLayout());

		JPanel mainpanel = new JPanel(new GridLayout(0, cols * 2));
		JPanel actionpanel = new JPanel(new FlowLayout());

		JButton ok = new JButton("ok");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FormDialog.this.cancelled = false;
				FormDialog.this.dispose();
			}
		});

		JButton cancel = new JButton("cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FormDialog.this.dispose();
			}
		});

		actionpanel.add(ok);
		actionpanel.add(cancel);

		this.getContentPane().add(mainpanel, BorderLayout.CENTER);
		this.getContentPane().add(actionpanel, BorderLayout.SOUTH);

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
				mainpanel.add(this.addListener(password, new JPasswordField(password.getValue())), null);

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

				DefaultListModel<Map.Entry<String, String>> model = new DefaultListModel<>();

				Map.Entry<String, String> sl = null;
				for (Map.Entry<String, String> option : selection.getOptions().entrySet()) {
					model.addElement(option);
					if (option.getKey().equals(selection.getValue())) {
						sl = option;
					}
				}

				JList<Map.Entry<String, String>> jList = new JList<>(model);
				jList.setCellRenderer(new DefaultListCellRenderer() {
					private static final long serialVersionUID = 5621985439026030982L;

					@Override
					public Component getListCellRendererComponent(JList<?> list, Object value, int index,
							boolean isSelected, boolean cellHasFocus) {
						if (value != null) {
							@SuppressWarnings("unchecked")
							Map.Entry<String, String> entry = (Entry<String, String>) value;
							value = entry.getValue();
						}
						return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					}
				});

				jList.setSelectionMode(selection.isMultiple() ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
						: ListSelectionModel.SINGLE_SELECTION);

				if (sl != null) {
					jList.setSelectedValue(sl, true);
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
				mainpanel.add(this.addListener(text, new JTextField(text.getValue())));

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

		this.setSize(new Dimension(400, 400));
		this.setLocationRelativeTo(null);
	}

	private JComponent addListener(final Input input, final JTextComponent jTextComponent) {
		if (jTextComponent instanceof JPasswordField) {
			jTextComponent.enableInputMethods(true);
		}

		jTextComponent.getDocument().addDocumentListener(new DocumentListener() {
			private void changed() {
				input.setValue(jTextComponent.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				this.changed();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				this.changed();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				this.changed();
			}
		});

		return jTextComponent;
	}

	public Form getForm() {
		return this.form;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}
}
