package org.swingeasy;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

import ca.odell.glazedlists.matchers.Matcher;

/**
 * @author Jurgen
 */
public class ETree<T> extends JTree implements ETreeI<T>, ReadableComponent {
	public class TreeFocusScanner extends MouseMotionAdapter {

		protected TreePath lastSelectedPath;

		@Override
		public void mouseMoved(MouseEvent e) {
			JTree tree = (JTree) e.getSource();
			TreeCellRenderer cr = tree.getCellRenderer();
			if (!(cr instanceof ETreeNodeRenderer)) {
				return;
			}
			ETreeNodeRenderer ecr = (ETreeNodeRenderer) cr;
			int x = e.getX();
			int y = e.getY();
			int lastSelected = tree.getRowForLocation(x, y); // set current selected row or -1
			ecr.setLastSelected(lastSelected);
			if (this.lastSelectedPath != null) {
				tree.repaint(tree.getPathBounds(this.lastSelectedPath)); // repaint previous hover over
			}
			if (lastSelected == -1) {
				return; // no selection: nothing to do anymore
			}
			this.lastSelectedPath = tree.getPathForLocation(x, y);
			tree.repaint(tree.getPathBounds(this.lastSelectedPath));// repaint current hover over
		}
	}

	private static final long serialVersionUID = -2866936668266217327L;

	protected ETreeSearchComponent<T> searchComponent = null;

	protected ETreeConfig cfg;

	protected ETree<T> stsi;

	protected ETree() {
		this(new ETreeNode<T>(null));
	}

	public ETree(ETreeConfig cfg, ETreeNode<T> rootNode) {
		super(new javax.swing.tree.DefaultTreeModel(rootNode, true));
		this.init(this.cfg = cfg.lock());
		// FIXME does not seem to work because of popupmenu
		// this.addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent me) {
		//
		// }
		// });
	}

	public ETree(ETreeNode<T> rootNode) {
		this(new ETreeConfig(), rootNode);
	}

	/**
	 * collapse all nodes
	 */
	public void collapseAll() {
		for (int i = this.getRowCount() - 1; i >= 0; i--) {
			this.collapseRow(i);
		}
	}

	/**
	 * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
	 */
	@Override
	public void copy(ActionEvent e) {
		// FIXME
		System.err.println("not implemented");
	}

	/**
	 * expand all nodes
	 */
	public void expandAll() {
		for (int i = this.getRowCount() - 1; i >= 0; i--) {
			this.expandRow(i);
		}
	}

	/**
	 * @see javax.swing.JTree#expandPath(javax.swing.tree.TreePath)
	 */
	@Override
	public void expandPath(TreePath path) {
		super.expandPath(path);
	}

	/**
	 * @see org.swingeasy.ETreeI#getNextMatch(TreePath, String)
	 */
	@Override
	public TreePath getNextMatch(TreePath current, Matcher<T> matcher) {
		int startingRow = this.getRowForPath(current) + 1;
		int max = this.getRowCount();
		if ((startingRow < 0) || (startingRow >= max)) {
			throw new IllegalArgumentException();
		}
		int row = startingRow;
		do {
			TreePath path = this.getPathForRow(row);
			@SuppressWarnings("unchecked")
			ETreeNode<T> treeNode = (ETreeNode<T>) path.getLastPathComponent();
			@SuppressWarnings("unchecked")
			T t = (T) treeNode.getUserObject();
			if (matcher.matches(t)) {
				return path;
			}
			row = (row + 1 + max) % max;
		} while (row != startingRow);
		return null;
	}

	public ETreeI<T> getOriginal() {
		return this;
	}

	/**
	 * @see org.swingeasy.HasParentComponent#getParentComponent()
	 */
	@Override
	public JComponent getParentComponent() {
		return this;
	}

	/**
	 * returns and creates if necessary {@link ETreeSearchComponent}
	 */
	public ETreeSearchComponent<T> getSearchComponent() {
		if (this.searchComponent == null) {
			this.searchComponent = new ETreeSearchComponent<T>(this);
			this.searchComponent.setLocale(this.getLocale());
		}
		return this.searchComponent;
	}

	/**
	 * @see org.swingeasy.ETreeI#getSelectedOrTopNodePath()
	 */
	@Override
	public TreePath getSelectedOrTopNodePath() {
		try {
			return this.getSelectionPath();
		} catch (Exception ex) {
			return this.getTopNodePath();
		}
	}

	/**
	 * JDOC
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ETree<T> getSimpleThreadSafeInterface() {
		try {
			if (this.stsi == null) {
				this.stsi = EventThreadSafeWrapper.getSimpleThreadSafeInterface(ETree.class, this, ETreeI.class);
			}
			return this.stsi;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * @see org.swingeasy.ETreeI#getTopNodePath()
	 */
	@Override
	public TreePath getTopNodePath() {
		return new TreePath(this.getModel().getRoot());
	}

	protected void init(ETreeConfig config) {
		this.setShowsRootHandles(true);
		this.setRootVisible(true);

		ETreeNodeRenderer renderer = new ETreeNodeRenderer();
		this.setCellRenderer(renderer);

		if (config.isTooltips()) {
			ToolTipManager.sharedInstance().registerComponent(this);
		}

		if (config.isEditable()) {
			this.setEditable(true);
			this.setCellEditor(new ETreeNodeEditor());
		}

		if (config.isLocalized()) {
			UIUtils.registerLocaleChangeListener((EComponentI) this);
		}

		if (config.isDefaultPopupMenu()) {
			this.installPopupMenuAction(EComponentPopupMenu.installPopupMenu(this));
		}

		if (config.getFocusColor() != null) {
			renderer.setFocusColor(config.getFocusColor());
			this.addMouseMotionListener(new TreeFocusScanner());
		}
	}

	/**
	 * JDOC
	 */
	protected void installPopupMenuAction(EComponentPopupMenu menu) {
		//
	}

	/**
	 * @see javax.swing.JTree#setCellRenderer(javax.swing.tree.TreeCellRenderer)
	 */
	@Override
	public void setCellRenderer(TreeCellRenderer x) {
		super.setCellRenderer(x);
		if ((this.cfg != null) && (this.cfg.getFocusColor() != null) && (x instanceof ETreeNodeRenderer)) {
			((ETreeNodeRenderer) x).setFocusColor(this.cfg.getFocusColor());
		}
	}

	/**
	 * @see org.swingeasy.ETableI#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		this.repaint();
	}

	/**
	 * @see javax.swing.JTree#setSelectionPath(javax.swing.tree.TreePath)
	 */
	@Override
	public void setSelectionPath(TreePath path) {
		super.setSelectionPath(path);
	}

	/**
	 * @see #getSimpleThreadSafeInterface()
	 */
	public ETree<T> stsi() {
		return this.getSimpleThreadSafeInterface();
	}

	/**
	 * @see #getSimpleThreadSafeInterface()
	 */
	public ETree<T> STSI() {
		return this.getSimpleThreadSafeInterface();
	}
}
