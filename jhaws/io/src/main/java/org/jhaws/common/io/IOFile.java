package org.jhaws.common.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.jhaws.common.io.filter.AudioFileFilter;
import org.jhaws.common.io.filter.ImageFileFilter;
import org.jhaws.common.io.filter.MediaFileFilter;
import org.jhaws.common.io.filter.VideoFileFilter;

// http://en.wikipedia.org/wiki/List_of_file_formats
/**
 * functionality that java.io.File lacks for real files only.<br>
 *
 * @author Jurgen
 * @version 29 January 2007
 *
 * @see IOGeneralFile
 * @see IODirectory
 */
@Deprecated
@SuppressWarnings({ "deprecation", "resource" })
public class IOFile extends IOGeneralFile<IOFile> {
	/**
	 * delete ShutDownHook
	 */
	static class ShutDownHook implements Runnable {
		/** field */
		private Set<IOFile> toerase = new HashSet<IOFile>();

		/** field */
		private Set<IOFile> torecycle = new HashSet<IOFile>();

		/**
		 * Creates a new ShutDownHook object.
		 */
		public ShutDownHook() {
			super();
		}

		/**
		 * NA
		 *
		 * @param todelete
		 *            NA
		 */
		public void addErase(IOFile todelete) {
			this.toerase.add(todelete);
		}

		/**
		 * NA
		 *
		 * @param todelete
		 *            NA
		 */
		public void addRecycle(IOFile todelete) {
			this.torecycle.add(todelete);
		}

		/**
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			for (IOFile todelete : this.toerase) {
				if (todelete.exists() && !todelete.erase()) {
					System.err.println("could not erase " + todelete);
				}
			}

			for (IOFile todelete : this.torecycle) {
				if (todelete.exists() && !todelete.recycle()) {
					System.err.println("could not recycle " + todelete);
				}
			}
		}
	}

	/**
	 * creates a file when not exists
	 *
	 * @param dir
	 *            : IODirectory : directory
	 * @param file
	 *            : String : filename (or path and filename)
	 *
	 * @return : IoFile : file created or existing or null when unable to create or null when exists and is a directory
	 */
	public static IOFile create(final IODirectory dir, final String file) {
		return IOFile.create(new IOFile(dir, file).getAbsolutePath());
	}

	/**
	 * creates a file when not exists
	 *
	 * @param filePath
	 *            : String : file name & path to create
	 *
	 * @return : IoFile : file created or existing or null when unable to create or null when exists and is a directory
	 */
	public static IOFile create(final String filePath) {
		try {
			IOFile file = new IOFile(filePath);

			// Create file if it does not exist
			boolean success = file.createNewFile();

			if (success) {
				// File did not exist and was created
				return file;
			}

			// File already exists
			if (file.isDirectory()) {
				return null;
			}

			return file;
		} catch (final IOException ex) {
			System.err.println(ex + ": " + ex.getMessage()); //$NON-NLS-1$

			return null;
		}
	}

	/**
	 * gets the default maximum compare size (class level)
	 *
	 * @return : int : default maximum compare size
	 */
	public static int getDefaultMaxCompareSize() {
		return IOFile.MAX_COMPARE_SIZE;
	}

	/**
	 * gets filepath separator
	 *
	 * @return always '/'
	 */
	public static char getSeparator() {
		return '/';
	}

	/**
	 * return a file in the temp dir
	 *
	 * @param relativeName
	 *            relative name in temp dir
	 *
	 * @return IOFile
	 */
	public static IOFile newTempFile(String relativeName) {
		return new IOFile(IODirectory.getTempDir(), relativeName);
	}

	/**
	 * new file in temporary directory with random extension with random name
	 *
	 * @return IOFile
	 */
	public static IOFile newTmpFile() {
		return IOFile.newTmpFileExt("tmp");
	}

	/**
	 * new file in temporary directory with given name and extension
	 *
	 * @param name
	 *            name
	 *
	 * @return IOFile
	 */
	public static IOFile newTmpFile(String name) {
		IOFile tmp = new IOFile(IODirectory.TEMPDIR, name);
		tmp.eraseOnExit();

		return tmp;
	}

	/**
	 * new file in temporary directory with given extension with random name
	 *
	 * @param ext
	 *            extension
	 *
	 * @return IOFile
	 */
	public static IOFile newTmpFileExt(String ext) {
		return IOFile.newTmpFile(IOFile.rand(32) + "." + ext); //$NON-NLS-1$
	}

	/**
	 * return a file in the user home dir
	 *
	 * @param relativeName
	 *            relative name in user home dir
	 *
	 * @return IOFile
	 */
	public static IOFile newUserFile(String relativeName) {
		return new IOFile(IODirectory.getUserDir(), relativeName);
	}

	/**
	 * na
	 *
	 * @param LEN
	 *            na
	 *
	 * @return
	 */
	public static String rand(int LEN) {
		char[] nc = new char[LEN];

		for (int i = 0; i < LEN; i++) {
			int index = IOFile.r.nextInt(IOFile.c.length);
			nc[i] = IOFile.c[index];
		}

		return new String(nc);
	}

	/**
	 * sets the default maximum compare size (class level)
	 *
	 * @param maxCompareSize
	 *            : int : default maximum compare size
	 */
	public static void setDefaultMaxCompareSize(final int maxCompareSize) {
		IOFile.MAX_COMPARE_SIZE = maxCompareSize;
	}

	/** serialVersionUID */
	private static final long serialVersionUID = 6893324119288839097L;

	/** maximum size that is taken to compare a file binary is 8 kB */
	protected static int MAX_COMPARE_SIZE = 8192;

	/** field */
	private static final ShutDownHook SHUTDOWNHOOK;

	static {
		SHUTDOWNHOOK = new ShutDownHook();
		Runtime.getRuntime().addShutdownHook(new Thread(IOFile.SHUTDOWNHOOK));
	}

	/** field */
	private static char[] c = "ABCDEFGHIJLKMNOPQRSTUVWXYZ0123456789".toCharArray(); //$NON-NLS-1$

	/** field */
	private static Random r = new Random(System.currentTimeMillis());

	/** field */
	private static ExtensionIconFinder grabber_alt = null;

