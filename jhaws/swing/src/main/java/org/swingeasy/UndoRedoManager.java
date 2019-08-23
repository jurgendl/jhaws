package org.swingeasy;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * UndoRedoManager tool
 *
 * @author Jurgen
 *
 * @see http://www.javaworld.com/javaworld/jw-06-1998/jw-06-undoredo.html
 */
public class UndoRedoManager {
    /** KeyStroke */
    public static final KeyStroke CTRL_Z = KeyStroke.getKeyStroke("control Z");

    /** KeyStroke */
    public static final KeyStroke CTRL_Y = KeyStroke.getKeyStroke("control Y");

    /** String */
    public static final String UNDO = "Undo";

    /** String */
    public static final String REDO = "Redo";

    /** AbstractAction */
    private final AbstractAction redoAction;

    /** AbstractAction */
    private final AbstractAction undoAction;

    /** Document */
    private final Document doc;

    /** JTextComponent */
    private final JTextComponent parent;

    /** UndoManager */
    private final UndoManager manager = new UndoManager();

    /** UndoableEditListener */
    private final UndoableEditListener listener;

    /** field */
    private boolean paused = false;

    /**
     * Creates a new UndoRedoManager object.
     *
     * @param textcomp JTextComponent
     */
    public UndoRedoManager(final JTextComponent textcomp) {
        parent = textcomp;
        doc = textcomp.getDocument();
        listener = evt -> manager.addEdit(evt.getEdit());
        undoAction = new AbstractAction(UndoRedoManager.UNDO) {
            private static final long serialVersionUID = -5353581047689102668L;

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    UndoRedoManager.this.undoEdit();
                } catch (CannotUndoException e) {
                    //
                }
            }
        };
        redoAction = new AbstractAction(UndoRedoManager.REDO) {
            private static final long serialVersionUID = -1207023126325216865L;

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    UndoRedoManager.this.redoEdit();
                } catch (CannotRedoException e) {
                    //
                }
            }
        };

        parent.getActionMap().put(UndoRedoManager.UNDO, undoAction);
        parent.getActionMap().put(UndoRedoManager.REDO, redoAction);
        parent.getInputMap().put(UndoRedoManager.CTRL_Z, UndoRedoManager.UNDO);
        parent.getInputMap().put(UndoRedoManager.CTRL_Y, UndoRedoManager.REDO);
        doc.addUndoableEditListener(listener);
    }

    /**
     *
     * @see util.swing.UndoRedoAble#addEdit(javax.swing.undo.UndoableEdit)
     */
    public void addEdit(UndoableEdit event) {
        manager.addEdit(event);
    }

    /**
     *
     * @see util.swing.UndoRedoAble#canRedo()
     */
    public boolean canRedo() {
        return manager.canRedo();
    }

    /**
     *
     * @see util.swing.UndoRedoAble#canUndo()
     */
    public boolean canUndo() {
        return manager.canUndo();
    }

    /**
     *
     * @see util.swing.UndoRedoAble#clearEdits()
     */
    public void clearEdits() {
        manager.discardAllEdits();
    }

    /**
     * gets redoAction
     *
     * @return Returns the redoAction.
     */
    protected AbstractAction getRedoAction() {
        return redoAction;
    }

    /**
     *
     * @see util.swing.UndoRedoAble#getRedoString()
     */
    public String getRedoString() {
        return manager.getRedoPresentationName();
    }

    /**
     * gets undoAction
     *
     * @return Returns the undoAction.
     */
    protected AbstractAction getUndoAction() {
        return undoAction;
    }

    /**
     *
     * @see util.swing.UndoRedoAble#getUndoString()
     */
    public String getUndoString() {
        return manager.getUndoPresentationName();
    }

    /**
     *
     * @see util.swing.UndoRedoAble#isPaused()
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     *
     * @see util.swing.UndoRedoAble#redoEdit()
     */
    public boolean redoEdit() throws CannotRedoException {
        if (manager.canRedo()) {
            manager.redo();

            return true;
        }

        return false;
    }

    /**
     *
     * @see util.swing.UndoRedoAble#undoEdit()
     */
    public boolean undoEdit() throws CannotUndoException {
        if (manager.canUndo()) {
            manager.undo();

            return true;
        }

        return false;
    }

    /**
     *
     * @see util.swing.UndoRedoAble#undoRedoPause()
     */
    public void undoRedoPause() {
        parent.getInputMap().remove(UndoRedoManager.CTRL_Z);
        parent.getInputMap().remove(UndoRedoManager.CTRL_Y);
        doc.removeUndoableEditListener(listener);
        paused = true;
    }

    /**
     *
     * @see util.swing.UndoRedoAble#undoRedoResume()
     */
    public void undoRedoResume() {
        parent.getInputMap().put(UndoRedoManager.CTRL_Z, UndoRedoManager.UNDO);
        parent.getInputMap().put(UndoRedoManager.CTRL_Y, UndoRedoManager.REDO);
        doc.addUndoableEditListener(listener);
        paused = false;
    }
}
