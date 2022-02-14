package org.swingeasy;

import java.awt.Component;
import java.awt.Event;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.swingeasy.system.SystemSettings;

/**
 * @see http://java-swing-tips.blogspot.com/2010/11/jtable-celleditor-popupmenu.html
 * @author Jurgen
 */
public class EComponentPopupMenu extends JPopupMenu implements EComponentI {
    /**
     * adapter for {@link AncestorListener}
     */
    public static class AncestorAdapter implements AncestorListener {
        /**
         * @see javax.swing.event.AncestorListener#ancestorAdded(javax.swing.event.AncestorEvent)
         */
        @Override
        public void ancestorAdded(AncestorEvent event) {
            //
        }

        /**
         * @see javax.swing.event.AncestorListener#ancestorMoved(javax.swing.event.AncestorEvent)
         */
        @Override
        public void ancestorMoved(AncestorEvent event) {
            //
        }

        /**
         * @see javax.swing.event.AncestorListener#ancestorRemoved(javax.swing.event.AncestorEvent)
         */
        @Override
        public void ancestorRemoved(AncestorEvent event) {
            //
        }
    }

    public static class CheckEnabled {
        protected final boolean hasSelection;

        protected final boolean hasText;

        protected final boolean canUndo;

        protected final boolean canRedo;

        private transient String toString;

        protected CheckEnabled(boolean hasSelection, boolean hasText, boolean canUndo, boolean canRedo) {
            this.hasSelection = hasSelection;
            this.hasText = hasText;
            this.canUndo = canUndo;
            this.canRedo = canRedo;
        }

        public boolean isCanRedo() {
            return canRedo;
        }

        public boolean isCanUndo() {
            return canUndo;
        }

        public boolean isHasSelection() {
            return hasSelection;
        }

        public boolean isHasText() {
            return hasText;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            if (toString == null) {
                toString = new ToStringBuilder(this).append("hasSelection", hasSelection).append("hasText", hasText).append("canUndo", canUndo).append("canRedo", canRedo).toString();
            }
            return toString;
        }
    }

    /**
     * adapter for {@link ClipboardOwner}
     */
    public static class ClipboardOwnerAdapter implements ClipboardOwner {
        @Override
        public void lostOwnership(Clipboard clipboard, Transferable contents) {
            //
        }
    }

    /**
     * CopyAction
     */
    protected static class CopyAction extends EComponentPopupMenuAction<ReadableComponent> {
        private static final long serialVersionUID = 3044725124645042202L;

