package org.swingeasy;

import java.io.FileInputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

/**
 * @author Jurgen
 */
public class TreeTableDemo {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			UIUtils.systemLookAndFeel();

			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			String[] h = { "key", "value" };

			Collection<ETreeTableRecord<Entry<String, String>>> col = new ArrayList<ETreeTableRecord<Entry<String, String>>>();

			Properties p = new Properties();

			if ((args != null) && (args.length > 0)) {
				for (String arg : args) {
					Properties pp = new Properties();
					try (FileInputStream in = new FileInputStream(arg)) {
						pp.load(in);
						in.close();
					}
					p.putAll(pp);
				}
			} else {
				int i = 1;
				String loc = JOptionPane.showInputDialog("Properties #" + i + " location");
				while (loc != null) {
					Properties pp = new Properties();
					try (FileInputStream in = new FileInputStream(loc)) {
						pp.load(in);
						in.close();
					}
					p.putAll(pp);
					loc = JOptionPane.showInputDialog("Properties #" + (++i) + " location");
				}
			}

			Map<String, ETreeTableRecordBean<Entry<String, String>>> parents = new HashMap<String, ETreeTableRecordBean<Entry<String, String>>>();

			for (Entry<?, ?> entry : p.entrySet()) {
				ETreeTableRecordBean<Entry<String, String>> rec = new ETreeTableRecordBean<Entry<String, String>>(null, (Entry<String, String>) entry, h);
				String k = entry.getKey().toString();
				String kpc = null;
				ETreeTableRecordBean<Entry<String, String>> prevparent = null;
				for (String kp : k.split("\\.")) {
					if (kpc == null) {
						kpc = kp;
					} else {
						kpc += "." + kp;
					}
					if ((kpc == null) || kpc.equals(k)) {
						continue;
					}
					ETreeTableRecordBean<Entry<String, String>> parent = parents.get(kpc);
					if (parent == null) {
						parent = new ETreeTableRecordBean<Entry<String, String>>(prevparent, new SimpleEntry<String, String>(kp, null), h);
						parents.put(kpc, parent);
					}
					prevparent = parent;

				}
				rec.setParent(prevparent);
				col.add(rec);
			}

			ETreeTableHeaders<Map.Entry<String, String>> headers = new ETreeTableHeaders<Map.Entry<String, String>>(h);
			ETreeTable<Map.Entry<String, String>> t = new ETreeTable<Map.Entry<String, String>>(new ETreeTableConfig().setEditable(true), headers);
			t.stsi().addRecords(col);

			f.getContentPane().add(new JScrollPane(t));
			f.pack();
			f.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
