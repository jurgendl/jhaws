package org.swingeasy;

import java.util.Locale;

import org.swingeasy.system.SystemSettings;

/**
 * @see http://www.excelsior-usa.com/articles/localization.html
 */
public class Wtf {
    public static void main(String[] args) {
        System.out.println("\"windows\".toUpperCase().equals(\"WINDOWS\"): " + SystemSettings.getCurrentLocale() + ": "
                + "windows".toUpperCase().equals("WINDOWS") + " = OK");
        SystemSettings.setCurrentLocale(Locale.UK);
        System.out.println("\"windows\".toUpperCase().equals(\"WINDOWS\"): " + SystemSettings.getCurrentLocale() + ": "
                + "windows".toUpperCase().equals("WINDOWS") + " = OK");
        SystemSettings.setCurrentLocale(new Locale("tr", "TR"));
        System.out.println("\"windows\".toUpperCase().equals(\"WINDOWS\"): " + SystemSettings.getCurrentLocale() + ": "
                + "windows".toUpperCase().equals("WINDOWS") + " = WTF!");
        System.out.println("                         equalsIgnoreCase: " + SystemSettings.getCurrentLocale() + ": "
                + "windows".equalsIgnoreCase("WINDOWS") + " = ouph!!!");
    }
}
