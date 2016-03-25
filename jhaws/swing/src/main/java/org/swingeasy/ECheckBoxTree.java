package org.swingeasy;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public class ECheckBoxTree<T> extends JTree implements ECheckBoxTreeI<T>, ReadableComponent {
    private static final long serialVersionUID = 6378784816121886802L;

    private ECheckBoxTree<T> stsi;

    protected ECheckBoxTreeConfig cfg;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ECheckBoxTree() {
        super(new ECheckBoxTreeNode("root")); //$NON-NLS-1$
    }

    public ECheckBoxTree(ECheckBoxTreeConfig cfg, ECheckBoxTreeNode<T> root) {
        super(root);
        this.init(this.cfg = cfg.lock());
    }

    public ECheckBoxTree(ECheckBoxTreeNode<T> root) {
        this(new ECheckBoxTreeConfig(), root);
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
     */
    @Override
    public void copy(ActionEvent e) {
        // FIXME
        System.err.println("not implemented");
    }

    public ECheckBoxTreeI<T> getOriginal() {
        return this;
    }

    /**
     * 
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this;
    }

    /**
     * JDOC
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ECheckBoxTree<T> getSimpleThreadSafeInterface() {
        try {
            if (this.stsi == null) {
                this.stsi = EventThreadSafeWrapper.getSimpleThreadSafeInterface(ECheckBoxTree.class, this, ECheckBoxTreeI.class);
            }
            return this.stsi;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void init(ECheckBoxTreeConfig config) {
        this.setCellRenderer(new ECheckBoxTreeNodeRenderer());

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }
                TreePath path = ECheckBoxTree.this.getPathForLocation(e.getPoint().x, e.getPoint().y);
                if (path != null) {
                    @SuppressWarnings("unchecked")
                    ECheckBoxTreeNode<T> node = ECheckBoxTreeNode.class.cast(path.getLastPathComponent());
                    node.setSelected(!node.isSelected());
                    DefaultTreeModel.class.cast(ECheckBoxTree.this.getModel()).nodeChanged(node);
                }
            }
        });
        this.setEditable(false);

        if (config.isTooltips()) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        if (config.isLocalized()) {
            UIUtils.registerLocaleChangeListener((EComponentI) this);
        }

        if (config.isDefaultPopupMenu()) {
            this.installPopupMenuAction(EComponentPopupMenu.installPopupMenu(this));
        }
    }

    protected void installPopupMenuAction( EComponentPopupMenu menu) {
        //
    }

    /**
     * 
     * @see org.swingeasy.ETableI#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.repaint();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ECheckBoxTree<T> stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ECheckBoxTree<T> STSI() {
        return this.getSimpleThreadSafeInterface();
    }
}
