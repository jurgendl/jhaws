package org.swingeasy.wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.swingeasy.EButton;
import org.swingeasy.EButtonConfig;
import org.swingeasy.EComponentGradientRenderer.GradientOrientation;
import org.swingeasy.EComponentI;
import org.swingeasy.ELabel;
import org.swingeasy.GradientPanel;
import org.swingeasy.Messages;
import org.swingeasy.UIUtils;

/**
 * @author Jurgen
 */
public class EWizard extends JPanel implements EComponentI {
    protected class WizardListCellRenderer extends ELabel implements ListCellRenderer<WizardPage> {
        private static final long serialVersionUID = -1156206181214408618L;

        protected Font font;

        protected Font selectedFont;

        public WizardListCellRenderer() {
            font = getFont();
            selectedFont = font.deriveFont(Font.BOLD);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends WizardPage> list, WizardPage page, int index, boolean isSelected, boolean cellHasFocus) {
            setFont(wizardPages.get(wizardPage) == page ? selectedFont : font);
            String stringValue = null;
            if (page != null) {
                stringValue = wizardPages.indexOf(page) + 1 + ". " + page.getTitle();
            }
            setText(stringValue);
            return this;
        }
    }

    private static final long serialVersionUID = -1099152219083484268L;

    protected static final String PAGES_SUFFIX = ":                                         ";

    protected ELabel lblIcon;

    protected JPanel mainPanel;

    protected int wizardPage = 0;

    protected final List<WizardPage> wizardPages = new ArrayList<>();

    protected JSeparator separatorTop;

    protected EButton btnHelp;

    protected EButton btnCancel;

    protected EButton btnBack;

    protected EButton btnNext;

    protected EButton btnFinish;

    protected JList<WizardPage> pageList;

    protected JPanel topSubPanel;

    protected ELabel lblTitle;

    protected ELabel lblDescription;

    protected GradientPanel leftPanel;

    protected GradientPanel topPanel;

    protected ELabel lblPages;

