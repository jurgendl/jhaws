package org.swingeasy;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * see {@link CustomizableOptionPane#showCustomDialog(Component, JComponent, String, MessageType, OptionType, Icon, OptionPaneCustomizer)}
 * 
 * @author Jurgen
 */
public class CustomizableOptionPane {
    private static class CustomizableFileChooserImpl extends JFileChooser {
        private static final long serialVersionUID = -2847691366004582199L;

        FileChooserCustomizer customizer;

        private int getReturnValue() {
            return new ObjectWrapper(this).get(Integer.class, "returnValue");
        }

        private void setDialog(JDialog dialog) {
            new ObjectWrapper(this).set("dialog", dialog);
        }

        private void setReturnValue(int rv) {
            new ObjectWrapper(this).set("returnValue", rv);
        }

        @Override
        public int showDialog(Component parent, String approveButtonText) throws HeadlessException {
            if (approveButtonText != null) {
                this.setApproveButtonText(approveButtonText);
                this.setDialogType(JFileChooser.CUSTOM_DIALOG);
            }
            JDialog dialog = this.createDialog(parent);
            this.setDialog(dialog);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    CustomizableFileChooserImpl.this.setReturnValue(JFileChooser.CANCEL_OPTION);
                }
            });
            this.setReturnValue(JFileChooser.ERROR_OPTION);
            this.rescanCurrentDirectory();
            if (this.customizer != null) {
                this.customizer.customize(parent, dialog);
            }
            dialog.setVisible(true);
            this.firePropertyChange("JFileChooserDialogIsClosingProperty", dialog, null);
            dialog.removeAll();
            dialog.dispose();
            dialog = null;
            return this.getReturnValue();
        }

    }

    private static class CustomizableOptionPaneImpl extends JOptionPane {

        private static final long serialVersionUID = 6539025260851538675L;

        private static Method styleFromMessageTypeMethod;

        private static Method createDialogMethod;

        private static int _styleFromMessageType(int messageType) {
            return CustomizableOptionPaneImpl.invoke(CustomizableOptionPaneImpl.getStyleFromMessageTypeMethod(), Integer.class, null, messageType);
        }

        private static Method getStyleFromMessageTypeMethod() {
            // thank you for private methods but no use setting this one anything but private
            if (CustomizableOptionPaneImpl.styleFromMessageTypeMethod == null) {
                try {
                    CustomizableOptionPaneImpl.styleFromMessageTypeMethod = JOptionPane.class.getDeclaredMethod("styleFromMessageType", Integer.TYPE);
                    CustomizableOptionPaneImpl.styleFromMessageTypeMethod.setAccessible(true);
                } catch (SecurityException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return CustomizableOptionPaneImpl.styleFromMessageTypeMethod;
        }

        @SuppressWarnings("unchecked")
        private static <T> T invoke(Method method,  Class<T> returnClass, Object obj, Object... params) {
            try {
                return (T) method.invoke(obj, params);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }

        @SuppressWarnings("unchecked")
        private static <T> T showDialog(Component parentComponent, JComponent component, String title, MessageType messageType,
                OptionType optionType, Icon icon, OptionPaneCustomizer customizer, T[] options, T initialValue) throws HeadlessException {
            CustomizableOptionPaneImpl pane = new CustomizableOptionPaneImpl(component, messageType.code,
                    optionType == null ? OptionType.OK_CANCEL.code : optionType.code, icon, options, initialValue);
            pane.setInitialValue(null);
            pane.setComponentOrientation(((parentComponent == null) ? JOptionPane.getRootFrame() : parentComponent).getComponentOrientation());

            int style = CustomizableOptionPaneImpl._styleFromMessageType(messageType.code);
            JDialog dialog = pane._createDialog(parentComponent, title, style);

            if (customizer != null) {
                customizer.customize(parentComponent, messageType, optionType, pane, dialog);
            }

            pane.selectInitialValue();
            dialog.setVisible(true);
            dialog.dispose();

            Object selectedValue = pane.getValue();

            // options
            if (optionType == null) {
                return (T) selectedValue;
            }

            // no options
            if (selectedValue == null) {
                return (T) ResultType.CLOSED;
            }

            if (selectedValue instanceof Integer) {
                int counter = Integer.class.cast(selectedValue).intValue();
                try {
                    return (T) optionType.getResultType(counter);
                } catch (IllegalArgumentException ex) {
                    return (T) ResultType.CLOSED;
                }
            }

            return (T) ResultType.CLOSED;
        }

        private CustomizableOptionPaneImpl(Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
            super(message, messageType, optionType, icon, options, initialValue);
        }

        private JDialog _createDialog(Component parentComponent, String title, int style) throws HeadlessException {
            return CustomizableOptionPaneImpl.invoke(this.getCreateDialogMethod(), JDialog.class, this, parentComponent, title, style);
        }

        private Method getCreateDialogMethod() {
            // thank you for private methods but no use setting this one anything but private
            try {
                CustomizableOptionPaneImpl.createDialogMethod = JOptionPane.class.getDeclaredMethod("createDialog", Component.class, String.class,
                        Integer.TYPE);
                CustomizableOptionPaneImpl.createDialogMethod.setAccessible(true);
                return CustomizableOptionPaneImpl.createDialogMethod;
            } catch (SecurityException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * show customizable (by {@link OptionPaneCustomizer}) dialog with {@link JComponent} as content, uses enums as parameters and return value
     * 
     * @param parentComponent
     * @param component
     * @param title
     * @param messageType
     * @param optionType
     * @param icon
     * @param customizer
     * 
     * @return
     * 
     * @throws HeadlessException
     */
    public static ResultType showCustomDialog(Component parentComponent, JComponent component, String title, MessageType messageType,
            OptionType optionType, Icon icon, OptionPaneCustomizer customizer) throws HeadlessException {
        return CustomizableOptionPaneImpl.showDialog(parentComponent, component, title, messageType, optionType, icon, customizer, null, null);
    }

    public static <T> T showCustomDialog(Component parentComponent, JComponent component, String title, MessageType messageType, T[] options,
            T initialValue, Icon icon, OptionPaneCustomizer customizer) throws HeadlessException {
        return CustomizableOptionPaneImpl.showDialog(parentComponent, component, title, messageType, null, icon, customizer, options, initialValue);
    }

    public static File showFileChooserDialog(Component parent, FileChooserType type, FileChooserCustomizer customizer) {
        CustomizableFileChooserImpl jfc = new CustomizableFileChooserImpl();
        if (customizer != null) {
            customizer.customize(jfc);
            jfc.customizer = customizer;
        }
        int returnValue = type == FileChooserType.SAVE ? jfc.showSaveDialog(parent) : jfc.showOpenDialog(parent);
        if (JFileChooser.APPROVE_OPTION == returnValue) {
            File exportTo = jfc.getSelectedFile();
            if (exportTo != null) {
                return exportTo;
            }
        }
        return null;
    }
}
