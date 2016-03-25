package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class UIExceptionHandler {
    public static interface Out {
        /**
         * public void println(String line)
         * 
         * @param line
         * @param outType
         */
        public void println(String line, OutType outType);
    }

    public static enum OutType {
        CAUSE, EXCEPTION, STACK;
    }

    /**
     * PrintWriter implementing Out interface
     */
    public static class PrintWriterOutWrapper extends PrintWriter implements Out {
        /**
         * Instantieer een nieuwe PrintWriterOut
         * 
         * @param file
         * @throws FileNotFoundException
         */
        public PrintWriterOutWrapper(File file) throws FileNotFoundException {
            super(file);
        }

        /**
         * Instantieer een nieuwe PrintWriterOut
         * 
         * @param file
         * @param csn
         * @throws FileNotFoundException
         * @throws UnsupportedEncodingException
         */
        public PrintWriterOutWrapper(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
            super(file, csn);
        }

        /**
         * Instantieer een nieuwe PrintWriterOut
         * 
         * @param out
         */
        public PrintWriterOutWrapper(OutputStream out) {
            super(out);
        }

        /**
         * Instantieer een nieuwe PrintWriterOut
         * 
         * @param out
         * @param autoFlush
         */
        public PrintWriterOutWrapper(OutputStream out, boolean autoFlush) {
            super(out, autoFlush);
        }

        /**
         * Instantieer een nieuwe PrintWriterOut
         * 
         * @param fileName
         * @throws FileNotFoundException
         */
        public PrintWriterOutWrapper(String fileName) throws FileNotFoundException {
            super(fileName);
        }

        /**
         * Instantieer een nieuwe PrintWriterOut
         * 
         * @param fileName
         * @param csn
         * @throws FileNotFoundException
         * @throws UnsupportedEncodingException
         */
        public PrintWriterOutWrapper(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
            super(fileName, csn);
        }

        /**
         * Instantieer een nieuwe PrintWriterOut
         * 
         * @param out
         */
        public PrintWriterOutWrapper(Writer out) {
            super(out);
        }

        /**
         * Instantieer een nieuwe PrintWriterOut
         * 
         * @param out
         * @param autoFlush
         */
        public PrintWriterOutWrapper(Writer out, boolean autoFlush) {
            super(out, autoFlush);
        }

        /**
         * 
         * @see be.ugent.komodo.exceptions.ExceptionHandlerUtils.Out#println(java.lang.String,
         *      be.ugent.komodo.exceptions.ExceptionHandlerUtils.OutType)
         */
        @Override
        public void println(String line, OutType outType) {
            this.println(line);
        }
    }

    private static final UIExceptionHandler instance = new UIExceptionHandler();

    public static UIExceptionHandler getInstance() {
        return UIExceptionHandler.instance;
    }

    /**
     * There is an easy an elegant way to convert or store an exception stack trace to a string variable. The following method takes an exception
     * object as a parameter and returns the string representation of the stack trace.
     * 
     * @param ex
     * 
     * @return
     */
    public static String getStackTrace(Throwable ex) {
        java.io.StringWriter out = new java.io.StringWriter();
        UIExceptionHandler.printStackTrace(ex, new PrintWriterOutWrapper(out));

        return out.toString();
    }

    /**
     * internal use
     * 
     * @param t internal use
     * @param out internal use
     * @param supert super throwable
     */
    private static synchronized void print(final Throwable t, final Out out, final Throwable supert) {
        if (t == null) {
            return;
        }

        if (t == supert) {
            out.println(t.toString(), OutType.EXCEPTION);
        } else {
            out.println("Caused by: " + t, OutType.CAUSE); //$NON-NLS-1$
        }

        StackTraceElement[] stackTrace = t.getStackTrace();

        for (StackTraceElement element : stackTrace) {
            out.println("\t " + element, OutType.STACK); //$NON-NLS-1$
        }
    }

    /**
     * 
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    public static synchronized void printStackTrace(final Throwable top, final Out out) {
        Throwable t = top;

        do {
            UIExceptionHandler.print(t, out, top);
            t = t.getCause();
        } while (t != null);
    }

    private UIExceptionHandler() {
        super();
    }

    public void show(Thread thread, Exception ex, String message) {
        System.out.println(thread);
        String stackTrace = (thread == null ? "" : String.valueOf(thread) + SystemSettings.getNewline() + SystemSettings.getNewline())
                + UIExceptionHandler.getStackTrace(ex);
        System.out.println(stackTrace);
        final ETextArea view = new ETextArea(new ETextAreaConfig(), stackTrace);
        view.setEditable(false);
        final JScrollPane jsp = new JScrollPane(view);
        Dimension size = new Dimension(600, 250);
        jsp.setMinimumSize(size);
        jsp.setPreferredSize(size);
        jsp.setSize(size);
        EButton button = new EButton(new EButtonConfig(new EIconButtonCustomizer() {
            @Override
            public void customize(AbstractButton _button) {
                super.customize(_button);
                _button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                _button.setBackground(Color.WHITE);
                _button.setHorizontalAlignment(SwingConstants.LEFT);
            }
        }, "<HTML><FONT color=\"#000099\"><U>" + message + "</U></FONT></HTML>"));
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(button, BorderLayout.CENTER);
        final ValueHolder<JDialog> jdialog = new ValueHolder<JDialog>();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Arrays.asList(panel.getComponents()).contains((jsp))) {
                    panel.remove(jsp);
                } else {
                    panel.add(jsp, BorderLayout.SOUTH);
                }
                jdialog.getValue().pack();
                jdialog.getValue().setLocationRelativeTo(null);
            }
        });
        CustomizableOptionPane.showCustomDialog(null, panel, Messages.getString((Locale) null, "Error"), MessageType.ERROR, OptionType.OK, null,
                new OptionPaneCustomizer() {
                    @Override
                    public void customize(Component parentComponent, MessageType messageType, OptionType optionType, JOptionPane pane, JDialog dialog) {
                        jdialog.setValue(dialog);
                        dialog.pack();
                        dialog.setLocationRelativeTo(null);
                    }
                });
    }
}
