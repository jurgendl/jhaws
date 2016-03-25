package org.swingeasy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.swingeasy.system.SystemSettings;
import org.swingeasy.validation.Translator;

/**
 * @author Jurgen
 */
public class Messages implements Translator {
    public static String getDefaultString(String key, Object... arguments) {
        return Messages.getString((Locale) null, key, arguments);
    }

    public static Messages getInstance() {
        return Messages.instance;
    }

    protected static ResourceBundle getResourceBundle(Locale locale) {
        if (locale == null) {
            locale = SystemSettings.getCurrentLocale();
        }
        ResourceBundle resourceBundle = Messages.RESOURCE_BUNDLES.get(locale);
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle(Messages.BUNDLE_NAME, locale);
        }
        return resourceBundle;
    }

    public static String getString(Locale locale, String key, Object... arguments) {
        if (locale == null) {
            locale = SystemSettings.getCurrentLocale();
        }
        try {
            String translated = Messages.getResourceBundle(locale).getString(key);

            if (translated.startsWith("{") && translated.endsWith("}")) {
                key = translated.substring(1, translated.length() - 1);
                translated = Messages.getString(locale, key, arguments);
            }

            if ((arguments != null) && (arguments.length > 0)) {
                translated = String.format(translated, arguments);
            }

            return translated;
        } catch (MissingResourceException e) {
            System.out.println("missing resource key:" + key);
            return key;
        }
    }

    protected static final String BUNDLE_NAME = "org.swingeasy.resources.swing-easy"; //$NON-NLS-1$

    protected static final Map<Locale, ResourceBundle> RESOURCE_BUNDLES = new HashMap<Locale, ResourceBundle>();

    protected static final Messages instance = new Messages();

    protected Messages() {
        super();
    }

    /**
     *
     * @see org.swingeasy.validation.Translator#getString(java.lang.String, java.lang.Object[])
     */
    @Override
    public String getString(String key, Object... arguments) {
        return Messages.getString(null, key, arguments);
    }
}
