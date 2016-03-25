import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DownloadDependencies {
	public static void main(String[] args) {
		try {
			File dlist = new File("dependencylist.txt");
			if (!dlist.exists()) {
				System.err.println(dlist.getAbsolutePath()
						+ " does not exist in");
			}
			File rlist = new File("repositories.txt");
			if (!rlist.exists()) {
				System.err.println(rlist.getAbsolutePath()
						+ " does not exist in");
			}
			List<String> r = new ArrayList<String>();
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(rlist)));
				String line;
				while ((line = br.readLine()) != null) {
					r.add(line);
					System.out.println("repository: " + line);
				}
			}
			List<String> d = new ArrayList<String>();
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(dlist)));
				String line;
				boolean start = false;
				while ((line = br.readLine()) != null) {
					if (line.indexOf("The following files have been resolved:") != -1) {
						start = true;
						continue;
					}
					if (start) {
						if (line.trim().equals("[INFO]")) {
							start = false;
						}
					}
					if (start) {
						d.add(line.substring("[INFO]".length()).trim());
					}
				}
			}
			File out = new File("dependencies");
			out.mkdir();
			for (String dd : d) {
				String[] parts = dd.split(":");
				if (parts[2].equals("jar")) {
					String fname = parts[1] + "-" + parts[3] + ".jar";
					String url = parts[0].replace('.', '/') + "/" + parts[1]
							+ "/" + parts[3] + "/" + fname;
					System.out.println("file " + fname + " ...");
					boolean dl = false;
					for (String rr : r) {
						URL u = new URL(rr + '/' + url);
						ReadableByteChannel rbc;
						InputStream stream;
						try {
							stream = u.openStream();
							rbc = Channels.newChannel(stream);
						} catch (java.io.FileNotFoundException e) {
							continue;
						}
						System.out.println("... downloading from " + rr);
						File tmp = new File(out, fname + ".tmp");
						FileOutputStream fos = new FileOutputStream(tmp);
						fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
						fos.close();
						stream.close();
						tmp.renameTo(new File(out, fname));
						System.out.println("completed "
								+ new File(out, fname).length() + " bytes");
						dl = true;
						break;
					}
					if (!dl)
						System.out.println("not found anywhere");
				}
			}
			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
