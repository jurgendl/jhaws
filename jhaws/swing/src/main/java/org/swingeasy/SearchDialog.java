package org.swingeasy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.StringUtils;
import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class SearchDialog extends JDialog implements EComponentI {
    private static final long serialVersionUID = -7658724511534306863L;

    protected final ETextArea textComponent;

    protected boolean replacing;

    protected EButton btnReplaceAll;

    protected EButton btnClose;

    protected ELabel lblFind;

    protected EButton btnHighlightAll;

    protected ETextField tfFind;

    protected EButton btnFind;

    protected ETextField tfReplace;

    protected EButton btnReplace;

    protected ECheckBox cbReplace;

    public SearchDialog(boolean replacing, ETextArea textComponent) {
        super(UIUtils.getRootWindow(textComponent), Messages.getString(SystemSettings.getCurrentLocale(), "SearchDialog.title"),
                ModalityType.MODELESS);
        this.textComponent = textComponent;
        this.init();
        this.setLocationRelativeTo(UIUtils.getRootWindow(textComponent));
        this.setReplacing(replacing && textComponent.isEditable() && textComponent.isEnabled());
        String selectedText = textComponent.getSelectedText();
        if (StringUtils.isNotBlank(selectedText)) {
            this.tfFind.setText(selectedText);
        }
        UIUtils.registerLocaleChangeListener((EComponentI) this);
    }

    protected void closed() {
        // this.textComponent.removeHighlights();
    }

    protected void find(String find) {
        this.textComponent.find(find);
    }

    protected EButton getBtnClose() {
        if (this.btnClose == null) {
            this.btnClose = new EButton(new EButtonConfig());
        }
        return this.btnClose;
    }

    protected EButton getBtnFind() {
        if (this.btnFind == null) {
            this.btnFind = new EButton(new EButtonConfig());
        }
        return this.btnFind;
    }

    protected EButton getBtnHighlightAll() {
        if (this.btnHighlightAll == null) {
            this.btnHighlightAll = new EButton(new EButtonConfig());
        }
        return this.btnHighlightAll;
    }

    protected EButton getBtnReplace() {
        if (this.btnReplace == null) {
            this.btnReplace = new EButton(new EButtonConfig());
        }
        return this.btnReplace;
    }

    protected EButton getBtnReplaceAll() {
        if (this.btnReplaceAll == null) {
            this.btnReplaceAll = new EButton(new EButtonConfig());
        }
        return this.btnReplaceAll;
    }

    protected ECheckBox getCbReplace() {
        if (this.cbReplace == null) {
            this.cbReplace = new ECheckBox(new ECheckBoxConfig());
            this.cbReplace.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        return this.cbReplace;
    }

    protected ELabel getLblFind() {
        if (this.lblFind == null) {
            this.lblFind = new ELabel(new ELabelConfig("").setHorizontalAlignment(SwingConstants.RIGHT));
        }
        return this.lblFind;
    }

    protected ETextField getTfFind() {
        if (this.tfFind == null) {
            this.tfFind = new ETextField(new ETextFieldConfig());
        }
        return this.tfFind;
    }

    protected ETextField getTfReplace() {
        if (this.tfReplace == null) {
            this.tfReplace = new ETextField(new ETextFieldConfig());
        }
        return this.tfReplace;
    }

    protected void highlightAll(String find) {
        this.textComponent.highlightAll(find);
    }

    protected void init() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    SearchDialog.this.closed();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        String layoutConstraints = "wrap 4, insets 5 5 0 5";
        String colConstraints = "[right,fill]rel[grow,fill,220::]14px[fill,sg grp1]14px[fill,sg grp1]";
        String rowConstraints = "[]rel[]rel[]0px";
        this.setLayout(new MigLayout(layoutConstraints, colConstraints, rowConstraints));

        this.add(this.getLblFind());
        this.add(this.getTfFind());
        this.add(this.getBtnFind());
        this.add(this.getBtnHighlightAll(), "wrap");

        this.getCbReplace().setEnabled(this.textComponent.isEditable() && this.textComponent.isEnabled());
        this.add(this.getCbReplace());

        this.getCbReplace().addValueChangeListener(new ValueChangeListener<Boolean>() {
            @Override
            public void valueChanged(Boolean value) {
                SearchDialog.this.setReplacing(value);
            }
        });

        this.add(this.getTfReplace());
        this.add(this.getBtnReplace());
        this.add(this.getBtnReplaceAll(), "wrap");

        this.add(new ELabel(), "span 3");
        this.add(this.getBtnClose());

        ActionListener findAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                    try {
                        SearchDialog.this.find(SearchDialog.this.getTfFind().getText().trim());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        this.tfFind.addActionListener(findAction);
        this.getBtnFind().addActionListener(findAction);
        this.getBtnHighlightAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                    try {
                        SearchDialog.this.highlightAll(SearchDialog.this.getTfFind().getText().trim());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        ActionListener replaceAtion = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                    try {
                        SearchDialog.this.replace(SearchDialog.this.getTfFind().getText().trim(), SearchDialog.this.getTfReplace().getText().trim());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        this.getTfReplace().addActionListener(replaceAtion);
        this.getBtnReplace().addActionListener(replaceAtion);
        this.getBtnReplaceAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                    try {
                        SearchDialog.this.replaceAll(SearchDialog.this.getTfFind().getText().trim(), SearchDialog.this.getTfReplace().getText()
                                .trim());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        this.getBtnClose().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SearchDialog.this.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.pack();
        this.setResizable(false);
        this.getBtnReplaceAll().setName("SearchDialog.replace-all");
        this.getBtnClose().setName("SearchDialog.cancelButtonText");
        this.getLblFind().setName("SearchDialog.find");
        this.getBtnHighlightAll().setName("SearchDialog.highlight-all");
        this.getBtnFind().setName("SearchDialog.find");
        this.getCbReplace().setName("SearchDialog.replace-by");
        this.getBtnReplace().setName("SearchDialog.replace");
        this.getTfFind().setName("SearchDialog.find.input");
        this.getTfReplace().setName("SearchDialog.replace.input");
    }

    public boolean isReplacing() {
        return this.replacing;
    }

    protected void replace(String find, String replace) {
        this.textComponent.replace(find, replace);
    }

    protected void replaceAll(String find, String replace) {
        this.textComponent.replaceAll(find, replace);
    }

    /**
     *
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.setTitle(Messages.getString(l, "SearchDialog.title"));
        this.getBtnReplaceAll().setText(Messages.getString(l, "SearchDialog.replace-all"));
        this.getBtnClose().setText(Messages.getString(l, "SearchDialog.cancelButtonText"));
        this.getLblFind().setText(Messages.getString(l, "SearchDialog.find") + ": ");
        this.getBtnHighlightAll().setText(Messages.getString(l, "SearchDialog.highlight-all"));
        this.getBtnFind().setText(Messages.getString(l, "SearchDialog.find"));
        this.getCbReplace().setText(Messages.getString(l, "SearchDialog.replace-by") + ": ");
        this.getBtnReplace().setText(Messages.getString(l, "SearchDialog.replace"));
    }

    public void setReplacing(boolean replacing) {
        this.replacing = replacing;
        this.getTfReplace().setEnabled(replacing);
        this.getBtnReplace().setEnabled(replacing);
        this.getBtnReplaceAll().setEnabled(replacing);
        this.getCbReplace().setSelected(replacing);
    }

    public void updateFocus() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SearchDialog.this.tfFind.requestFocusInWindow();
            }
        });
    }
}
