package org.swingeasy.colors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jhaws.common.encoding.HexString;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.KeyValue;
import org.swingeasy.EButton;
import org.swingeasy.EButtonConfig;
import org.swingeasy.ETextField;
import org.swingeasy.ETextFieldConfig;
import org.swingeasy.UIUtils;

// https://en.wikipedia.org/wiki/List_of_colors:_A%E2%80%93F
public class ClosestColorname {
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		try {
			FilePath f = new FilePath(ClosestColorname.class, "colors/colornames.txt");
			f.setCharSet(Charset.forName("Cp1252"));
			BufferedReader in = f.newBufferedReader();
			String line;
			in.readLine();
			in.readLine();
			Map<String, int[]> colornames_rgb = new LinkedHashMap<>();
			Map<String, float[]> colornames_hsv = new LinkedHashMap<>();
			while ((line = in.readLine()) != null) {
				String[] parts = line.split("\t");
				String name = parts[0];
				String rgbhex = parts[1];
				rgbhex = rgbhex.substring(1);
				int r = HexString.hexToByte(rgbhex.substring(0, 2));
				int g = HexString.hexToByte(rgbhex.substring(2, 4));
				int b = HexString.hexToByte(rgbhex.substring(4, 6));
				if (r < 0)
					r += 256;
				if (g < 0)
					g += 256;
				if (b < 0)
					b += 256;
				colornames_rgb.put(name, new int[] { r, g, b });
				float[] hsbvals = new float[3];
				Color.RGBtoHSB(r, g, b, hsbvals);
				colornames_hsv.put(name, hsbvals);
			}

			JColorChooser color = new JColorChooser();
			JColorChooser colorclosest = new JColorChooser();
			ETextField colorinput = new ETextField(new ETextFieldConfig());
			ETextField colorclosesttext = new ETextField(new ETextFieldConfig());

			UIUtils.systemLookAndFeel();
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			JTabbedPane tabs = new JTabbedPane();
			JPanel op = new JPanel(new BorderLayout());
			op.add(colorinput, BorderLayout.NORTH);
			op.add(color, BorderLayout.CENTER);
			op.add(new EButton(new EButtonConfig(new AbstractAction("Calc") {
				@Override
				public void actionPerformed(ActionEvent e) {
					String hex = colorinput.getText();
					int r = HexString.hexToByte(hex.substring(0, 2));
					int g = HexString.hexToByte(hex.substring(2, 4));
					int b = HexString.hexToByte(hex.substring(4, 6));
					if (r < 0)
						r += 256;
					if (g < 0)
						g += 256;
					if (b < 0)
						b += 256;
					float[] hsbvals = new float[3];
					Color.RGBtoHSB(r, g, b, hsbvals);
					Color c = new Color(r, g, b);

					List<ColorDiff> diff = new ArrayList<>();
					colornames_hsv.entrySet().forEach(entry -> {
						Float value = (3 * Math.abs(hsbvals[0] - entry.getValue()[0])) + Math.abs(hsbvals[1] - entry.getValue()[1]) + Math.abs(hsbvals[2] - entry.getValue()[2]);
						diff.add(new ColorDiff(entry.getKey(), value));
					});
					Collections.sort(diff);
					String closest = diff.iterator().next().getKey();
					int[] closestrgb = colornames_rgb.get(closest);

					color.setColor(c);
					colorclosesttext.setText(closest);
					colorclosest.setColor(new Color(closestrgb[0], closestrgb[1], closestrgb[2]));
				}
			})), BorderLayout.SOUTH);
			tabs.add("original", op);
			JPanel cp = new JPanel(new BorderLayout());
			cp.add(colorclosest, BorderLayout.CENTER);
			cp.add(colorclosesttext, BorderLayout.SOUTH);
			tabs.add("approximation", cp);
			frame.add(tabs, BorderLayout.CENTER);
			frame.setTitle("");
			frame.setSize(600, 400);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("serial")
	private static class ColorDiff extends KeyValue<String, Float> implements Comparable<ColorDiff> {
		public ColorDiff(String key, Float value) {
			super(key, value);
		}

		@Override
		public int compareTo(ColorDiff o) {
			return new CompareToBuilder().append(getValue(), o.getValue()).toComparison();
		}
	}
}
