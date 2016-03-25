package org.swingeasy.wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
			this.font = this.getFont();
			this.selectedFont = this.font.deriveFont(Font.BOLD);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends WizardPage> list, WizardPage page, int index, boolean isSelected, boolean cellHasFocus) {
			this.setFont(EWizard.this.wizardPages.get(EWizard.this.wizardPage) == page ? this.selectedFont : this.font);
			String stringValue = null;
			if (page != null) {
				stringValue = (EWizard.this.wizardPages.indexOf(page) + 1) + ". " + page.getTitle();
			}
			this.setText(stringValue);
			return this;
		}
	}

	private static final long serialVersionUID = -1099152219083484268L;

	protected static final String PAGES_SUFFIX = ":                                         ";

	protected ELabel lblIcon;

	protected JPanel mainPanel;

	protected int wizardPage = 0;

	protected final List<WizardPage> wizardPages = new ArrayList<WizardPage>();

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
		this.setLayout(gridBagLayout);

		this.topPanel = new GradientPanel();
		this.topPanel.getGradientRenderer().setOrientation(GradientOrientation.HORIZONTAL);
		GridBagConstraints gbc_topPanel = new GridBagConstraints();
		gbc_topPanel.anchor = GridBagConstraints.NORTH;
		gbc_topPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_topPanel.gridx = 0;
		gbc_topPanel.gridy = 0;
		this.add(this.topPanel, gbc_topPanel);
		this.topPanel.setLayout(new BorderLayout(10, 10));

		this.lblIcon = new ELabel("");
		this.lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		this.lblIcon.setRequestFocusEnabled(false);
		this.lblIcon.setPreferredSize(new Dimension(80, 80));
		this.lblIcon.setMinimumSize(new Dimension(80, 80));
		this.lblIcon.setMaximumSize(new Dimension(80, 80));
		this.topPanel.add(this.lblIcon, BorderLayout.EAST);

		this.topSubPanel = new JPanel();
		this.topSubPanel.setOpaque(false);
		this.topPanel.add(this.topSubPanel, BorderLayout.CENTER);
		this.topSubPanel.setLayout(new BorderLayout(0, 0));

		this.lblTitle = new ELabel("");
		this.lblTitle.setRequestFocusEnabled(false);
		this.lblTitle.setBorder(new EmptyBorder(8, 14, 0, 0));
		this.lblTitle.setFont(this.lblTitle.getFont().deriveFont(this.lblTitle.getFont().getStyle() | Font.BOLD));
		this.topSubPanel.add(this.lblTitle, BorderLayout.NORTH);

		this.lblDescription = new ELabel("");
		this.lblDescription.setRequestFocusEnabled(false);
		this.lblDescription.setVerticalAlignment(SwingConstants.TOP);
		this.lblDescription.setBorder(new EmptyBorder(6, 28, 0, 4));
		this.topSubPanel.add(this.lblDescription, BorderLayout.CENTER);

		this.separatorTop = new JSeparator();
		GridBagConstraints gbc_separatorTop = new GridBagConstraints();
		gbc_separatorTop.fill = GridBagConstraints.HORIZONTAL;
		gbc_separatorTop.gridx = 0;
		gbc_separatorTop.gridy = 1;
		this.add(this.separatorTop, gbc_separatorTop);

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

		this.leftPanel = new GradientPanel();
		this.leftPanel.setPreferredSize(new Dimension(240, 0));
		this.leftPanel.setMinimumSize(new Dimension(240, 0));
		GridBagConstraints gbc_leftPanel = new GridBagConstraints();
		gbc_leftPanel.fill = GridBagConstraints.VERTICAL;
		gbc_leftPanel.gridx = 0;
		gbc_leftPanel.gridy = 0;
		centerPanel.add(this.leftPanel, gbc_leftPanel);

		this.lblPages = new ELabel("Steps" + EWizard.PAGES_SUFFIX);
		Font original = this.lblPages.getFont();
		@SuppressWarnings("unchecked")
		Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) original.getAttributes();
		attributes.put(java.awt.font.TextAttribute.UNDERLINE, java.awt.font.TextAttribute.UNDERLINE_ON);
		attributes.put(java.awt.font.TextAttribute.WEIGHT, java.awt.font.TextAttribute.WEIGHT_BOLD);
		this.lblPages.setFont(original.deriveFont(attributes));
		this.lblPages.setRequestFocusEnabled(false);

		this.pageList = new JList<>();
		this.pageList.setRequestFocusEnabled(false);
		this.pageList.setBorder(new EmptyBorder(4, 14, 10, 10));
		this.pageList.setOpaque(false);
		GroupLayout gl_leftPanel = new GroupLayout(this.leftPanel);
		gl_leftPanel
				.setHorizontalGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						gl_leftPanel.createSequentialGroup().addContainerGap()
								.addGroup(gl_leftPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(this.lblPages, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
										.addComponent(this.pageList, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
								.addContainerGap()));
		gl_leftPanel.setVerticalGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_leftPanel.createSequentialGroup().addGap(8).addComponent(this.lblPages)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(this.pageList, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE).addContainerGap()));
		this.leftPanel.setLayout(gl_leftPanel);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.VERTICAL;
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 0;
		centerPanel.add(separator, gbc_separator);

		this.mainPanel = new JPanel();
		GridBagConstraints gbc_mainPanel = new GridBagConstraints();
		gbc_mainPanel.fill = GridBagConstraints.BOTH;
		gbc_mainPanel.gridx = 2;
		gbc_mainPanel.gridy = 0;
		centerPanel.add(this.mainPanel, gbc_mainPanel);
		this.mainPanel.setLayout(new CardLayout(0, 0));

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

		this.btnHelp = new EButton(new EButtonConfig("Help"));
		this.btnHelp.setPreferredSize(this.getButtonSize());
		bottomPanel.add(this.btnHelp);

		this.btnCancel = new EButton(new EButtonConfig("Cancel"));
		this.btnCancel.setPreferredSize(this.getButtonSize());
		bottomPanel.add(this.btnCancel);

		this.btnBack = new EButton(new EButtonConfig("< Back"));
		this.btnBack.setPreferredSize(this.getButtonSize());
		this.btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EWizard.this.back();
			}
		});
		bottomPanel.add(this.btnBack);

		this.btnNext = new EButton(new EButtonConfig("Next >"));
		this.btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EWizard.this.next();
			}
		});
		this.btnNext.setPreferredSize(this.getButtonSize());
		bottomPanel.add(this.btnNext);

		this.btnFinish = new EButton(new EButtonConfig("Finish"));
		this.btnFinish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EWizard.this.finish();
			}
		});
		this.btnFinish.setPreferredSize(this.getButtonSize());
		bottomPanel.add(this.btnFinish);

		// leave at bottom
		UIUtils.registerLocaleChangeListener((EComponentI) this);
	}

	public void addWizardPage(WizardPage page) {
		this.wizardPages.add(page);
	}

	protected void back() {
		if (0 < this.wizardPage) {
			this.wizardPage--;
			this.updatePage();
		}
	}

	protected void finish() {
		this.wizardPage = this.wizardPages.size() - 1;
		this.updatePage();
	}

	protected EButton getBtnBack() {
		return this.btnBack;
	}

	protected EButton getBtnCancel() {
		return this.btnCancel;
	}

	protected EButton getBtnFinish() {
		return this.btnFinish;
	}

	protected EButton getBtnHelp() {
		return this.btnHelp;
	}

	protected EButton getBtnNext() {
		return this.btnNext;
	}

	protected Dimension getButtonSize() {
		return new Dimension(100, 25);
	}

	protected ELabel getLblDescription() {
		return this.lblDescription;
	}

	protected ELabel getLblIcon() {
		return this.lblIcon;
	}

	protected ELabel getLblPages() {
		return this.lblPages;
	}

	protected ELabel getLblTitle() {
		return this.lblTitle;
	}

	protected GradientPanel getLeftPanel() {
		return this.leftPanel;
	}

	protected JPanel getMainPanel() {
		return this.mainPanel;
	}

	protected JList<WizardPage> getPageList() {
		return this.pageList;
	}

	protected GradientPanel getTopPanel() {
		return this.topPanel;
	}

	public void init() {
		int i = 0;
		DefaultListModel<WizardPage> pageListModel = new DefaultListModel<WizardPage>();
		this.getPageList().setModel(pageListModel);
		this.getPageList().setCellRenderer(new WizardListCellRenderer());
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel() {
			private static final long serialVersionUID = 6618282932208651223L;

			@Override
			public void setSelectionInterval(int index0, int index1) {
				//
			}
		};
		this.getPageList().setSelectionModel(selectionModel);
		for (WizardPage page : this.wizardPages) {
			pageListModel.add(i, page);
			this.getMainPanel().add(page.createComponent(), String.valueOf(i++));
		}

		if (this.getBtnCancel().getAction() == null) {
			this.getBtnCancel().setVisible(false);
		}
		if (this.getBtnHelp().getAction() == null) {
			this.getBtnHelp().setVisible(false);
		}

		this.getLblDescription().setPreferredSize(new Dimension(0, 0));

		this.updatePage();
	}

	protected void next() {
		if (this.wizardPage < (this.wizardPages.size() - 1)) {
			this.wizardPage++;
			this.updatePage();
		}
	}

	public void setCancelAction(Action cancelAction) {
		String text = this.getBtnCancel().getText();
		this.getBtnCancel().setHideActionText(true);
		this.getBtnCancel().setAction(cancelAction);
		this.getBtnCancel().setText(text);
	}

	public void setHelpAction(Action helpAction) {
		String text = this.getBtnHelp().getText();
		this.getBtnHelp().setHideActionText(true);
		this.getBtnHelp().setAction(helpAction);
		this.getBtnHelp().setText(text);
	}

	public void setIcon(Icon icon) {
		this.getLblIcon().setIcon(icon);
	}

	public void setLeftPanelVisible(boolean b) {
		if (b) {
			this.getLeftPanel().setPreferredSize(new Dimension(240, 0));
			this.getLeftPanel().setMinimumSize(new Dimension(240, 0));
		} else {
			this.getLeftPanel().setPreferredSize(new Dimension(0, 0));
			this.getLeftPanel().setMinimumSize(new Dimension(0, 0));
		}
	}

	/**
	 * 
	 * @see java.awt.Component#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		this.getLblPages().setText(Messages.getString(l, "EWizard.steps") + EWizard.PAGES_SUFFIX);
		this.getBtnBack().setText(Messages.getString(l, "EWizard.back"));
		this.getBtnCancel().setText(Messages.getString(l, "EWizard.cancel"));
		this.getBtnFinish().setText(Messages.getString(l, "EWizard.finish"));
		this.getBtnHelp().setText(Messages.getString(l, "EWizard.help"));
		this.getBtnNext().setText(Messages.getString(l, "EWizard.next"));
		this.repaint();
	}

	public void setTopPanelVisible(boolean b) {
		this.getTopPanel().setVisible(b);
	}

	protected void updatePage() {
		CardLayout cl = (CardLayout) this.getMainPanel().getLayout();
		cl.show(this.getMainPanel(), String.valueOf(this.wizardPage));

		WizardPage page = this.wizardPages.get(this.wizardPage);
		this.getLblTitle().setText(page.getTitle());
		this.getLblDescription().setText("<html><p>" + page.getDescription().replaceAll("\r\n", "<br>").replaceAll("\n", "<br>").replaceAll("\r", "<br>") + "</p></html>");

		this.getBtnBack().setEnabled(0 < this.wizardPage);
		this.getBtnNext().setEnabled((this.wizardPage < (this.wizardPages.size() - 1)) && this.wizardPages.get(this.wizardPage + 1).validate());
		boolean valid = true;
		for (int pi = this.wizardPage + 1; pi < this.wizardPages.size(); pi++) {
			if (!this.wizardPages.get(pi).validate()) {
				valid = false;
				break;
			}
		}
		this.getBtnFinish().setEnabled(valid && (this.wizardPage < (this.wizardPages.size() - 2)));

		this.getPageList().setSelectedIndex(this.wizardPage);
		this.getPageList().repaint();
	}
}
