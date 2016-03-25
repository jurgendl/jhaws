package org.swingeasy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @author Jurgen
 */
public class SplashDemo {
    public static void main(String[] args) throws Exception {
        BufferedImage logo = new BufferedImage(400, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = logo.createGraphics();
        g2.setColor(Color.lightGray);
        g2.fillRect(0, 0, 400, 200);
        Splash splash = new Splash(logo);
        splash.showSplash();
        splash.setProgress(.0f);
        splash.setText("text");
        for (int i = 0; i <= 10; i++) {
            splash.setProgress(i / 10f);
            if (i == 0) {
                Thread.sleep(5000l);
            } else {
                Thread.sleep(1000l);
            }
        }
    }

}
