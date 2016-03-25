package org.swingeasy;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Jurgen
 */
public abstract class DocumentKeyListener implements DocumentListener, KeyListener {
    public static enum Type {
        INSERT, UPDATE, REMOVE;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        this.update(Type.UPDATE, e);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        this.update(Type.INSERT, e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        this.update(Type.REMOVE, e);
    }

    public abstract void update(Type type, DocumentEvent e);
}
