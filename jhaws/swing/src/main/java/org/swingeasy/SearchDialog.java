package org.swingeasy;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;
import org.swingeasy.system.SystemSettings;

import net.miginfocom.swing.MigLayout;

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
        super(UIUtils.getRootWindow(textComponent), Messages.getString(SystemSettings.getCurrentLocale(), "SearchDialog.title"), ModalityType.MODELESS);
        this.textComponent = textComponent;
        init();
        setLocationRelativeTo(UIUtils.getRootWindow(textComponent));
        setReplacing(replacing && textComponent.isEditable() && textComponent.isEnabled());
        String selectedText = textComponent.getSelectedText();
        if (StringUtils.isNotBlank(selectedText)) {
            tfFind.setText(selectedText);
        }
        UIUtils.registerLocaleChangeListener((EComponentI) this);
    }

    protected void closed() {
        // this.textComponent.removeHighlights();
    }

    protected void find(String find) {
        textComponent.find(find);
    }

    protected EButton getBtnClose() {
        if (btnClose == null) {
            btnClose = new EButton(new EButtonConfig());
        }
        return btnClose;
    }

    protected EButton getBtnFind() {
        if (btnFind == null) {
            btnFind = new EButton(new EButtonConfig());
        }
        return btnFind;
    }

    protected EButton getBtnHighlightAll() {
        if (btnHighlightAll == null) {
            btnHighlightAll = new EButton(new EButtonConfig());
        }
        return btnHighlightAll;
    }

    protected EButton getBtnReplace() {
        if (btnReplace == null) {
            btnReplace = new EButton(new EButtonConfig());
        }
        return btnReplace;
    }

    protected EButton getBtnReplaceAll() {
        if (btnReplaceAll == null) {
            btnReplaceAll = new EButton(new EButtonConfig());
        }
        return btnReplaceAll;
    }

    protected ECheckBox getCbReplace() {
        if (cbReplace == null) {
            cbReplace = new ECheckBox(new ECheckBoxConfig());
            cbReplace.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        return cbReplace;
    }

    protected ELabel getLblFind() {
        if (lblFind == null) {
            lblFind = new ELabel(new ELabelConfig("").setHorizontalAlignment(SwingConstants.RIGHT));
        }
        return lblFind;
    }

    protected ETextField getTfFind() {
        if (tfFind == null) {
            tfFind = new ETextField(new ETextFieldConfig());
        }
        return tfFind;
    }

    protected ETextField getTfReplace() {
        if (tfReplace == null) {
            tfReplace = new ETextField(new ETextFieldConfig());
        }
        return tfReplace;
    }

    protected void highlightAll(String find) {
        textComponent.highlightAll(find);
    }

    protected void init() {
        addWindowListener(new WindowAdapter() {
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
        setLayout(new MigLayout(layoutConstraints, colConstraints, rowConstraints));

        this.add(getLblFind());
        this.add(getTfFind());
        this.add(getBtnFind());
        this.add(getBtnHighlightAll(), "wrap");

        getCbReplace().setEnabled(textComponent.isEditable() && textComponent.isEnabled());
        this.add(getCbReplace());

        getCbReplace().addValueChangeListener(value -> SearchDialog.this.setReplacing(value));

        this.add(getTfReplace());
        this.add(getBtnReplace());
        this.add(getBtnReplaceAll(), "wrap");

        this.add(new ELabel(), "span 3");
        this.add(getBtnClose());

        ActionListener findAction = e -> {
            if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                try {
                    SearchDialog.this.find(SearchDialog.this.getTfFind().getText().trim());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        tfFind.addActionListener(findAction);
        getBtnFind().addActionListener(findAction);
        getBtnHighlightAll().addActionListener(e -> {
            if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                try {
                    SearchDialog.this.highlightAll(SearchDialog.this.getTfFind().getText().trim());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        ActionListener replaceAtion = e -> {
            if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                try {
                    SearchDialog.this.replace(SearchDialog.this.getTfFind().getText().trim(), SearchDialog.this.getTfReplace().getText().trim());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        getTfReplace().addActionListener(replaceAtion);
        getBtnReplace().addActionListener(replaceAtion);
        getBtnReplaceAll().addActionListener(e -> {
            if (SearchDialog.this.getTfFind().getText().trim().length() > 0) {
                try {
                    SearchDialog.this.replaceAll(SearchDialog.this.getTfFind().getText().trim(), SearchDialog.this.getTfReplace().getText().trim());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        getBtnClose().addActionListener(e -> {
            try {
                SearchDialog.this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        pack();
        setResizable(false);
        getBtnReplaceAll().setName("SearchDialog.replace-all");
        getBtnClose().setName("SearchDialog.cancelButtonText");
        getLblFind().setName("SearchDialog.find");
        getBtnHighlightAll().setName("SearchDialog.highlight-all");
        getBtnFind().setName("SearchDialog.find");
        getCbReplace().setName("SearchDialog.replace-by");
        getBtnReplace().setName("SearchDialog.replace");
        getTfFind().setName("SearchDialog.find.input");
        getTfReplace().setName("SearchDialog.replace.input");
    }

    public boolean isReplacing() {
        return replacing;
    }

    protected void replace(String find, String replace) {
        textComponent.replace(find, replace);
    }

    protected void replaceAll(String find, String replace) {
        textComponent.replaceAll(find, replace);
    }

    /**
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        setTitle(Messages.getString(l, "SearchDialog.title"));
        getBtnReplaceAll().setText(Messages.getString(l, "SearchDialog.replace-all"));
        getBtnClose().setText(Messages.getString(l, "SearchDialog.cancelButtonText"));
        getLblFind().setText(Messages.getString(l, "SearchDialog.find") + ": ");
        getBtnHighlightAll().setText(Messages.getString(l, "SearchDialog.highlight-all"));
        getBtnFind().setText(Messages.getString(l, "SearchDialog.find"));
        getCbReplace().setText(Messages.getString(l, "SearchDialog.replace-by") + ": ");
        getBtnReplace().setText(Messages.getString(l, "SearchDialog.replace"));
    }

    public void setReplacing(boolean replacing) {
        this.replacing = replacing;
        getTfReplace().setEnabled(replacing);
        getBtnReplace().setEnabled(replacing);
        getBtnReplaceAll().setEnabled(replacing);
        getCbReplace().setSelected(replacing);
    }

    public void updateFocus() {
        SwingUtilities.invokeLater(() -> tfFind.requestFocusInWindow());
    }
}