    /**
     * Created by Eclipse WindowBuilder.
     */
    public EWizard() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0 };
        gridBagLayout.columnWeights = new double[] { 1.0 };
        setLayout(gridBagLayout);

        topPanel = new GradientPanel();
        topPanel.getGradientRenderer().setOrientation(GradientOrientation.HORIZONTAL);
        GridBagConstraints gbc_topPanel = new GridBagConstraints();
        gbc_topPanel.anchor = GridBagConstraints.NORTH;
        gbc_topPanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_topPanel.gridx = 0;
        gbc_topPanel.gridy = 0;
        this.add(topPanel, gbc_topPanel);
        topPanel.setLayout(new BorderLayout(10, 10));

        lblIcon = new ELabel("");
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcon.setRequestFocusEnabled(false);
        lblIcon.setPreferredSize(new Dimension(80, 80));
        lblIcon.setMinimumSize(new Dimension(80, 80));
        lblIcon.setMaximumSize(new Dimension(80, 80));
        topPanel.add(lblIcon, BorderLayout.EAST);

        topSubPanel = new JPanel();
        topSubPanel.setOpaque(false);
        topPanel.add(topSubPanel, BorderLayout.CENTER);
        topSubPanel.setLayout(new BorderLayout(0, 0));

        lblTitle = new ELabel("");
        lblTitle.setRequestFocusEnabled(false);
        lblTitle.setBorder(new EmptyBorder(8, 14, 0, 0));
        lblTitle.setFont(lblTitle.getFont().deriveFont(lblTitle.getFont().getStyle() | Font.BOLD));
        topSubPanel.add(lblTitle, BorderLayout.NORTH);

        lblDescription = new ELabel("");
        lblDescription.setRequestFocusEnabled(false);
        lblDescription.setVerticalAlignment(SwingConstants.TOP);
        lblDescription.setBorder(new EmptyBorder(6, 28, 0, 4));
        topSubPanel.add(lblDescription, BorderLayout.CENTER);

        separatorTop = new JSeparator();
        GridBagConstraints gbc_separatorTop = new GridBagConstraints();
        gbc_separatorTop.fill = GridBagConstraints.HORIZONTAL;
        gbc_separatorTop.gridx = 0;
        gbc_separatorTop.gridy = 1;
        this.add(separatorTop, gbc_separatorTop);

        JPanel centerPanel = new JPanel();
        GridBagConstraints gbc_centerPanel = new GridBagConstraints();
        gbc_centerPanel.fill = GridBagConstraints.BOTH;
        gbc_centerPanel.gridx = 0;
        gbc_centerPanel.gridy = 2;
        this.add(centerPanel, gbc_centerPanel);
        GridBagLayout gbl_centerPanel = new GridBagLayout();
        gbl_centerPanel.columnWeights = new double[] { 0.0, 0.0, 1.0 };
        gbl_centerPanel.rowWeights = new double[] { 1.0 };
        centerPanel.setLayout(gbl_centerPanel);

        leftPanel = new GradientPanel();
        leftPanel.setPreferredSize(new Dimension(240, 0));
        leftPanel.setMinimumSize(new Dimension(240, 0));
        GridBagConstraints gbc_leftPanel = new GridBagConstraints();
        gbc_leftPanel.fill = GridBagConstraints.VERTICAL;
        gbc_leftPanel.gridx = 0;
        gbc_leftPanel.gridy = 0;
        centerPanel.add(leftPanel, gbc_leftPanel);

        lblPages = new ELabel("Steps" + EWizard.PAGES_SUFFIX);
        Font original = lblPages.getFont();
        @SuppressWarnings("unchecked")
        Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) original.getAttributes();
        attributes.put(java.awt.font.TextAttribute.UNDERLINE, java.awt.font.TextAttribute.UNDERLINE_ON);
        attributes.put(java.awt.font.TextAttribute.WEIGHT, java.awt.font.TextAttribute.WEIGHT_BOLD);
        lblPages.setFont(original.deriveFont(attributes));
        lblPages.setRequestFocusEnabled(false);

        pageList = new JList<>();
        pageList.setRequestFocusEnabled(false);
        pageList.setBorder(new EmptyBorder(4, 14, 10, 10));
        pageList.setOpaque(false);
        GroupLayout gl_leftPanel = new GroupLayout(leftPanel);
        gl_leftPanel.setHorizontalGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
                gl_leftPanel.createSequentialGroup().addContainerGap().addGroup(
                        gl_leftPanel.createParallelGroup(Alignment.TRAILING).addComponent(lblPages, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE).addComponent(pageList, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
                        .addContainerGap()));
        gl_leftPanel.setVerticalGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_leftPanel.createSequentialGroup().addGap(8).addComponent(lblPages).addPreferredGap(ComponentPlacement.RELATED).addComponent(pageList, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE).addContainerGap()));
        leftPanel.setLayout(gl_leftPanel);

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.VERTICAL);
        GridBagConstraints gbc_separator = new GridBagConstraints();
        gbc_separator.fill = GridBagConstraints.VERTICAL;
        gbc_separator.gridx = 1;
        gbc_separator.gridy = 0;
        centerPanel.add(separator, gbc_separator);

        mainPanel = new JPanel();
        GridBagConstraints gbc_mainPanel = new GridBagConstraints();
        gbc_mainPanel.fill = GridBagConstraints.BOTH;
        gbc_mainPanel.gridx = 2;
        gbc_mainPanel.gridy = 0;
        centerPanel.add(mainPanel, gbc_mainPanel);
        mainPanel.setLayout(new CardLayout(0, 0));

        JSeparator separatorBottom = new JSeparator();
        GridBagConstraints gbc_separatorBottom = new GridBagConstraints();
        gbc_separatorBottom.fill = GridBagConstraints.HORIZONTAL;
        gbc_separatorBottom.gridx = 0;
        gbc_separatorBottom.gridy = 3;
        this.add(separatorBottom, gbc_separatorBottom);

        JPanel bottomPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) bottomPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        GridBagConstraints gbc_bottomPanel = new GridBagConstraints();
        gbc_bottomPanel.fill = GridBagConstraints.BOTH;
        gbc_bottomPanel.gridx = 0;
        gbc_bottomPanel.gridy = 4;
        this.add(bottomPanel, gbc_bottomPanel);

        btnHelp = new EButton(new EButtonConfig("Help"));
        btnHelp.setPreferredSize(getButtonSize());
        bottomPanel.add(btnHelp);

        btnCancel = new EButton(new EButtonConfig("Cancel"));
        btnCancel.setPreferredSize(getButtonSize());
        bottomPanel.add(btnCancel);

        btnBack = new EButton(new EButtonConfig("< Back"));
        btnBack.setPreferredSize(getButtonSize());
        btnBack.addActionListener(e -> EWizard.this.back());
        bottomPanel.add(btnBack);

        btnNext = new EButton(new EButtonConfig("Next >"));
        btnNext.addActionListener(e -> EWizard.this.next());
        btnNext.setPreferredSize(getButtonSize());
        bottomPanel.add(btnNext);

        btnFinish = new EButton(new EButtonConfig("Finish"));
        btnFinish.addActionListener(e -> EWizard.this.finish());
        btnFinish.setPreferredSize(getButtonSize());
        bottomPanel.add(btnFinish);

        // leave at bottom
        UIUtils.registerLocaleChangeListener((EComponentI) this);
    }

    public void addWizardPage(WizardPage page) {
        wizardPages.add(page);
    }

    protected void back() {
        if (0 < wizardPage) {
            wizardPage--;
            updatePage();
        }
    }

    protected void finish() {
        wizardPage = wizardPages.size() - 1;
        updatePage();
    }

    protected EButton getBtnBack() {
        return btnBack;
    }

    protected EButton getBtnCancel() {
        return btnCancel;
    }

    protected EButton getBtnFinish() {
        return btnFinish;
    }

    protected EButton getBtnHelp() {
        return btnHelp;
    }

    protected EButton getBtnNext() {
        return btnNext;
    }

    protected Dimension getButtonSize() {
        return new Dimension(100, 25);
    }

    protected ELabel getLblDescription() {
        return lblDescription;
    }

    protected ELabel getLblIcon() {
        return lblIcon;
    }

    protected ELabel getLblPages() {
        return lblPages;
    }

    protected ELabel getLblTitle() {
        return lblTitle;
    }

    protected GradientPanel getLeftPanel() {
        return leftPanel;
    }

    protected JPanel getMainPanel() {
        return mainPanel;
    }

    protected JList<WizardPage> getPageList() {
        return pageList;
    }

    protected GradientPanel getTopPanel() {
        return topPanel;
    }

    public void init() {
        int i = 0;
        DefaultListModel<WizardPage> pageListModel = new DefaultListModel<>();
        getPageList().setModel(pageListModel);
        getPageList().setCellRenderer(new WizardListCellRenderer());
        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel() {
            private static final long serialVersionUID = 6618282932208651223L;

            @Override
            public void setSelectionInterval(int index0, int index1) {
                //
            }
        };
        getPageList().setSelectionModel(selectionModel);
        for (WizardPage page : wizardPages) {
            pageListModel.add(i, page);
            getMainPanel().add(page.createComponent(), String.valueOf(i++));
        }

        if (getBtnCancel().getAction() == null) {
            getBtnCancel().setVisible(false);
        }
        if (getBtnHelp().getAction() == null) {
            getBtnHelp().setVisible(false);
        }

        getLblDescription().setPreferredSize(new Dimension(0, 0));

        updatePage();
    }

    protected void next() {
        if (wizardPage < wizardPages.size() - 1) {
            wizardPage++;
            updatePage();
        }
    }

    public void setCancelAction(Action cancelAction) {
        String text = getBtnCancel().getText();
        getBtnCancel().setHideActionText(true);
        getBtnCancel().setAction(cancelAction);
        getBtnCancel().setText(text);
    }

    public void setHelpAction(Action helpAction) {
        String text = getBtnHelp().getText();
        getBtnHelp().setHideActionText(true);
        getBtnHelp().setAction(helpAction);
        getBtnHelp().setText(text);
    }

    public void setIcon(Icon icon) {
        getLblIcon().setIcon(icon);
    }

    public void setLeftPanelVisible(boolean b) {
        if (b) {
            getLeftPanel().setPreferredSize(new Dimension(240, 0));
            getLeftPanel().setMinimumSize(new Dimension(240, 0));
        } else {
            getLeftPanel().setPreferredSize(new Dimension(0, 0));
            getLeftPanel().setMinimumSize(new Dimension(0, 0));
        }
    }

    /**
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        getLblPages().setText(Messages.getString(l, "EWizard.steps") + EWizard.PAGES_SUFFIX);
        getBtnBack().setText(Messages.getString(l, "EWizard.back"));
        getBtnCancel().setText(Messages.getString(l, "EWizard.cancel"));
        getBtnFinish().setText(Messages.getString(l, "EWizard.finish"));
        getBtnHelp().setText(Messages.getString(l, "EWizard.help"));
        getBtnNext().setText(Messages.getString(l, "EWizard.next"));
        this.repaint();
    }

    public void setTopPanelVisible(boolean b) {
        getTopPanel().setVisible(b);
    }

    protected void updatePage() {
        CardLayout cl = (CardLayout) getMainPanel().getLayout();
        cl.show(getMainPanel(), String.valueOf(wizardPage));

        WizardPage page = wizardPages.get(wizardPage);
        getLblTitle().setText(page.getTitle());
        getLblDescription().setText("<html><p>" + page.getDescription().replace("\r\n", "<br>").replace("\n", "<br>").replace("\r", "<br>") + "</p></html>");

        getBtnBack().setEnabled(0 < wizardPage);
        getBtnNext().setEnabled(wizardPage < wizardPages.size() - 1 && wizardPages.get(wizardPage + 1).validate());
        boolean valid = true;
        for (int pi = wizardPage + 1; pi < wizardPages.size(); pi++) {
            if (!wizardPages.get(pi).validate()) {
                valid = false;
                break;
            }
        }
        getBtnFinish().setEnabled(valid && wizardPage < wizardPages.size() - 2);

        getPageList().setSelectedIndex(wizardPage);
        getPageList().repaint();
    }
}
