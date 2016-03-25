package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * @author Jurgen
 */
public class EDateEditor extends AbstractELabeledTextFieldButtonComponent<ELabel, ESpinner<Date>, EButton> {
    private static final long serialVersionUID = 3275532427920825736L;

    protected JComponent parentComponent = null;

    protected EDateTimeChooser dateChooser = null;

    protected JPopupMenu popup = null;

    protected JPanel datePanel = null;

    protected SimpleDateFormat formatter;

    public EDateEditor() {
        this(new Date());
    }

    public EDateEditor(Date date) {
        this.init();
        this.setDate(date);
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
     */
    @Override
    public void copy(ActionEvent e) {
        EComponentPopupMenu.copyToClipboard(String.valueOf(this.getInput().getValue()));
    }

    protected EDateTimeChooser createDateChooser() {
        return new EDateTimeChooser(DateTimeType.DATE);
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#doAction()
     */
    @Override
    protected void doAction() {
        JPopupMenu pop = this.getPopup();
        pop.setLocation(new Point((this.getLocationOnScreen().x + this.getWidth()) - (int) pop.getPreferredSize().getWidth(), this
                .getLocationOnScreen().y + this.getHeight()));
        Date date = this.getInput().get();
        if (date == null) {
            date = new Date();
        }
        this.getDateChooser().setDate(date);
        pop.setVisible(true);
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#getAction()
     */
    @Override
    protected String getAction() {
        return "pick-date";//$NON-NLS-1$
    }

    /**
     * 
     * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getButton()
     */
    @Override
    public EButton getButton() {
        if (this.button == null) {
            this.button = new EButton(new EButtonConfig(new EIconButtonCustomizer(new Dimension(20, 20)), this.getIcon()));
            this.button.setActionCommand(this.getAction());
            this.button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EDateEditor.this.doAction();
                }
            });
        }
        return this.button;
    }

    public Date getDate() {
        return this.getInput().get();
    }

    protected EDateTimeChooser getDateChooser() {
        if (this.dateChooser == null) {
            this.dateChooser = this.createDateChooser();
        }
        return this.dateChooser;
    }

    protected JPanel getDatePanel() {
        if (this.datePanel == null) {
            this.datePanel = new JPanel(new BorderLayout());
            JPanel actions = new JPanel(new FlowLayout());
            {
                EButton okbtn = new EButton(new EButtonConfig(Messages.getString(this.getLocale(), "EDateEditor.OK")));//$NON-NLS-1$
                okbtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        EDateEditor.this.setDate(EDateEditor.this.getDateChooser().getDate());
                        EDateEditor.this.getPopup().setVisible(false);
                    }
                });
                actions.add(okbtn);
            }
            {
                EButton nullbtn = new EButton(new EButtonConfig(Messages.getString(this.getLocale(), "EDateEditor.Null")));//$NON-NLS-1$
                nullbtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        EDateEditor.this.setDate(null);
                        EDateEditor.this.getPopup().setVisible(false);
                    }
                });
                actions.add(nullbtn);
            }
            {
                EButton cancelbtn = new EButton(new EButtonConfig(Messages.getString(this.getLocale(), "EDateEditor.Cancel")));//$NON-NLS-1$
                cancelbtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        EDateEditor.this.getPopup().setVisible(false);
                    }
                });
                actions.add(cancelbtn);
            }
            this.datePanel.add(this.getDateChooser(), BorderLayout.CENTER);
            this.datePanel.add(actions, BorderLayout.SOUTH);
        }
        return this.datePanel;
    }

    public SimpleDateFormat getFormatter() {
        if (this.formatter == null) {
            this.formatter = SimpleDateFormat.class.cast(DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()));
        }
        return this.formatter;
    }

    /**
     * 
     * @see org.swingeasy.ELabeledTextFieldButtonComponent#getIcon()
     */
    @Override
    protected Icon getIcon() {
        return Resources.getImageResource("date.png");//$NON-NLS-1$
    }

    /**
     * 
     * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getInput()
     */
    @Override
    public ESpinner<Date> getInput() {
        if (this.input == null) {
            ESpinnerDateModel model = new ESpinnerDateModel();
            model.setValue(new Date());
            this.input = new ESpinner<Date>(model);
            this.input.setEditor(new ESpinner.DateEditor(this.input, this.getFormatter().toPattern()));
            this.input.setBorder(null);
        }
        return this.input;
    }

    /**
     * 
     * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getLabel()
     */
    @Override
    public ELabel getLabel() {
        if (this.label == null) {
            this.label = new ELabel();
            this.label.setLabelFor(this.getInput());
        }
        return this.label;
    }

    /**
     * 
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this.parentComponent;
    }

    protected JPopupMenu getPopup() {
        if (this.popup == null) {
            this.popup = new JPopupMenu();
            this.popup.setLightWeightPopupEnabled(true);
            this.popup.add(this.getDatePanel());
            UIUtils.getRootWindow(this.getButton()).addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    EDateEditor.this.popup.setVisible(false);
                }

                @Override
                public void windowIconified(WindowEvent e) {
                    EDateEditor.this.popup.setVisible(false);
                }
            });
        }
        return this.popup;
    }

    protected void hidePopup() {
        if (this.popup != null) {
            this.popup.setVisible(false);
        }
    }

    protected void init() {
        this.installAncestorListener();
    }

    protected void installAncestorListener() {
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                EDateEditor.this.hidePopup();
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                if (event.getSource() != EDateEditor.this) {
                    EDateEditor.this.hidePopup();
                }
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                EDateEditor.this.hidePopup();
            }
        });
    }

    public void setDate(Date date) {
        this.getInput().setValue(date);
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.getButton().setToolTipText(Messages.getString(l, "EDateEditor.button.text"));//$NON-NLS-1$
        this.getLabel().setText(Messages.getString(l, "EDateEditor.label.text") + ": ");//$NON-NLS-1$ //$NON-NLS-2$
        // TODO set local format spinner
    }

    public void setParentComponent(JComponent parentComponent) {
        this.parentComponent = parentComponent;
    }
}
