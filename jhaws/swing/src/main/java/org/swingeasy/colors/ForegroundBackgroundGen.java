package org.swingeasy.colors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;

import org.jhaws.common.io.media.images.HexString;
import org.swingeasy.ELabel;
import org.swingeasy.ETextField;
import org.swingeasy.ETextFieldConfig;
import org.swingeasy.UIUtils;

import net.miginfocom.swing.MigLayout;

// view-source:https://snook.ca/technical/colour_contrast/colour.html#fg=0A0A0A,bg=88CCEE
public class ForegroundBackgroundGen {

	public static void main(String[] args) {
		new ForegroundBackgroundGen();
	}

	static double getLuminance(double[] rgb) {
		for (int i = 0; i < rgb.length; i++) {
			if (rgb[i] <= 0.03928) {
				rgb[i] = rgb[i] / 12.92;
			} else {
				rgb[i] = Math.pow(((rgb[i] + 0.055) / 1.055), 2.4);
			}
		}
		double l = (0.2126 * rgb[0]) + (0.7152 * rgb[1]) + (0.0722 * rgb[2]);
		return l;
	}

	ETextField bg;

	ELabel fg;

	ELabel bdiff;

	ELabel cdiff;

	ELabel compl;

	ELabel cr;

	ELabel aa;

	ELabel aa18;

	ELabel aaa;

	ELabel aaa18;

	JSlider slider;

	JLabel paint;

	public static class FBGReport {

		public boolean compliancy;

		public double colorDifference;

		public double brightnessDifference;

		public double ratio;

		public boolean aa;

		public boolean aa18;

		public boolean aaa;

		public boolean aaa18;

		public boolean success() {
			return compliancy && aa && aa18 && aaa && aaa18;
		}

		public int successes() {
			return (int) Arrays.stream(new Boolean[] { compliancy, aa, aa18, aaa, aaa18 }).filter(b -> b).count();
		}
	}

