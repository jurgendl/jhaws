package org.swingeasy;

import java.awt.Container;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.text.MaskFormatter;

import org.swingeasy.formatters.DateFormatBuilder;
import org.swingeasy.formatters.IPAddressFormatter;
import org.swingeasy.formatters.NumberFormatBuilder;
import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class FormattedFieldDemo {
    private static void addComponents(Container container) throws ParseException, MalformedURLException {
        container.setLayout(new GridLayout(-1, 2));

        final EButtonGroup localegroup = new EButtonGroup();
        ERadioButton en = new ERadioButton(new ERadioButtonConfig("en"));//$NON-NLS-1$
        container.add(en);
        localegroup.add(en);
        ERadioButton nl = new ERadioButton(new ERadioButtonConfig("nl"));//$NON-NLS-1$
        container.add(nl);
        localegroup.add(nl);
        localegroup.addPropertyChangeListener(EButtonGroup.SELECTION, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Locale l = new Locale(String.valueOf(evt.getNewValue()));
                SystemSettings.setCurrentLocale(l);
            }
        });

        container.add(new ELabel(""));
        container.add(new EFormattedTextField<Double>(new EFormattedTextFieldConfig(new NumberFormatBuilder(NumberFormatBuilder.Type.Default)),
                1234.5678));
        container.add(new ELabel("currency"));
        container.add(new EFormattedTextField<Double>(new EFormattedTextFieldConfig(new NumberFormatBuilder(NumberFormatBuilder.Type.Currency)),
                1234.5678));
        container.add(new ELabel("integer"));
        container
                .add(new EFormattedTextField<Integer>(new EFormattedTextFieldConfig(new NumberFormatBuilder(NumberFormatBuilder.Type.Integer)), 1234));
        container.add(new ELabel("number"));
        container.add(new EFormattedTextField<Double>(new EFormattedTextFieldConfig(new NumberFormatBuilder(NumberFormatBuilder.Type.Number)),
                1234.5678));
        container.add(new ELabel("percent"));
        container.add(new EFormattedTextField<Double>(new EFormattedTextFieldConfig(new NumberFormatBuilder(NumberFormatBuilder.Type.Percentage)),
                0.12345678));
        container.add(new ELabel("date"));
        container.add(new EFormattedTextField<Date>(new EFormattedTextFieldConfig(new DateFormatBuilder(DateFormatBuilder.Type.Date,
                DateFormatBuilder.Length.Default)), new Date()));
        container.add(new ELabel("time"));
        container.add(new EFormattedTextField<Date>(new EFormattedTextFieldConfig(new DateFormatBuilder(DateFormatBuilder.Type.Time,
                DateFormatBuilder.Length.Default)), new Date()));
        container.add(new ELabel("date/time"));
        container.add(new EFormattedTextField<Date>(new EFormattedTextFieldConfig(new DateFormatBuilder(DateFormatBuilder.Type.Both,
                DateFormatBuilder.Length.Default)), new Date()));
        container.add(new ELabel("mask"));
        container.add(new EFormattedTextField<String>(new EFormattedTextFieldConfig(new MaskFormatter("(###) ###-###")), "(032) 111-222"));
        // container.add(new ELabel("url"));
        // container.add(new EFormattedTextField<URL>(new EFormattedTextFieldConfig((DefaultFormatter) null)), new URL("http://www.google.com"));
        container.add(new ELabel("ip4"));
        container
                .add(new EFormattedTextField<byte[]>(new EFormattedTextFieldConfig(new IPAddressFormatter()), new byte[] { (byte) 130, 65, 86, 66 }));
        container.add(new ELabel("ip6"));
        container.add(new EFormattedTextField<byte[]>(new EFormattedTextFieldConfig(new IPAddressFormatter(true)), new byte[] {
                (byte) 130,
                65,
                86,
                66,
                2,
                3 }));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        try {
            FormattedFieldDemo.addComponents(frame.getContentPane());
        } catch (Exception ex) {
            //
        }
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle("FormattedFieldDemo");
        frame.setVisible(true);
    }
}