	static {
		try {
			IOFile.grabber_alt = (ExtensionIconFinder) Class.forName("util.io.ExtensionsIconsAlt").newInstance(); //$NON-NLS-1$
		} catch (Throwable e) {
			//
		}
	}

	/** field */
	private static ExtensionIconFinder grabber = null;

	static {
		try {
			IOFile.grabber = (ExtensionIconFinder) Class.forName("util.io.ExtensionIcons").newInstance(); //$NON-NLS-1$
		} catch (Throwable e) {
			//
		}
	}

	/** maximum compare size for the object */
	protected int maxCompareSize = IOFile.MAX_COMPARE_SIZE;

	/**
	 * Creates a new IOFile object.
	 *
	 * @param file
	 *            : File : File object
	 */
	public IOFile(final File file) {
		super(file);
		try {
			this.checkIfFile();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Creates a new IOFile object.
	 *
	 * @param parent
	 *            : File : directory
	 * @param child
	 *            : String : short name of file
	 */
	public IOFile(final File parent, final String child) {
		super(parent, child);
		try {
			this.checkIfFile();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Creates a new IOFile object.
	 *
	 * @param pathname
	 *            : String : full path name of file; when only a name is given, the file will be in the current working directory
	 */
	public IOFile(final String pathname) {
		super(pathname.replace('\\', '/'));
		try {
			this.checkIfFile();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Creates a new IOFile object.
	 *
	 * @param parent
	 *            : String : directory
	 * @param child
	 *            : String : short name of file
	 */
	public IOFile(final String parent, final String child) {
		this(new IODirectory(parent), child);
		try {
			this.checkIfFile();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * checks if a file may be overwritten or not in save mode (asks if necessary)
	 *
	 * @param destinationFile
	 *            : File : target directory
	 *
	 * @return : boolean : file may be overwritten or not
	 */
	protected boolean canOverwrite(final File destinationFile) {
		// file does not exists
		if (!destinationFile.exists()) {
			return true;
		}

		// file does exists
		// no 'all' value has been chosen yet -> show dialog box
		if ((this.overwriteOptionChosen == null) || (this.overwriteOptionChosen.compareTo(IOGeneralFile.overwriteOptions[1]) == 0)
				|| (this.overwriteOptionChosen.compareTo(IOGeneralFile.overwriteOptions[1]) == 0)) {
			this.overwriteOptionChosen = JOptionPane.showInputDialog(null, "overwrite file " + destinationFile + "?", "overwrite", JOptionPane.QUESTION_MESSAGE, null, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					IOGeneralFile.overwriteOptions, IOGeneralFile.overwriteOptions[1]).toString();
		}

		if (this.overwriteOptionChosen.equals(IOGeneralFile.overwriteOptions[0])) {
			// yes
			return true;
		}

		if (this.overwriteOptionChosen.equals(IOGeneralFile.overwriteOptions[1])) {
			// no
			return false;
		}

		if (this.overwriteOptionChosen.equals(IOGeneralFile.overwriteOptions[2])) {
			// yes to all
			return true;
		}

		if (this.overwriteOptionChosen.equals(IOGeneralFile.overwriteOptions[3])) {
			// no to all
			return false;
		}

		return true;
	}

	/**
	 * checks if a file may be overwritten or not
	 *
	 * @param destinationFile
	 *            : File : target directory
	 * @param parameter
	 *            : int : ALL, ONLY_NEWER, ONLY_NOT_EXISTING, NEWER_AND_NON_EXISTING
	 *
	 * @return : boolean : file may be overwritten or not
	 */
	protected boolean canOverwrite(final File destinationFile, final Replace parameter) {
		long sourceFileTime = new Date(this.lastModified()).getTime();
		long destinationFileTime = 0L;
		boolean destinationFileExistst = destinationFile.exists();

		if (destinationFileExistst) {
			destinationFileTime = new Date(destinationFile.lastModified()).getTime();
		}

		if (parameter == Replace.NEWER_AND_NON_EXISTING) {
			if (!destinationFileExistst) {
				return true;
			}

			if (sourceFileTime <= destinationFileTime) {
				return false;
			}

			return true;
		}

		if (parameter == Replace.ONLY_NEWER) {
			if (!destinationFileExistst) {
				return false;
			}

			if (sourceFileTime <= destinationFileTime) {
				return false;
			}

			return true;
		}

		if (parameter == Replace.ONLY_NOT_EXISTING) {
			if (!destinationFileExistst) {
				return true;
			}

			return false;
		}

		return true;
	}

	/**
	 * {@link #checkFileIndex(String, String, String, String, String)} but with separator set to '_' and format to '0000' and the other parameters derived from given File
	 *
	 * @return : IOFile : new indexed File
	 */
	public IOFile checkFileIndex() {
		try {
			if (!this.exists()) {
				return this;
			}

			String str = this.getAbsolutePath();
			int p1 = str.lastIndexOf(File.separator);

			if (p1 == -1) {
				p1 = str.length();
			}

			String path = str.substring(0, p1 + 1);
			String file = str.substring(p1 + 1);

			int p2 = file.lastIndexOf("."); //$NON-NLS-1$

			if (p2 == -1) {
				p2 = file.length();
			}

			String shortFile = file.substring(0, p2);
			String extension = ""; //$NON-NLS-1$

			if (p2 != file.length()) {
				extension = file.substring(p2 + 1);
			}

			return IOGeneralFile.checkFileIndex(path, shortFile, extension);
		} catch (final Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * {@link #checkFileIndex(String, String, String, String, String)} but with given separator and given format and the other parameters derived from given File
	 *
	 * @param separator0
	 *            : String : characters sperating filename from index (example: _ )
	 * @param format
	 *            : String : number of positions character 0 (example: 0000 )
	 *
	 * @return : IOFile : new indexed File
	 */
	public IOFile checkFileIndex(final String separator0, final String format) {
		String str = this.getAbsolutePath();
		int p2 = str.lastIndexOf("."); //$NON-NLS-1$

		if (p2 == -1) {
			p2 = str.length();
		}

		int p1 = str.lastIndexOf(File.separator);

		if (p1 == -1) {
			p1 = str.length();
		}

		String path = str.substring(0, p1 + 1);
		String outFileName = str.substring(p1 + 1, p2);
		String extension = ""; //$NON-NLS-1$

		// if (p2 < str.length()) {
		// str.substring(p2 + 1, str.length());
		// }

		return IOGeneralFile.checkFileIndex(path, outFileName, separator0, format, extension);
	}

	/**
	 * checks for parameter, throws exception if needed
	 *
	 * @return
	 *
	 * @throws ObjectIsNoFileException
	 *             : thrown exception
	 */
	protected IOFile checkIfFile() throws IOException {
		if (this.exists() && this.isDirectory()) {
			throw new IOException(this.getAbsolutePath());
		}

		return this;
	}

	/**
	 * compares two files binary, reads only first 8192 bytes (default) (MAX_COMPARE_SIZE)
	 *
	 * @param file
	 *            : IOFile : file to compare to
	 *
	 * @return : boolean : file is equal or not
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public boolean compare2(final IOFile file) throws IOException {
		return this.compare2(file, this.maxCompareSize);
	}

	/**
	 * compares two files binary, reads only first maxCompareSize bytes
	 *
	 * @param file
	 *            : IOFile : file to compare to
	 * @param maxCompareSize0
	 *            : int : maximum size when comparing files
	 *
	 * @return : boolean : file is equal or not
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public boolean compare2(final IOFile file, final int maxCompareSize0) throws IOException {
		this.checkExistence();

		try {
			if (this.length() != file.length()) {
				return false;
			}

			int compareSize = Math.min(maxCompareSize0, (int) this.length());

			BufferedInputStream iser1 = new BufferedInputStream(new FileInputStream(this));
			byte[] b1 = new byte[compareSize];
			iser1.read(b1, 0, compareSize);
			iser1.close();

			BufferedInputStream iser2 = new BufferedInputStream(new FileInputStream(file));
			byte[] b2 = new byte[compareSize];
			iser2.read(b2, 0, compareSize);
			iser2.close();

			int bpos = 0;

			while (bpos < compareSize) {
				if (b1[bpos] != b2[bpos]) {
					return false;
				}

				bpos++;
			}

			return true;
		} catch (final IOException ex) {
			throw new IOException(ex);
		}
	}

	/**
	 * converts a size in bytes to kB, MB, GB, TB
	 *
	 * @param conversion
	 *            : int : use BYTE_TO_KiloByte, BYTE_TO_MegaByte, BYTE_TO_GigaByte, BYTE_TO_TerraByte
	 * @param decimals
	 *            : int : number of decimals to show
	 *
	 * @return : double : converted size
	 * @throws FileNotFoundException
	 */
	public double convertSize(final ByteTo conversion, final int decimals) throws FileNotFoundException {
		this.checkExistence();

		return super.convertSize(this.length(), conversion, decimals);
	}

	/**
	 * converts this file's size to string, decimals 2
	 *
	 * @return : String : converted size as string
	 */
	public String convertSize2String() {
		return super.convertSize2String(this.length());
	}

	/**
	 * converts a size in bytes to kB, MB, GB, TB with suffix
	 *
	 * @param conversion
	 *            : int : use BYTE_TO_KiloByte, BYTE_TO_MegaByte, BYTE_TO_GigaByte, BYTE_TO_TerraByte
	 * @param decimals
	 *            : int : number of decimals to show
	 *
	 * @return : String : converted size with size suffix
	 * @throws FileNotFoundException
	 */
	public String convertSize2String(final ByteTo conversion, final int decimals) throws FileNotFoundException {
		this.checkExistence();

		return super.convertSize2String(this.length(), conversion, decimals);
	}

	/**
	 * autoconverts a size in bytes to kB, MB, GB, TB with a suffix
	 *
	 * @param decimals
	 *            : int : number of decimals to show
	 *
	 * @return : String : converted size with size suffix
	 * @throws FileNotFoundException
	 */
	public String convertSize2String(final int decimals) throws FileNotFoundException {
		this.checkExistence();

		return super.convertSize2String(this.length(), decimals);
	}

	/**
	 * na
	 *
	 * @param target
	 *            na
	 * @param listener
	 *
	 * @return
	 *
	 * @throws IOException
	 */
	protected IOFile copy0(IOFile target, IOCopyListener listener) throws IOException {
		if (listener != null) {
			listener.copyStarted(this, target);
		}

		target.getParentDirectory().create();

		try {
			FileInputStream fin = new FileInputStream(this);
			FileOutputStream fout = new FileOutputStream(target);
			byte[] buffer = new byte[1024 * 1024];
			int read = fin.read(buffer);
			long total = 0;

			while (read > 0) {
				fout.write(buffer, 0, read);
				read = fin.read(buffer);
				total += read;

				if (listener != null) {
					listener.copyProgress(total);
				}
			}

			fin.close();
			fout.close();
		} catch (IOException ex) {
			if (listener != null) {
				listener.copyFailed(this, target, ex);
			}

			throw new IOException(ex);
		} catch (RuntimeException ex) {
			if (listener != null) {
				listener.copyFailed(this, target, ex);
			}

			throw ex;
		}

		if (listener != null) {
			listener.copyFinished(this, target);
		}

		return target;
	}

	/**
	 * copies a file to another file (binary), overwrites
	 *
	 * @param destination
	 *            : IODirectory : destination directory
	 *
	 * @return : IOFile : destination file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile copy2(IODirectory destination) throws IOException {
		return this.copy2(destination, (IOCopyListener) null);
	}

	/**
	 * copies a file to another file (binary), overwrites
	 *
	 * @param destination
	 *            : IODirectory : destination directory
	 * @param listener
	 *
	 * @return : IOFile : destination file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile copy2(IODirectory destination, IOCopyListener listener) throws IOException {
		return this.move_copy(new IOFile(destination, this.getName()), false, true, false, false, false, listener);
	}

	/**
	 * copies a file to another file (binary)
	 *
	 * @param destination
	 *            : IODirectory : destination directory
	 * @param parameter
	 *            : int : ALL, ONLY_NEWER, ONLY_NOT_EXISTING, NEWER_AND_NON_EXISTING
	 *
	 * @return : IOFile : destination file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile copy2(IODirectory destination, Replace parameter) throws IOException {
		return this.copy2(new IOFile(destination, this.getName()), parameter);
	}

	/**
	 * copies a file to another file (binary), overwrites by default
	 *
	 * @param destination
	 *            : IOFile : destination file
	 *
	 * @return : IOFile : destination file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile copy2(final IOFile destination) throws IOException {
		return this.copy2(destination, (IOCopyListener) null);
	}

	/**
	 * copies a file to another file (binary), overwrites by default
	 *
	 * @param destination
	 *            : IOFile : destination file
	 * @param listener
	 *
	 * @return : IOFile : destination file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile copy2(final IOFile destination, IOCopyListener listener) throws IOException {
		return this.move_copy(destination, false, true, false, false, false, listener);
	}

	/**
	 * copies a file to another file (binary)
	 *
	 * @param destination
	 *            : IOFile : destination file
	 * @param parameter
	 *            : int : ALL, ONLY_NEWER, ONLY_NOT_EXISTING, NEWER_AND_NON_EXISTING
	 *
	 * @return : IOFile : destination file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile copy2(final IOFile destination, final Replace parameter) throws IOException {
		return this.copy2(destination, parameter, (IOCopyListener) null);
	}

	/**
	 * copies a file to another file (binary)
	 *
	 * @param destination
	 *            : IOFile : destination file
	 * @param parameter
	 *            : int : ALL, ONLY_NEWER, ONLY_NOT_EXISTING, NEWER_AND_NON_EXISTING
	 * @param listener
	 *
	 * @return : IOFile : destination file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile copy2(final IOFile destination, final Replace parameter, IOCopyListener listener) throws IOException {
		if ((parameter == Replace.NEWER_AND_NON_EXISTING) || (parameter == Replace.ONLY_NEWER)) {
			return this.move_copy(destination, false, false, true, false, false, listener);
		}

		if (parameter == Replace.ONLY_NOT_EXISTING) {
			return this.move_copy(destination, false, false, false, false, false, listener);
		}

		// parameter == Replace.ALWAYS
		return this.move_copy(destination, false, true, false, false, false, listener);
	}

	/**
	 * copy file to destination (ask if file exists)
	 *
	 * @param destination
	 *            : IODirectory : destination directory
	 *
	 * @return : boolean : file copied
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile copySafely2(final IODirectory destination) throws IOException {
		return this.copySafely2(new IOFile(destination, this.getName()));
	}

	/**
	 * copies a file to another file (binary), asks when file exists
	 *
	 * @param destination
	 *            : IOFile : destination file
	 *
	 * @return : boolean : file is overwritten
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile copySafely2(final IOFile destination) throws IOException {
		return this.copySafely2(destination, (IOCopyListener) null);
	}

	/**
	 * copies a file to another file (binary), asks when file exists
	 *
	 * @param destination
	 *            : IOFile : destination file
	 * @param listener
	 *
	 * @return : boolean : file is overwritten
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile copySafely2(final IOFile destination, IOCopyListener listener) throws IOException {
		return this.move_copy(destination, false, false, false, true, false, listener);
	}

	/**
	 *
	 * @see java.io.File#delete()
	 */
	@Override
	public boolean delete() {
		return this.recycle();
	}

	/**
	 *
	 * @see java.io.File#deleteOnExit()
	 */
	@Override
	public void deleteOnExit() {
		IOFile.SHUTDOWNHOOK.addRecycle(this);
	}

	/**
	 * downloads a file from the web to a local file when it does not exists or is older, binary copy
	 *
	 * @param urlSourceFile
	 *            : URL : file on the web
	 *
	 * @return : boolean : file downloaded or not
	 *
	 * @throws IOException
	 *             : IOException
	 */
	public boolean downloadFromURL(final URL urlSourceFile) throws IOException {
		try {
			if (this.exists()) {
				Date lastModified = new Date(this.lastModified());
				long localFileTime = lastModified.getTime();

				URLConnection conn = urlSourceFile.openConnection();
				long webFileTime = conn.getLastModified();

				if (webFileTime <= localFileTime) {
					return false;
				}
			}

			InputStream is_ = urlSourceFile.openStream();
			BufferedInputStream is = new BufferedInputStream(is_);
			FileOutputStream fos = new FileOutputStream(this);
			BufferedOutputStream os = new BufferedOutputStream(fos);

			int b = is.read();

			while (b != -1) {
				os.write(b);
				b = is.read();
			}

			is.close();
			os.close();

			return true;
		} catch (final IOException ex) {
			throw new IOException(ex);
		}
	}

	/**
	 * na
	 *
	 * @return
	 */
	public String dropExtension() {
		String name = this.getName();
		int pos = name.lastIndexOf('.');

		if (pos == -1) {
			return name;
		}

		return name.substring(0, pos);
	}

	/**
	 * erases a file
	 *
	 * @return : boolean : success
	 */
	public boolean erase() {
		return super.delete();
	}

	/**
	 * na
	 */
	public void eraseOnExit() {
		IOFile.SHUTDOWNHOOK.addErase(this);
	}

	/**
	 * gets the parent directory
	 *
	 * @return : IODirectory : parent directory
	 */
	public IODirectory getDirectory() {
		return new IODirectory(this.getAbsoluteFile().getParentFile());
	}

	/**
	 * gets the extension of a file ("" when no extension)
	 *
	 * @return : String : extension
	 */
	public String getExtension() {
		String[] name = this.getName().split("\\."); //$NON-NLS-1$

		if (name.length == 1) {
			return ""; //$NON-NLS-1$
		}

		return name[name.length - 1];
	}

	/**
	 * gets the extension from given file and maps it to an image icon (png read from this jar)
	 *
	 * @return : ImageIcon
	 */
	public Icon getLargeIcon1() {
		return (IOFile.grabber == null) ? null : IOFile.grabber.getLargeIcon(new FilePath(this));
	}

	/**
	 * gets the extension from given file and maps it to an image icon (needs swing and icons package)
	 *
	 * @return : ImageIcon
	 */
	public Icon getLargeIcon2() {
		return (IOFile.grabber_alt == null) ? null : IOFile.grabber_alt.getLargeIcon(new FilePath(this));
	}

	/**
	 * gets the maximum compare size (object level)
	 *
	 * @return : int : maximum compare size
	 */
	public int getMaxCompareSize() {
		return this.maxCompareSize;
	}

	/**
	 * returns another object for the same file returning only the name as {@link Object#toString()}
	 *
	 * @return IOFile
	 */
	public IOFile getShortIOFile() {
		return new IOFile(this) {
			/** serialVersionUID */
			private static final long serialVersionUID = 3150080605007952466L;

			@Override
			public String toString() {
				return this.getName();
			}
		};
	}

	/**
	 * gets the filename without the extension and the pathname
	 *
	 * @return : String : short filename
	 */
	public String getShortName() {
		return IOGeneralFile.getShortFile(this);
	}

	/**
	 * gets the extension from given file and maps it to an image icon (png read from this jar)
	 *
	 * @return : ImageIcon
	 */
	public Icon getSmallIcon1() {
		return (IOFile.grabber == null) ? null : IOFile.grabber.getSmallIcon(new FilePath(this));
	}

	/**
	 * gets the extension from given file and maps it to an image icon (needs swing and icons package)
	 *
	 * @return : ImageIcon
	 */
	public Icon getSmallIcon2() {
		return (IOFile.grabber_alt == null) ? null : IOFile.grabber_alt.getSmallIcon(new FilePath(this));
	}

	/**
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getAbsolutePath().hashCode();
	}

	/**
	 * is this file an audio file (via extension)
	 *
	 * @return : boolean
	 */
	public boolean isAudio() {
		return new AudioFileFilter().accept(this);
	}

	/**
	 * is this file the same age as the other file
	 *
	 * @param other
	 *            : IOFile : other file
	 *
	 * @return : boolean
	 */
	public boolean isEqualAge(final IOFile other) {
		if (this.lastModified() == other.lastModified()) {
			return true;
		}

		return false;
	}

	/**
	 * is this file the same size as the other file
	 *
	 * @param other
	 *            : IOFile : other file
	 *
	 * @return : boolean
	 */
	public boolean isEqualSize(final IOFile other) {
		if (this.length() == other.length()) {
			return true;
		}

		return false;
	}

	/**
	 * is this file an image (via extension)
	 *
	 * @return : boolean
	 */
	public boolean isImage() {
		return new ImageFileFilter().accept(this);
	}

	/**
	 * is this file larger than the other file
	 *
	 * @param other
	 *            : IOFile : other file
	 *
	 * @return : boolean
	 */
	public boolean isLarger(final IOFile other) {
		if (this.length() > other.length()) {
			return true;
		}

		return false;
	}

	/**
	 * is this file an media (movie) file (via extension)
	 *
	 * @return : boolean
	 */
	public boolean isMedia() {
		return new MediaFileFilter().accept(this);
	}

	/**
	 * is this file newer than the other file
	 *
	 * @param other
	 *            : IOFile : other file
	 *
	 * @return : boolean
	 */
	public boolean isNewer(final IOFile other) {
		if (this.lastModified() > other.lastModified()) {
			return true;
		}

		return false;
	}

	/**
	 * is this file older than the other file
	 *
	 * @param other
	 *            : IOFile : other file
	 *
	 * @return : boolean
	 */
	public boolean isOlder(final IOFile other) {
		if (this.lastModified() < other.lastModified()) {
			return true;
		}

		return false;
	}

	/**
	 * is this file smaller than the other file
	 *
	 * @param other
	 *            : IOFile : other file
	 *
	 * @return : boolean
	 */
	public boolean isSmaller(final IOFile other) {
		if (this.length() < other.length()) {
			return true;
		}

		return false;
	}

	/**
	 * is this file an media (movie) file (via extension)
	 *
	 * @return : boolean
	 */
	public boolean isVideo() {
		return new VideoFileFilter().accept(this);
	}

	/**
	 * na
	 *
	 * @return
	 */
	public Icon mapExtensionLarge() {
		String os = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$

		if (os.indexOf("win") != -1) { //$NON-NLS-1$

			return this.getLargeIcon();
		}

		Icon ic = this.getLargeIcon2();

		if (ic == null) {
			return this.getLargeIcon1();
		}

		return ic;
	}

	/**
	 * na
	 *
	 * @return
	 */
	public Icon mapExtensionSmall() {
		String os = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$

		if (os.indexOf("win") != -1) { //$NON-NLS-1$

			return this.getSmallIcon();
		}

		Icon ic = this.getSmallIcon2();

		if (ic == null) {
			return this.getSmallIcon1();
		}

		return ic;
	}

	/**
	 * invokes creating of directory on parent directory
	 *
	 * @return IODirectory
	 */
	public IODirectory mkDir() {
		return this.getParentDirectory().create();
	}

	/**
	 * internal use.<br>
	 * 'renameInstead' overwrites 'overwriteAlways,overwriteWhenNewer,askForOverwrite'<br>
	 * 'askForOverwrite' overwrites 'overwriteAlways,overwriteWhenNewer'<br>
	 * 'overwriteAlways' overwrites 'overwriteWhenNewer'<br>
	 * deleteSource=true MOVES and deleteSource=false COPIES<br>
	 *
	 * @param target
	 *            NA
	 * @param deleteSource
	 *            NA
	 * @param overwriteAlways
	 *            NA
	 * @param overwriteWhenNewer
	 *            NA
	 * @param askForOverwrite
	 *            NA
	 * @param renameInstead
	 *            NA
	 *
	 * @return NA
	 *
	 * @throws IOException
	 *             NA
	 */
	protected IOFile move_copy(IOFile target, boolean deleteSource, boolean overwriteAlways, boolean overwriteWhenNewer, boolean askForOverwrite, boolean renameInstead)
			throws IOException {
		return this.move_copy(target, deleteSource, overwriteAlways, overwriteWhenNewer, askForOverwrite, renameInstead, (IOCopyListener) null);
	}

	/**
	 * internal use.<br>
	 * 'renameInstead' overwrites 'overwriteAlways,overwriteWhenNewer,askForOverwrite'<br>
	 * 'askForOverwrite' overwrites 'overwriteAlways,overwriteWhenNewer'<br>
	 * 'overwriteAlways' overwrites 'overwriteWhenNewer'<br>
	 * deleteSource=true MOVES and deleteSource=false COPIES<br>
	 *
	 * @param target
	 *            NA
	 * @param deleteSource
	 *            NA
	 * @param overwriteAlways
	 *            NA
	 * @param overwriteWhenNewer
	 *            NA
	 * @param askForOverwrite
	 *            NA
	 * @param renameInstead
	 *            NA
	 * @param listener
	 *
	 * @return NA
	 *
	 * @throws IOException
	 *             NA
	 */
	protected IOFile move_copy(IOFile target, boolean deleteSource, boolean overwriteAlways, boolean overwriteWhenNewer, boolean askForOverwrite, boolean renameInstead,
			IOCopyListener listener) throws IOException {
		this.checkExistence();

		if (deleteSource) {
			if (target.exists()) {
				if (renameInstead) {
					return this.move_copy(target.checkFileIndex(), deleteSource, false, false, false, renameInstead, listener);
				}

				if (askForOverwrite) {
					if (this.canOverwrite(target)) {
						// move after asking
						this.move0(target, listener);
					} else {
						// only delete
						this.erase();
					}
				} else {
					if (overwriteAlways) {
						// move
						this.move0(target, listener);
					} else {
						if (overwriteWhenNewer) {
							if (this.isNewer(target)) {
								// move if newer
								this.move0(target, listener);
							} else {
								// only delete
								this.erase();
							}
						} else {
							// only delete
							this.erase();
						}
					}
				}
			} else {
				// move
				this.move0(target, listener);
			}
		} else {
			if (target.exists()) {
				if (renameInstead) {
					return this.move_copy(target.checkFileIndex(), deleteSource, false, false, false, renameInstead, listener);
				}

				if (askForOverwrite) {
					if (this.canOverwrite(target)) {
						// copy after asking
						this.copy0(target, listener);
					} else {
						// nothing
					}
				} else {
					if (overwriteAlways) {
						// copy
						this.copy0(target, listener);
					} else {
						if (overwriteWhenNewer) {
							if (this.isNewer(target)) {
								// copy if newer
								this.copy0(target, listener);
							} else {
								// nothing
							}
						} else {
							// nothing
						}
					}
				}
			} else {
				// copy
				this.copy0(target, listener);
			}
		}

		return target;
	}

	/**
	 * na
	 *
	 * @param target
	 *            na
	 * @param listener
	 *
	 * @return
	 *
	 * @throws IOException
	 *             na
	 */
	protected IOFile move0(IOFile target, IOCopyListener listener) throws IOException {
		target.getParentDirectory().create();

		if (target.exists()) {
			target.erase();
		}

		boolean success = true;

		if (System.getProperty("os.name").toLowerCase().equals("unix") || System.getProperty("os.name").toLowerCase().equals("linux")) { //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
			this.copy0(target, listener);
			super.delete();
			success = target.exists() && !this.exists();
		} else {
			success = this.renameTo(target);
		}

		if (!success) {
			System.out.println("failed to move file " + this + " to " + target); //$NON-NLS-1$//$NON-NLS-2$
		}

		return target;
	}

	/**
	 * moves a file (copy & delete), always overwrites
	 *
	 * @param destination
	 *            : IODirectory : destination directory
	 *
	 * @return : IOFile : destination file
	 *
	 * @throws IOException
	 */
	public IOFile move2(final IODirectory destination) throws IOException {
		return this.move2(new IOFile(destination, this.getName()));
	}

	/**
	 * moves a file (copy & delete), always overwrites
	 *
	 * @param destination
	 *            : IOFile : destination file
	 *
	 * @return : IOFile : destination file
	 *
	 * @throws IOException
	 */
	public IOFile move2(final IOFile destination) throws IOException {
		return this.move2(destination, (IOCopyListener) null);
	}

	/**
	 * moves a file (copy & delete), always overwrites
	 *
	 * @param destination
	 *            : IOFile : destination file
	 * @param listener
	 *
	 * @return : IOFile : destination file
	 *
	 * @throws IOException
	 */
	public IOFile move2(final IOFile destination, IOCopyListener listener) throws IOException {
		return this.move_copy(destination, true, true, false, false, false, listener);
	}

	/**
	 * moves a file (ask when file will be overwritten)
	 *
	 * @param destination
	 *            IODirectory : destination directory
	 *
	 * @return : boolean : file moved
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile moveSafely2(final IODirectory destination) throws IOException {
		return this.moveSafely2(new IOFile(destination, this.getName()));
	}

	/**
	 * moves a file (ask when file will be overwritten)
	 *
	 * @param destination
	 *            : IOFile : destination file
	 *
	 * @return : boolean : file moved
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile moveSafely2(final IOFile destination) throws IOException {
		return this.moveSafely2(destination, (IOCopyListener) null);
	}

	/**
	 * moves a file (ask when file will be overwritten)
	 *
	 * @param destination
	 *            : IOFile : destination file
	 * @param listener
	 *
	 * @return : boolean : file moved
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile moveSafely2(final IOFile destination, IOCopyListener listener) throws IOException {
		return this.move_copy(destination, true, false, false, true, false, listener);
	}

	/**
	 * copyViaChannel
	 *
	 * @param target
	 * @param blocks
	 *
	 * @return
	 *
	 * @throws IOException
	 */
	public IOFile niocopy(IOFile target, boolean blocks) throws IOException {
		try (FileChannel inChannel = new FileInputStream(this).getChannel(); FileChannel outChannel = new FileOutputStream(target).getChannel()) {
			if (blocks) {
				// magic number for Windows, 64Mb - 32Kb)
				int maxCount = (64 * 1024 * 1024) - (32 * 1024);
				long size = inChannel.size();
				long position = 0;

				while (position < size) {
					position += inChannel.transferTo(position, maxCount, outChannel);
				}
			} else {
				inChannel.transferTo(0, inChannel.size(), outChannel);
			}
			inChannel.close();
			outChannel.close();
		} catch (IOException e) {
			throw e;
		}
		return target;
	}

	/**
	 * gets the bytes from a file up to the maximum capacity for the lenght of an array
	 *
	 * @return byte[] : byte array from file
	 *
	 * @throws IOException
	 *             : thrown exception
	 * @throws FileTooLargeException
	 *             : thrown exception
	 */
	public byte[] read() throws IOException {
		this.checkExistence();

		InputStream is = new FileInputStream(this);

		// Get the size of the file
		long length = this.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
			is.close();
			throw new IOException(String.valueOf(length));
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;

		while ((offset < bytes.length) && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			is.close();
			throw new IOException(new IOException("Could not completely read file " + this.getAbsolutePath())); //$NON-NLS-1$
		}

		// Close the input stream and return bytes
		is.close();

		return bytes;

	}

	/**
	 * gets the bytes from a file up to the maximum capacity for the lenght of an array
	 *
	 * @return ByteBuffer : byte buffer from file
	 *
	 * @throws IOException
	 *             : thrown exception
	 * @throws FileTooLargeException
	 *             : thrown exception
	 */
	public ByteBuffer readToByteBuffer() throws IOException {
		return ByteBuffer.wrap(this.read());
	}

	/**
	 *
	 * @see util.io.jni.Recycler#recycle(java.io.File)
	 */
	public boolean recycle() {
		return (!this.exists()) ? false : RecyclerFactory.recycle(this);
	}

	/**
	 * recycles a file
	 *
	 * @param alternativeRecycleBin
	 *            : IODirectory : alternative recycle directory when impossible to use system recycle
	 *
	 * @return : Object : null => could not delete, Boolean(true) => deleted to OS recycle bin, String => deleted to location on disk
	 */
	public boolean recycle(final IODirectory alternativeRecycleBin) {
		alternativeRecycleBin.create();

		IODirectory old = RecyclerFactory.getDefaultRecycleDirectory();
		RecyclerFactory.setDefaultRecycleDirectory(alternativeRecycleBin);

		boolean o = this.recycle();
		RecyclerFactory.setDefaultRecycleDirectory(old);

		return o;
	}

	/**
	 * rename a file
	 *
	 * @param target
	 *            target
	 *
	 * @return target or null when failed
	 */
	public IOFile rename(IOFile target) {
		return this.renameTo(target) ? target : null;
	}

	/**
	 * searches for a specific byte array in a file from start position 0 (binary search works with ALL sorts of files) (will decode if encoding is found), searches case sensitive
	 *
	 * @param searchForBytes
	 *            : byte[] : bytes to search for
	 *
	 * @return : int : position where searched bytes are found (starting from 0), negative when not found (-1: not found, -2: startpos greater than size, -3: file empty or
	 *         [startpos+1] greater than size, -4: IOException)
	 * @throws FileNotFoundException
	 */
	public int searchFor(final byte[] searchForBytes) throws FileNotFoundException {
		return this.searchFor(0, searchForBytes, true);
	}

	/**
	 * searches for a specific byte array in a file from start position 0 (binary search works with ALL sorts of files) (will decode if encoding is found), can search case
	 * sensitive or insensitive
	 *
	 * @param searchForBytes
	 *            : byte[] : bytes to search for
	 * @param caseSensitive
	 *            : boolean : case sensitive search
	 *
	 * @return : int : position where searched bytes are found (starting from 0), negative when not found (-1: not found, -2: startpos greater than size, -3: file empty or
	 *         [startpos+1] greater than size, -4: IOException)
	 * @throws FileNotFoundException
	 */
	public int searchFor(final byte[] searchForBytes, final boolean caseSensitive) throws FileNotFoundException {
		return this.searchFor(0, searchForBytes, caseSensitive);
	}

	/**
	 * searches for a specific string in a file from given start position (binary search works with ALL sorts of files) (will decode if encoding is found), searches case sensitive
	 *
	 * @param startPos
	 *            : int : position where to start searching in file
	 * @param searchForBytes
	 *            : byte[] : bytes to search for
	 *
	 * @return : int : position where searched bytes are found (starting from 0), negative when not found (-1: not found, -2: startpos greater than size, -3: file empty or
	 *         [startpos+1] greater than size, -4: IOException)
	 * @throws FileNotFoundException
	 */
	public int searchFor(final int startPos, final byte[] searchForBytes) throws FileNotFoundException {
		return this.searchFor(startPos, searchForBytes, true);
	}

	/**
	 * searches for a specific byte array in a file from given start position (binary search works with ALL sorts of files) (will decode if encoding is found), can search case
	 * sensitive or insensitive
	 *
	 * @param startPos
	 *            : position where to start searching in file
	 * @param searchForBytes
	 *            : byte[] : bytes to search for
	 * @param caseSensitive
	 *            : boolean : case sensitive search
	 *
	 * @return : int : position where searched bytes are found (starting from 0), negative when not found (-1: not found, -2: startpos greater than size, -3: file empty or
	 *         [startpos+1] greater than size, -4: IOException)
	 * @throws FileNotFoundException
	 */
	public int searchFor(final int startPos, final byte[] searchForBytes, final boolean caseSensitive) throws FileNotFoundException {
		this.checkExistence();

		try {
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(this));
			EncodingInfo enci = DecodingReader.findEncoding(this);
			InputStreamReader isr = new InputStreamReader(is);

			if ((enci.getEncoding() != null) && (enci.getEncoding().length() != 0)) {
				isr = new InputStreamReader(is, enci.getEncoding());
			}

			for (int i = 0; i < startPos; i++) {
				int tmp = isr.read();

				if (tmp == -1) {
					isr.close();

					return -2;
				}
			}

			int b = isr.read();

			if (b == -1) {
				isr.close();

				return -3;
			}

			int filePos = startPos;
			int index = 0;

			while (b != -1) {
				byte c0 = (byte) b;

				if (caseSensitive) {
					if ((char) c0 == (char) searchForBytes[index]) {
						index++;
					} else {
						index = 0;
					}
				} else {
					if (Character.toUpperCase((char) c0) == Character.toUpperCase((char) searchForBytes[index])) {
						index++;
					} else {
						index = 0;
					}
				}

				if (index == searchForBytes.length) {
					isr.close();

					return ((filePos - searchForBytes.length) + 1);
				}

				filePos++;
				b = isr.read();
			}

			isr.close();

			return -1;
		} catch (final IOException ex) {
			ex.printStackTrace();

			return -4;
		}
	}

	/**
	 * searches for a specific string in a file from given start position (binary search works with ALL sorts of files) (will decode if encoding is found), searches case sensitive
	 *
	 * @param startPos
	 *            : int : position where to start searching in file
	 * @param searchFor
	 *            : String : string to search for
	 *
	 * @return : int : position where searched bytes are found (starting from 0), negative when not found (-1: not found, -2: startpos greater than size, -3: file empty or
	 *         [startpos+1] greater than size, -4: IOException)
	 * @throws FileNotFoundException
	 */
	public int searchFor(final int startPos, final String searchFor) throws FileNotFoundException {
		return this.searchFor(startPos, searchFor.getBytes(), true);
	}

	/**
	 * searches for a specific string in a file from given start position (binary search works with ALL sorts of files) (will decode if encoding is found), can search case
	 * sensitive or insensitive
	 *
	 * @param startPos
	 *            : int : position where to start searching in file
	 * @param searchFor
	 *            : String : string to search for
	 * @param caseSensitive
	 *            : boolean : case sensitive search
	 *
	 * @return : int : position where searched bytes are found (starting from 0), negative when not found (-1: not found, -2: startpos greater than size, -3: file empty or
	 *         [startpos+1] greater than size, -4: IOException)
	 * @throws FileNotFoundException
	 */
	public int searchFor(final int startPos, final String searchFor, final boolean caseSensitive) throws FileNotFoundException {
		return this.searchFor(startPos, searchFor.getBytes(), caseSensitive);
	}

	/**
	 * searches for a specific string in a file from start position 0 (binary search works with ALL sorts of files), searches case sensitive
	 *
	 * @param searchFor
	 *            : String : string to search for
	 *
	 * @return : int : position where searched bytes are found (starting from 0), negative when not found (-1: not found, -2: startpos greater than size, -3: file empty or
	 *         [startpos+1] greater than size, -4: IOException)
	 * @throws FileNotFoundException
	 */
	public int searchFor(final String searchFor) throws FileNotFoundException {
		return this.searchFor(0, searchFor.getBytes(), true);
	}

	/**
	 * searches for a specific string in a file from start position 0 (binary search works with ALL sorts of files), can search case sensitive or insensitive
	 *
	 * @param searchFor
	 *            : String : string to search for
	 * @param caseSensitive
	 *            : boolean : case sensitive search
	 *
	 * @return : int : position where searched bytes are found (starting from 0), negative when not found (-1: not found, -2: startpos greater than size, -3: file empty or
	 *         [startpos+1] greater than size, -4: IOException)
	 * @throws FileNotFoundException
	 */
	public int searchFor(final String searchFor, final boolean caseSensitive) throws FileNotFoundException {
		return this.searchFor(0, searchFor.getBytes(), caseSensitive);
	}

	/**
	 * sets the maximum compare size (object level)
	 *
	 * @param maxCompareSize
	 *            : int : maximum compare size
	 *
	 * @return
	 */
	public IOFile setMaxCompareSize(final int maxCompareSize) {
		this.maxCompareSize = maxCompareSize;

		return this;
	}

	/**
	 * writes a byte array to a file (possibly overwrites the file)
	 *
	 * @param source
	 *            : byte[] : source
	 *
	 * @return : IOFile : this file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile writeBytes(final byte[] source) throws IOException {
		return this.writeBytes(source, false);
	}

	/**
	 * writes a byte array to a file (possibly overwrites the file)
	 *
	 * @param source
	 *            : byte[] : source
	 * @param append
	 *            : boolean : appends to the end of the file
	 *
	 * @return : IOFile : this file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile writeBytes(final byte[] source, final boolean append) throws IOException {
		try {
			this.getParentDirectory().create();
			Utils.write(source, this);
		} catch (final IOException ex) {
			throw new IOException(ex);
		}

		return this;
	}

	/**
	 * writes a ByteBuffer to a file (possibly overwrites the file)
	 *
	 * @param source
	 *            : ByteBuffer : byte buffer
	 *
	 * @return : IOFile : this file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile writeBytes(final ByteBuffer source) throws IOException {
		return this.writeBytes(source, false);
	}

	/**
	 * writes a ByteBuffer to a file (possibly overwrites the file)
	 *
	 * @param source
	 *            : ByteBuffer : byte buffer
	 * @param append
	 *            : boolean : append to the end of an existing file or create a new
	 *
	 * @return : IOFile : this file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile writeBytes(final ByteBuffer source, final boolean append) throws IOException {
		// Set to true if the bytes should be appended to the file;
		// set to false if the bytes should replace current bytes
		// (if the file exists)
		try {
			FileOutputStream fo = new FileOutputStream(this, append);
			FileChannel wChannel = fo.getChannel(); // Create a writable file channel
			wChannel.write(source); // Write the ByteBuffer contents; the bytes between the ByteBuffer's position and the limit is written to the file
			wChannel.close();
			fo.close();
		} catch (final IOException ex) {
			throw new IOException(ex);
		}

		return this;
	}

	/**
	 * writes an InputStream to a file (possibly overwrites the file)
	 *
	 * @param source
	 *            : InputStream : source
	 *
	 * @return : IOFile : this file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile writeBytes(final InputStream source) throws IOException {
		try {
			this.getParentDirectory().create();
			Utils.copy(source, new FileOutputStream(this));
		} catch (IOException e) {
			throw new IOException(e);
		}

		return this;
	}

	/**
	 * writes a String to a file (possibly overwrites the file)
	 *
	 * @param source
	 *            : String : source
	 *
	 * @return : IOFile : this file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile writeBytes(final String source) throws IOException {
		return this.writeBytes(source, false);
	}

	/**
	 * writes a String to a file (possibly overwrites the file)
	 *
	 * @param source
	 *            : String : source
	 * @param append
	 *            : boolean : appends to the end of the file
	 *
	 * @return : IOFile : this file
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IOFile writeBytes(final String source, boolean append) throws IOException {
		return this.writeBytes(source.getBytes(), append);
	}

}