        public CopyAction(ReadableComponent component) {
            super(component, EComponentPopupMenu.COPY, Resources.getImageResource("page_copy.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.copy(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            setEnabled(true);
            return true;
        }
    }

    /**
     * CutAction
     */
    protected static class CutAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 4328082010034890480L;

        public CutAction(WritableComponent component) {
            super(component, EComponentPopupMenu.CUT, Resources.getImageResource("cut.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.cut(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            setEnabled(cfg.hasSelection);
            return cfg.hasSelection;
        }
    }

    /**
     * DeleteAction
     */
    protected static class DeleteAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -7609111337852520512L;

        public DeleteAction(WritableComponent component) {
            super(component, EComponentPopupMenu.DELETE, Resources.getImageResource("bin_closed.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.delete(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            setEnabled(cfg.hasSelection);
            return cfg.hasSelection;
        }
    }

    /**
     * DeleteAllAction
     */
    protected static class DeleteAllAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -6873629703224034266L;

        public DeleteAllAction(WritableComponent component) {
            super(component, EComponentPopupMenu.DELETE_ALL, Resources.getImageResource("bin_closed.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, Event.CTRL_MASK));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.selectAll(e);
            delegate.delete(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            setEnabled(cfg.hasText);
            return cfg.hasText;
        }
    }

    /**
     * JDOC
     */
    public abstract static class EComponentPopupMenuAction<C extends ReadableComponent> extends AbstractAction implements EComponentI, HasParentComponent {
        private static final long serialVersionUID = 3408961844539862485L;

        protected String key;

        protected final C delegate;

        public EComponentPopupMenuAction(C delegate, String name, Icon icon) {
            super(name, icon);
            this.key = name;
            this.delegate = delegate;
            super.putValue(Action.ACTION_COMMAND_KEY, this.key);
        }

        public abstract boolean checkEnabled(CheckEnabled cfg);

        /**
         * @see org.swingeasy.HasParentComponent#getParentComponent()
         */
        @Override
        public JComponent getParentComponent() {
            return this.delegate.getParentComponent();
        }

        /**
         * @see org.swingeasy.EComponentI#setLocale(java.util.Locale)
         */
        @Override
        public void setLocale(Locale l) {
            String name = Messages.getString(l, "EComponentPopupMenuAction." + this.key);
            super.putValue(Action.NAME, name);
            {
                String shortDescriptionKey = "EComponentPopupMenuAction." + this.key + "." + Action.SHORT_DESCRIPTION;
                String shortDescription = Messages.getString(l, shortDescriptionKey);
                if (shortDescriptionKey.equals(shortDescription)) {
                    shortDescription = name;
                }
                super.putValue(Action.SHORT_DESCRIPTION, shortDescription);
            }
            {
                String longDescriptionKey = "EComponentPopupMenuAction." + this.key + "." + Action.LONG_DESCRIPTION;
                String longDescription = Messages.getString(l, longDescriptionKey);
                if (longDescriptionKey.equals(longDescription)) {
                    longDescription = name;
                }
                super.putValue(Action.LONG_DESCRIPTION, longDescription);
            }
        }
    }

    /**
     * FindAction
     */
    protected static class FindAction extends EComponentPopupMenuAction<ReadableTextComponent> {
        private static final long serialVersionUID = 4328082010034890480L;

        public FindAction(ReadableTextComponent component) {
            super(component, EComponentPopupMenu.FIND, Resources.getImageResource("find.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.find(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            // this.setEnabled(cfg.hasText);
            // return cfg.hasText;
            setEnabled(true);
            return true;
        }
    }

    /**
     * FindNextAction
     */
    protected static class FindNextAction extends EComponentPopupMenuAction<ReadableTextComponent> {
        private static final long serialVersionUID = 4328082010034890480L;

        public FindNextAction(ReadableTextComponent component) {
            super(component, EComponentPopupMenu.FIND_NEXT, Resources.getImageResource("find.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.findNext(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            // this.setEnabled(cfg.hasText);
            // return cfg.hasText;
            setEnabled(true);
            return true;
        }
    }

    /**
     * GotoBeginAction
     */
    protected static class GotoBeginAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 3085509525399492253L;

        public GotoBeginAction(WritableComponent component) {
            super(component, EComponentPopupMenu.GOTO_BEGIN, Resources.getImageResource("arrow_up.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, Event.CTRL_MASK));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.gotoBegin(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            setEnabled(cfg.hasText);
            return cfg.hasText;
        }
    }

    /**
     * GotoEndAction
     */
    protected static class GotoEndAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 6262977802889470104L;

        public GotoEndAction(WritableComponent component) {
            super(component, EComponentPopupMenu.GOTO_END, Resources.getImageResource("arrow_down.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_END, Event.CTRL_MASK));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.gotoEnd(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            setEnabled(cfg.hasText);
            return cfg.hasText;
        }

    }

    /**
     * PasteAction
     */
    protected static class PasteAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -7609111337852520512L;

        public PasteAction(WritableComponent component) {
            super(component, EComponentPopupMenu.PASTE, Resources.getImageResource("page_paste.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.paste(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            setEnabled(true);
            return true;
        }
    }

    /**
     * adapter for {@link PopupMenuListener}
     */
    public static class PopupMenuAdapter implements PopupMenuListener {
        /**
         * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
         */
        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            //
        }

        /**
         * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
         */
        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            //
        }

        /**
         * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
         */
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            //
        }
    }

    /**
     * JDOC
     */
    public static interface ReadableComponent extends HasParentComponent {
        public void copy(ActionEvent e);
    }

    public static interface ReadableTextComponent extends ReadableComponent {
        public void find(ActionEvent e);

        public void findNext(ActionEvent e);
    }

    /**
     * RedoAction
     */
    protected static class RedoAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 700221902961828425L;

        private final UndoManager undoManager;

        public RedoAction(WritableComponent component, UndoManager manager) {
            super(component, EComponentPopupMenu.REDO, Resources.getImageResource("arrow_redo.png"));
            undoManager = manager;
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.redo();
            } catch (CannotRedoException cre) {
                //
            }
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            boolean canRedo = undoManager.canRedo();
            setEnabled(canRedo);
            return canRedo;
        }
    }

    /**
     * ReplaceAction
     */
    protected static class ReplaceAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 4328082010034890480L;

        public ReplaceAction(WritableComponent component) {
            super(component, EComponentPopupMenu.REPLACE, Resources.getImageResource("text_replace.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.replace(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            setEnabled(cfg.hasText);
            return cfg.hasText;
        }
    }

    /**
     * SelectAllAction
     */
    protected static class SelectAllAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -6873629703224034266L;

        public SelectAllAction(WritableComponent component) {
            super(component, EComponentPopupMenu.SELECT_ALL, Resources.getImageResource("page_white_text_width.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.selectAll(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            setEnabled(cfg.hasText);
            return cfg.hasText;
        }
    }

    /**
     * JDOC
     */
    public static class TextComponentWritableComponent implements WritableComponent {
        protected final JTextComponent parentComponent;

        public TextComponentWritableComponent(JTextComponent parentComponent) {
            this.parentComponent = parentComponent;
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#addUndoableEditListener(javax.swing.undo.UndoManager)
         */
        @Override
        public void addUndoableEditListener(UndoManager manager) {
            parentComponent.getDocument().addUndoableEditListener(manager);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
         */
        @Override
        public void copy(ActionEvent e) {
            if (hasSelection()) {
                parentComponent.copy();
            } else {
                int caretPosition = parentComponent.getCaretPosition();
                selectAll(e);
                parentComponent.copy();
                unselect(e);
                parentComponent.setCaretPosition(caretPosition);
            }
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#cut()
         */
        @Override
        public void cut(ActionEvent e) {
            parentComponent.cut();
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#delete(java.awt.event.ActionEvent)
         */
        @Override
        public void delete(ActionEvent e) {
            parentComponent.replaceSelection(null);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#find(java.awt.event.ActionEvent)
         */
        @Override
        public void find(ActionEvent e) {
            if (parentComponent instanceof ETextArea) {
                SearchDialog searchDialog = new SearchDialog(false, ETextArea.class.cast(parentComponent));
                searchDialog.setVisible(true);
                searchDialog.updateFocus();
            }
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#findNext(java.awt.event.ActionEvent)
         */
        @Override
        public void findNext(ActionEvent e) {
            if (parentComponent instanceof ETextArea) {
                ETextArea.class.cast(parentComponent).findNext();
            }
        }

        /**
         * @see org.swingeasy.HasParentComponent#getParentComponent()
         */
        @Override
        public JComponent getParentComponent() {
            return parentComponent;
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#gotoBegin(java.awt.event.ActionEvent)
         */
        @Override
        public void gotoBegin(ActionEvent e) {
            setCaret(0, null);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#gotoEnd(java.awt.event.ActionEvent)
         */
        @Override
        public void gotoEnd(ActionEvent e) {
            setCaret(parentComponent.getDocument().getLength(), null);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#hasSelection()
         */
        @Override
        public boolean hasSelection() {
            try {
                return parentComponent.getSelectedText() != null;
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#hasText()
         */
        @Override
        public boolean hasText() {
            return parentComponent.getDocument().getLength() > 0;
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#isEditable()
         */
        @Override
        public boolean isEditable() {
            return parentComponent.isEditable();
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#paste(java.awt.event.ActionEvent)
         */
        @Override
        public void paste(ActionEvent e) {
            parentComponent.paste();
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#replace(java.awt.event.ActionEvent)
         */
        @Override
        public void replace(ActionEvent e) {
            if (parentComponent instanceof ETextArea) {
                SearchDialog searchDialog = new SearchDialog(true, ETextArea.class.cast(parentComponent));
                searchDialog.setVisible(true);
                searchDialog.updateFocus();
            }
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#selectAll(java.awt.event.ActionEvent)
         */
        @Override
        public void selectAll(ActionEvent e) {
            setCaret(0, parentComponent.getDocument().getLength());
        }

        protected void setCaret(int pos, Integer to) {
            if (parentComponent instanceof ETextComponentI) {
                if (to == null) {
                    ETextComponentI.class.cast(parentComponent).setCaret(pos);
                } else {
                    ETextComponentI.class.cast(parentComponent).setCaret(pos, to);
                }
            } else {
                parentComponent.setCaretPosition(pos);
                if (to != null) {
                    parentComponent.moveCaretPosition(to);
                }
            }
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#unselect(java.awt.event.ActionEvent)
         */
        @Override
        public void unselect(ActionEvent e) {
            setCaret(parentComponent.getCaretPosition(), null);
        }
    }

    /**
     * UndoAction
     */
    protected static class UndoAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -2639363038955484287L;

        private final UndoManager undoManager;

        public UndoAction(WritableComponent component, UndoManager manager) {
            super(component, EComponentPopupMenu.UNDO, Resources.getImageResource("arrow_undo.png"));
            undoManager = manager;
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.undo();
            } catch (CannotUndoException cue) {
                //
            }
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            boolean canUndo = undoManager.canUndo();
            setEnabled(canUndo);
            return canUndo;
        }
    }

    /**
     * UnselectAction
     */
    protected static class UnselectAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -736429406339064829L;

        public UnselectAction(WritableComponent component) {
            super(component, EComponentPopupMenu.UNSELECT, Resources.getImageResource("page_white_width.png"));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            delegate.unselect(e);
        }

        /**
         * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
         */
        @Override
        public boolean checkEnabled(CheckEnabled cfg) {
            setEnabled(cfg.hasSelection);
            return cfg.hasSelection;
        }
    }

    /**
     * JDOC
     */
    public static interface WritableComponent extends ReadableTextComponent {
        public void addUndoableEditListener(UndoManager manager);

        public void cut(ActionEvent e);

        public void delete(ActionEvent e);

        public void gotoBegin(ActionEvent e);

        public void gotoEnd(ActionEvent e);

        public boolean hasSelection();

        public boolean hasText();

        public boolean isEditable();

        public void paste(ActionEvent e);

        public void replace(ActionEvent e);

        public void selectAll(ActionEvent e);

        public void unselect(ActionEvent e);
    }

    public static boolean accelerate(Action action) {
        if (action instanceof HasParentComponent) {
            KeyStroke acceleratorKey = KeyStroke.class.cast(action.getValue(Action.ACCELERATOR_KEY));
            if (acceleratorKey != null) {
                JComponent component1 = HasParentComponent.class.cast(action).getParentComponent();
                if (component1 != null) {
                    String actionCommandKey = String.valueOf(action.getValue(Action.ACTION_COMMAND_KEY));
                    return EComponentPopupMenu.accelerate(component1, action, acceleratorKey, actionCommandKey);
                }
            }
        }
        return false;
    }

    public static boolean accelerate(JComponent parentComponent, Action action) {
        KeyStroke acceleratorKey = KeyStroke.class.cast(action.getValue(Action.ACCELERATOR_KEY));
        if (acceleratorKey != null) {
            String actionCommandKey = String.valueOf(action.getValue(Action.ACTION_COMMAND_KEY));
            return EComponentPopupMenu.accelerate(parentComponent, action, acceleratorKey, actionCommandKey);
        }
        return false;
    }

    public static boolean accelerate(JComponent parentComponent, Action action, KeyStroke acceleratorKey, String actionCommandKey) {
        // System.out.println(this.component.getClass().getName() + " :: " + acceleratorKey + " :: " + actionCommandKey);
        parentComponent.getActionMap().put(actionCommandKey, action);

        for (int condition : EComponentPopupMenu.conditions) {
            InputMap inputMap = parentComponent.getInputMap(condition);
            if (inputMap.get(acceleratorKey) != null) {
                EComponentPopupMenu.removeRegisteredKeystroke(parentComponent, acceleratorKey);
            }
            inputMap.put(acceleratorKey, actionCommandKey);
        }

        return true;
    }

    /**
     * copy to clipboard
     */
    public static void copyToClipboard(String content) {
        SystemSettings.getClipboard().setContents(new StringSelection(content), new ClipboardOwnerAdapter());
    }

    /**
     * debug actions
     */
    public static void debug(JComponent component) {
        // List keystrokes in the WHEN_FOCUSED input map of the component
        InputMap map = component.getInputMap(JComponent.WHEN_FOCUSED);
        EComponentPopupMenu.list(map, map.keys());
        // List keystrokes in the component and in all parent input maps
        EComponentPopupMenu.list(map, map.allKeys());

        // List keystrokes in the WHEN_ANCESTOR_OF_FOCUSED_COMPONENT input map of the component
        map = component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        EComponentPopupMenu.list(map, map.keys());
        // List keystrokes in all related input maps
        EComponentPopupMenu.list(map, map.allKeys());

        // List keystrokes in the WHEN_IN_FOCUSED_WINDOW input map of the component
        map = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        EComponentPopupMenu.list(map, map.keys());
        // List keystrokes in all related input maps
        EComponentPopupMenu.list(map, map.allKeys());
    }

    /**
     * JDOC
     */
    public static EComponentPopupMenu installPopupMenu(final ReadableComponent component) {
        final EComponentPopupMenuAction<ReadableComponent> copyAction = new CopyAction(component);
        final EComponentPopupMenu popup = new EComponentPopupMenu(component, null);
        popup.add(copyAction);
        if (component instanceof ReadableTextComponent) {
            final EComponentPopupMenuAction<ReadableTextComponent> findAction = new FindAction(ReadableTextComponent.class.cast(component));
            final EComponentPopupMenuAction<ReadableTextComponent> findNextAction = new FindNextAction(ReadableTextComponent.class.cast(component));
            popup.add(findAction);
            popup.add(findNextAction);
        }
        component.getParentComponent().addAncestorListener(new AncestorAdapter() {
            @Override
            public void ancestorAdded(AncestorEvent e) {
                component.getParentComponent().requestFocusInWindow();
            }
        });
        popup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                if (component instanceof PopupMenuListener) {
                    PopupMenuListener.class.cast(component).popupMenuCanceled(e);
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                if (component instanceof PopupMenuListener) {
                    PopupMenuListener.class.cast(component).popupMenuWillBecomeInvisible(e);
                }
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if (component instanceof PopupMenuListener) {
                    PopupMenuListener.class.cast(component).popupMenuWillBecomeVisible(e);
                }
            }
        });
        component.getParentComponent().setComponentPopupMenu(popup);
        return popup;
    }

    /**
     * JDOC
     */
    public static EComponentPopupMenu installPopupMenu(final WritableComponent component) {
        if (!component.isEditable()) {
            return EComponentPopupMenu.installReadOnlyTextComponentPopupMenu((JTextComponent) component.getParentComponent());
        }

        final UndoManager undoRedoManager = new UndoManager();

        final EComponentPopupMenuAction<ReadableComponent> copyAction = new CopyAction(component);
        final EComponentPopupMenuAction<WritableComponent> undoAction = new UndoAction(component, undoRedoManager);
        final EComponentPopupMenuAction<WritableComponent> redoAction = new RedoAction(component, undoRedoManager);
        final EComponentPopupMenuAction<WritableComponent> cutAction = new CutAction(component);
        final EComponentPopupMenuAction<WritableComponent> pasteAction = new PasteAction(component);
        // FIXME delete action
        // final EComponentPopupMenuAction<WritableComponent> deleteAction = new DeleteAction(component);
        final EComponentPopupMenuAction<WritableComponent> selectAllAction = new SelectAllAction(component);
        final EComponentPopupMenuAction<WritableComponent> unselectAction = new UnselectAction(component);
        final EComponentPopupMenuAction<WritableComponent> deleteAllAction = new DeleteAllAction(component);
        final EComponentPopupMenuAction<WritableComponent> gotoBeginAction = new GotoBeginAction(component);
        final EComponentPopupMenuAction<WritableComponent> gotoEndAction = new GotoEndAction(component);
        final EComponentPopupMenuAction<ReadableTextComponent> findAction = new FindAction(component);
        final EComponentPopupMenuAction<ReadableTextComponent> findNextAction = new FindNextAction(component);
        final EComponentPopupMenuAction<WritableComponent> replaceAction = new ReplaceAction(component);

        final JComponent parentComponent = component.getParentComponent();

        parentComponent.addAncestorListener(new AncestorAdapter() {
            @Override
            public void ancestorAdded(AncestorEvent e) {
                undoRedoManager.discardAllEdits();
                parentComponent.requestFocusInWindow();
            }
        });

        component.addUndoableEditListener(undoRedoManager);

        final EComponentPopupMenu popup = new EComponentPopupMenu(component, undoRedoManager);

        popup.add(undoAction);
        popup.add(redoAction);
        popup.addSeparator();
        popup.add(cutAction);
        popup.add(copyAction);
        popup.add(pasteAction);
        // popup.add(deleteAction);
        popup.addSeparator();
        popup.add(selectAllAction);
        popup.add(unselectAction);
        popup.add(deleteAllAction);
        popup.add(gotoBeginAction);
        popup.add(gotoEndAction);

        if (parentComponent instanceof ETextArea) {
            popup.addSeparator();
            popup.add(findAction);
            popup.add(findNextAction);
            popup.add(replaceAction);
        }

        if (parentComponent instanceof JTextComponent) {
            JTextComponent textcomponent = JTextComponent.class.cast(parentComponent);
            textcomponent.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    popup.checkEnabled();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    popup.checkEnabled();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    popup.checkEnabled();
                }
            });
            textcomponent.addCaretListener(e -> popup.checkEnabled());
        }

        parentComponent.setComponentPopupMenu(popup);

        popup.checkEnabled();

        return popup;
    }

    /**
     * use this method to add the popup menu to a textcomponent which is never editable
     */
    public static EComponentPopupMenu installReadOnlyTextComponentPopupMenu(final JTextComponent component) {
        return EComponentPopupMenu.installPopupMenu((ReadableComponent) new TextComponentWritableComponent(component));
    }

    /**
     * use this method to add the popup menu to a textcomponent which can be or is always editable
     */
    public static EComponentPopupMenu installTextComponentPopupMenu(final JTextComponent component) {
        return EComponentPopupMenu.installPopupMenu(new TextComponentWritableComponent(component));
    }

    public static String keyStroke2String(KeyStroke key) {
        if (key == null) {
            return "";
        }
        if (key.getModifiers() == 0) {
            return KeyEvent.getKeyText(key.getKeyCode());
        }
        return InputEvent.getModifiersExText(key.getModifiers()) + "+" + KeyEvent.getKeyText(key.getKeyCode());
    }

    protected static void list(InputMap map, KeyStroke[] keys) {
        if (keys == null) {
            return;
        }
        for (KeyStroke key : keys) {
            // This method is defined in Converting a KeyStroke to a String
            String keystrokeStr = EComponentPopupMenu.keyStroke2String(key);
            System.out.println(keystrokeStr);
            // Get the action name bound to this keystroke
            while (map.get(key) == null) {
                map = map.getParent();
            }
            if (map.get(key) instanceof String) {
                String actionName = (String) map.get(key);
                System.out.println("\t" + actionName);
            } else {
                Action action = (Action) map.get(key);
                System.out.println("\t" + action);
            }
        }
    }

    /**
     * get text data on clipboard or null
     */
    public static String pasteFromClipboard() {
        String result = "";
        // odd: the Object param of getContents is not currently used
        Transferable contents = SystemSettings.getClipboard().getContents(null);
        boolean hasTransferableText = contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                if (contents != null) {
                    result = (String) contents.getTransferData(DataFlavor.stringFlavor);
                }
            } catch (UnsupportedFlavorException ex) {
                // highly unlikely since we are using a standard DataFlavor
                System.out.println(ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void removeAllRegisteredKeystroke(JComponent component) {
        // removes all key actions
        // like F6 toggleFocus
        // like F8 startResize
        for (int condition : EComponentPopupMenu.conditions) {
            component.setInputMap(condition, condition != JComponent.WHEN_IN_FOCUSED_WINDOW ? new InputMap() : new ComponentInputMap(component));
            component.setActionMap(new ActionMap());
        }
    }

    public static void removeRegisteredKeystroke(JComponent component, KeyStroke remove) {
        for (int condition : EComponentPopupMenu.conditions) {
            InputMap im = component.getInputMap(condition);
            if (im.get(remove) != null) {
                im.put(remove, "none");
            }
        }
    }

    private static final long serialVersionUID = 8362926178135321789L;

    public static final int[] conditions = { JComponent.WHEN_FOCUSED, JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT };

    public static final String DELETE = "delete-next";

    public static final String REDO = "redo";

    public static final String UNDO = "undo";

    public static final String CUT = "cut-tx";

    public static final String COPY = "copy-tx";

    public static final String PASTE = "paste-tx";

    public static final String SELECT_ALL = "select-all";

    public static final String DELETE_ALL = "delete-all";

    public static final String GOTO_BEGIN = "goto-begin";

    public static final String GOTO_END = "goto-end";

    public static final String UNSELECT = "unselect";

    public static final String FIND = "find";

    public static final String REPLACE = "replace";

    public static final String FIND_NEXT = "find-next";

    protected int cpx = 0;

    protected int cpy = 0;

    protected ReadableComponent component;

    protected UndoManager undoRedoManager;

    /**
     * hidden contructor
     */
    protected EComponentPopupMenu(ReadableComponent component, UndoManager undoRedoManager) {
        this.component = component;
        this.undoRedoManager = undoRedoManager;
        UIUtils.registerLocaleChangeListener((EComponentI) this);
    }

    /**
     * let action implement {@link EComponentI} to listen to {@link Locale} changes and {@link HasParentComponent} to accelerate {@link Action}s for the {@link JComponent}
     */
    @Override
    public JMenuItem add(Action action) {
        this.setLocale(action);
        JMenuItem mi = super.add(action);
        EComponentPopupMenu.accelerate(action);
        return mi;
    }

    public void checkEnabled() {
        if (component instanceof WritableComponent) {
            WritableComponent writableComponent = WritableComponent.class.cast(component);
            CheckEnabled cfg = new CheckEnabled(writableComponent.hasSelection(), writableComponent.hasText(), undoRedoManager == null ? false : undoRedoManager.canUndo(), undoRedoManager == null ? false : undoRedoManager.canRedo());
            // System.out.println(cfg);
            for (int i = 0; i < getComponentCount(); i++) {
                Component menuItem = this.getComponent(i);
                if (menuItem instanceof JMenuItem) {
                    Action action = JMenuItem.class.cast(menuItem).getAction();
                    if (action instanceof EComponentPopupMenuAction) {
                        EComponentPopupMenuAction.class.cast(action).checkEnabled(cfg);
                        // System.out.println(action.getValue(Action.NAME) + ":" + EComponentPopupMenuAction.class.cast(action).checkEnabled(cfg));
                    }
                }
            }
        }
    }

    protected void setLocale(Action action) {
        if (action instanceof EComponentI) {
            EComponentI.class.cast(action).setLocale(getLocale());
        }
    }

    /**
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        for (int i = 0; i < getComponentCount(); i++) {
            Component component1 = this.getComponent(i);
            if (component1 instanceof JMenuItem) {
                Action action = JMenuItem.class.cast(component1).getAction();
                if (action instanceof EComponentI) {
                    EComponentI.class.cast(action).setLocale(l);
                }
            }
        }
    }

    /**
     * @see javax.swing.JPopupMenu#setLocation(int, int)
     */
    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        cpx = x;
        cpy = y;
    }
}