	ForegroundBackgroundGen() {
		UIUtils.systemLookAndFeel();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel(new MigLayout("", "[][grow]", ""));
		panel.setFont(panel.getFont().deriveFont(18f));
		frame.add(panel, BorderLayout.CENTER);
		frame.setTitle("");

		panel.add(new ELabel("Background Color: #"));
		this.bg = new ETextField(new ETextFieldConfig("FFFFFF"));
		panel.add(bg, "growx, growy, wrap");

		panel.add(new ELabel("Foreground Color: #"));
		this.fg = new ELabel("000000");
		panel.add(fg, "growx, growy, wrap");

		panel.add(new ELabel(""));
		this.paint = new JLabel("Example");
		paint.setOpaque(true);
		paint.setBackground(Color.white);
		panel.add(paint, "growx, growy, wrap");

		panel.add(new ELabel("Background Value"));
		this.slider = new JSlider(0, 100, 0);
		slider.addChangeListener(e -> {
        	FBGReport report = new FBGReport();
        	{
        		int brightness = slider.getValue();
        		int color = Color.HSBtoRGB(0f, 0f, brightness / 100.0f);
        		Color c = new Color(color);
        		String hex = String.format("%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
        		fg.setText(hex);
        	}
        	{
        		String rgbhexbg = bg.getText();
        		String rgbhexfg = fg.getText();

        		int br = HexString.hexToByte(rgbhexbg.substring(0, 2));
        		@SuppressWarnings("hiding")
        		int bg = HexString.hexToByte(rgbhexbg.substring(2, 4));
        		int bb = HexString.hexToByte(rgbhexbg.substring(4, 6));
        		if (br < 0)
        			br += 256;
        		if (bg < 0)
        			bg += 256;
        		if (bb < 0)
        			bb += 256;

        		int fr = HexString.hexToByte(rgbhexfg.substring(0, 2));
        		@SuppressWarnings("hiding")
        		int fg = HexString.hexToByte(rgbhexfg.substring(2, 4));
        		int fb = HexString.hexToByte(rgbhexfg.substring(4, 6));
        		if (fr < 0)
        			fr += 256;
        		if (fg < 0)
        			fg += 256;
        		if (fb < 0)
        			fb += 256;

        		paint.setForeground(new Color(fr, fg, fb));
        		paint.setBackground(new Color(br, bg, bb));

        		double brightnessThreshold = 125.0;
        		double colorThreshold = 500.0;

        		double bY = ((br * 299.0) + (bg * 587.0) + (bb * 114.0)) / 1000.0;
        		double fY = ((fr * 299.0) + (fg * 587.0) + (fb * 114.0)) / 1000.0;

        		double brightnessDifference = Math.abs(bY - fY);
        		double colorDifference = (Math.max(fr, br) - Math.min(fr, br)) + (Math.max(fg, bg) - Math.min(fg, bg)) + (Math.max(fb, bb) - Math.min(fb, bb));

        		report.brightnessDifference = brightnessDifference;
        		bdiff.setText("" + brightnessDifference);
        		report.colorDifference = colorDifference;
        		cdiff.setText("" + colorDifference);

        		if ((brightnessDifference >= brightnessThreshold) && (colorDifference >= colorThreshold)) {
        			compl.setText("compliant");
        			report.compliancy = true;
        		} else if ((brightnessDifference >= brightnessThreshold) || (colorDifference >= colorThreshold)) {
        			compl.setText("sort of compliant");
        			report.compliancy = true;
        		} else {
        			compl.setText("NOT compliant"); // not compliant "Poor visibility between text and background colors."
        			report.compliancy = false;
        		}

        		// perform math for WCAG2
        		double ratio = 1;
        		double l1 = getLuminance(new double[] { fr / 255.0, fg / 255.0, fb / 255.0 });
        		double l2 = getLuminance(new double[] { br / 255.0, bg / 255.0, bb / 255.0 });
        		if (l1 >= l2) {
        			ratio = (l1 + .05) / (l2 + .05);
        		} else {
        			ratio = (l2 + .05) / (l1 + .05);
        		}
        		ratio = Math.floor(ratio * 1000.0) / 1000.0; // round to 3 decimal places

        		cr.setText("" + ratio);
        		report.ratio = ratio;
        		aa.setText((ratio >= 4.5 ? "YES" : "NO"));
        		report.aa = (ratio >= 4.5 ? true : false);
        		aa18.setText((ratio >= 3 ? "YES" : "NO"));
        		report.aa18 = (ratio >= 3 ? true : false);
        		aaa.setText((ratio >= 7 ? "YES" : "NO"));
        		report.aaa = (ratio >= 7 ? true : false);
        		aaa18.setText((ratio >= 4.5 ? "YES" : "NO"));
        		report.aaa18 = (ratio >= 4.5 ? true : false);
        	}
        });
		panel.add(slider, "growx, growy, wrap");

		panel.add(new ELabel("Brightness Difference: (>= 125):"));
		this.bdiff = new ELabel("");
		panel.add(bdiff, "growx, growy, wrap");

		panel.add(new ELabel("Colour Difference: (>= 500):"));
		this.cdiff = new ELabel("");
		panel.add(cdiff, "growx, growy, wrap");

		panel.add(new ELabel("Are colours compliant?"));
		this.compl = new ELabel("");
		panel.add(compl, "growx, growy, wrap");

		panel.add(new ELabel("Contrast Ratio:"));
		this.cr = new ELabel("");
		panel.add(cr, "growx, growy, wrap");

		panel.add(new ELabel("WCAG 2 AA Compliant:"));
		this.aa = new ELabel("");
		panel.add(aa, "growx, growy, wrap");

		panel.add(new ELabel("WCAG 2 AA Compliant (18pt+):"));
		this.aa18 = new ELabel("");
		panel.add(aa18, "growx, growy, wrap");

		panel.add(new ELabel("WCAG 2 AAA Compliant:"));
		this.aaa = new ELabel("");
		panel.add(aaa, "growx, growy, wrap");

		panel.add(new ELabel("WCAG 2 AAA Compliant (18pt+):"));
		this.aaa18 = new ELabel("");
		panel.add(aaa18, "growx, growy, wrap");

		for (int i = 0; i < panel.getComponentCount(); i++) {
			Component component = panel.getComponent(i);
			if (component instanceof JTextComponent) {
				component.setFont(new Font("Monospaced", Font.PLAIN, 18));
			} else {
				component.setFont(component.getFont().deriveFont(18f));
			}
		}

		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
