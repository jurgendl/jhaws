package org.swingeasy;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
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
        this.parent = textcomp;
        this.doc = textcomp.getDocument();
        this.listener = new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent evt) {
                UndoRedoManager.this.manager.addEdit(evt.getEdit());
            }
        };
        this.undoAction = new AbstractAction(UndoRedoManager.UNDO) {
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
        this.redoAction = new AbstractAction(UndoRedoManager.REDO) {
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

        this.parent.getActionMap().put(UndoRedoManager.UNDO, this.undoAction);
        this.parent.getActionMap().put(UndoRedoManager.REDO, this.redoAction);
        this.parent.getInputMap().put(UndoRedoManager.CTRL_Z, UndoRedoManager.UNDO);
        this.parent.getInputMap().put(UndoRedoManager.CTRL_Y, UndoRedoManager.REDO);
        this.doc.addUndoableEditListener(this.listener);
    }

    /**
     * 
     * @see util.swing.UndoRedoAble#addEdit(javax.swing.undo.UndoableEdit)
     */
    public void addEdit(UndoableEdit event) {
        this.manager.addEdit(event);
    }

    /**
     * 
     * @see util.swing.UndoRedoAble#canRedo()
     */
    public boolean canRedo() {
        return this.manager.canRedo();
    }

    /**
     * 
     * @see util.swing.UndoRedoAble#canUndo()
     */
    public boolean canUndo() {
        return this.manager.canUndo();
    }

    /**
     * 
     * @see util.swing.UndoRedoAble#clearEdits()
     */
    public void clearEdits() {
        this.manager.discardAllEdits();
    }

    /**
     * gets redoAction
     * 
     * @return Returns the redoAction.
     */
    protected AbstractAction getRedoAction() {
        return this.redoAction;
    }

    /**
     * 
     * @see util.swing.UndoRedoAble#getRedoString()
     */
    public String getRedoString() {
        return this.manager.getRedoPresentationName();
    }

    /**
     * gets undoAction
     * 
     * @return Returns the undoAction.
     */
    protected AbstractAction getUndoAction() {
        return this.undoAction;
    }

    /**
     * 
     * @see util.swing.UndoRedoAble#getUndoString()
     */
    public String getUndoString() {
        return this.manager.getUndoPresentationName();
    }

    /**
     * 
     * @see util.swing.UndoRedoAble#isPaused()
     */
    public boolean isPaused() {
        return this.paused;
    }

    /**
     * 
     * @see util.swing.UndoRedoAble#redoEdit()
     */
    public boolean redoEdit() throws CannotRedoException {
        if (this.manager.canRedo()) {
            this.manager.redo();

            return true;
        }

        return false;
    }

    /**
     * 
     * @see util.swing.UndoRedoAble#undoEdit()
     */
    public boolean undoEdit() throws CannotUndoException {
        if (this.manager.canUndo()) {
            this.manager.undo();

            return true;
        }

        return false;
    }

    /**
     * 
     * @see util.swing.UndoRedoAble#undoRedoPause()
     */
    public void undoRedoPause() {
        this.parent.getInputMap().remove(UndoRedoManager.CTRL_Z);
        this.parent.getInputMap().remove(UndoRedoManager.CTRL_Y);
        this.doc.removeUndoableEditListener(this.listener);
        this.paused = true;
    }

    /**
     * 
     * @see util.swing.UndoRedoAble#undoRedoResume()
     */
    public void undoRedoResume() {
        this.parent.getInputMap().put(UndoRedoManager.CTRL_Z, UndoRedoManager.UNDO);
        this.parent.getInputMap().put(UndoRedoManager.CTRL_Y, UndoRedoManager.REDO);
        this.doc.addUndoableEditListener(this.listener);
        this.paused = false;
    }
}
