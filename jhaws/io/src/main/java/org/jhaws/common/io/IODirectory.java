package org.jhaws.common.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileSystemView;

import org.jhaws.common.io.filter.OnlyDirectoriesFileFilter;
import org.jhaws.common.io.filter.OnlyFilesFilter;

/**
 * functionality that java.io.File lacks for directories only.<br>
 *
 * @author Jurgen
 * @version 29 January 2007
 *
 * @see IOGeneralFile
 * @see IODirectory
 */
@Deprecated
@SuppressWarnings("deprecation")
public class IODirectory extends IOGeneralFile<IODirectory> {
	private static final long serialVersionUID = 100001010000000001L;

	/** join function parameter */
	public static final int DEFAULTS = 0;

	/** join function parameter */
	public static final int RECURSIVE = 1;

	/** join function parameter */
	public static final int RENAME_EXISTING = 2;

	/** join function parameter */
	public static final int OVERWRITE_WHEN_NEWER = 4;

	/** join function parameter */
	public static final int OVERWRITE_ALWAYS = 12;

	/** join function parameter */
	public static final int DELETE_SOURCE = 16;

	/** join function parameter */
	public static final int FLATTEN = 32;

	/** temp dir */
	public static final IODirectory TEMPDIR = new IODirectory(System.getProperty("java.io.tmpdir"));

	/** desktop */
	public static final IODirectory DESKTOPDIR = new IODirectory(FileSystemView.getFileSystemView().getHomeDirectory());

	/** user home dir */
	public static final IODirectory USERDIR = new IODirectory(System.getProperty("user.home"));

	private static void _listIODirectoriesRecursive(final IODirectory d, final Vector<IODirectory> v, final FileFilter ff) {
		IODirectory[] f = d.listIODirectories();

		for (IODirectory element : f) {
			if (ff.accept(element)) {
				v.add(element);
			}

			IODirectory._listIODirectoriesRecursive(element, v, ff);
		}
	}

	/**
	 * internal use only
	 *
	 * @param v
	 *            NA
	 * @param dir
	 *            NA
	 * @param ff
	 *            NA
	 */
	protected static void _listIOFilesRecursive(final Vector<IOFile> v, final IODirectory dir, final FileFilter ff) {
		if (dir.isHidden()) {
			return;
		}

		IOFile[] files = dir.listIOFiles(ff);

		for (IOFile file : files) {
			v.add(file);
		}

		IODirectory[] subd = dir.listIODirectories();

		for (IODirectory element : subd) {
			IODirectory._listIOFilesRecursive(v, element, ff);
		}
	}

	/**
	 * DOC_ME
	 *
	 * @return DOC_ME
	 */
	public static IODirectory getCurrentDrive() {
		return new IODirectory().getDrive();
	}

	/**
	 * na
	 *
	 * @return
	 */
	public static List<IODirectory> getDrives() {
		List<IODirectory> drives = new ArrayList<>();
		FileSystemView fsv = FileSystemView.getFileSystemView();

		// windows
		if (System.getProperty("os.name").toLowerCase().indexOf("win") != -1) { //$NON-NLS-1$ //$NON-NLS-2$

			String systemDriveString = System.getProperty("user.home").substring(0, 2) + File.separator; //$NON-NLS-1$
			File systemDriveFile = new File(systemDriveString);
			File parentSystemDrive = fsv.getParentDirectory(systemDriveFile);

			for (File f : parentSystemDrive.listFiles()) {
				try {
					if (fsv.isFileSystem(f) && fsv.isDrive(f) && fsv.isTraversable(f) && !fsv.isFloppyDrive(f) && (f.listFiles().length > 0)) {
						if (new IODirectory(f).getParent() == null) {
							drives.add(new IODirectory(f));
						}
					}
				} catch (Throwable e) {
					// not accesible
				}
			}
		} else {
			// unix
			for (File f : fsv.getRoots()) {
				drives.add(new IODirectory(f));
			}
		}

		return drives;
	}

	/**
	 * gets directory separator
	 *
	 * @return ':' on Unix, ' ' on Windows
	 */
	public static char getSeparator() {
		return System.getProperty("path.separator").charAt(0); //$NON-NLS-1$
	}

	/**
	 * return the temp dir (singleton)
	 *
	 * @return IODirectory
	 */
	public static IODirectory getTempDir() {
		return IODirectory.TEMPDIR;
	}

	/**
	 * returns the user desktop dir (singleton)
	 *
	 * @return IODirectory
	 */
	public static IODirectory getUserDesktopDir() {
		return IODirectory.DESKTOPDIR;
	}

	/**
	 * returns the user home dir (singleton)
	 *
	 * @return IODirectory
	 */
	public static IODirectory getUserDir() {
		return IODirectory.USERDIR;
	}

	/**
	 * return a subdirectory in the temp dir
	 *
	 * @param relativeName
	 *            relative name in temp dir
	 *
	 * @return IODirectory
	 */
	public static IODirectory newTempDir(String relativeName) {
		return new IODirectory(IODirectory.getTempDir(), relativeName);
	}

	/**
	 * return a subdirectory in the user home dir
	 *
	 * @param relativeName
	 *            relative name in user home dir
	 *
	 * @return IODirectory
	 */
	public static IODirectory newUserDir(String relativeName) {
		return new IODirectory(IODirectory.getUserDir(), relativeName);
	}

	/**
	 * Creates a new IODirectory object.<br>
	 * THIS (working) directory
	 */
	public IODirectory() {
		super(new IODirectory(".").getAbsolutePath()); //$NON-NLS-1$
		try {
			this.checkIfDirectory();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Creates a new IoDirectory object.
	 *
	 * @param file
	 *            : File : File object
	 */
	public IODirectory(final File file) {
		super(file);
		try {
			this.checkIfDirectory();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Creates a new IODirectory object.
	 *
	 * @param parent
	 *            : File : directory
	 * @param child
	 *            : String : short name of subdirectory
	 */
	public IODirectory(final File parent, final String child) {
		super(parent, child);
		try {
			this.checkIfDirectory();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Creates a new IODirectory object.
	 *
	 * @param pathname
	 *            : String : full path name of directory; when only a name is given, the directory will be in the current working directory
	 */
	public IODirectory(final String pathname) {
		super(pathname);
		try {
			this.checkIfDirectory();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Creates a new IODirectory object.
	 *
	 * @param parent
	 *            : String : directory
	 * @param child
	 *            : String : short name of subdirectory
	 */
	public IODirectory(final String parent, final String child) {
		this(new IODirectory(parent), child);
		try {
			this.checkIfDirectory();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * checks for parameter, throws exception if needed
	 *
	 * @return
	 *
	 * @throws ObjectIsNoDirectoryException
	 *             : thrown exception
	 */
	protected IODirectory checkIfDirectory() throws IOException {
		if (this.exists()) {
			if (!this.isDirectory()) {
				throw new IOException(this.getAbsolutePath());
			}
		}

		return this;
	}

	/**
	 * compares a directory and all it's subdirectories with their files with this directory, sends output to System.out
	 *
	 * @param dir
	 *            : IODirectory : directory to compare with
	 * @param listener
	 *            : IODirectoryCompareListener : results of comparing are passed to an implementation of this inteface
	 *
	 * @return this
	 */
	public IODirectory compare(final IODirectory dir, final IODirectoryCompareListener listener) throws FileNotFoundException {
		this.checkExistence();

		File[] myChildren = this.listFiles();
		File[] hisChildren = dir.listFiles();
		HashMap<String, File> myMap = new HashMap<>();
		HashMap<String, File> hisMap = new HashMap<>();

		for (File element : myChildren) {
			myMap.put(element.getName(), element);
		}

		for (File element : hisChildren) {
			hisMap.put(element.getName(), element);
		}

		Iterator<String> keys = myMap.keySet().iterator();

		while (keys.hasNext()) {
			String key = keys.next().toString();
			Object o = myMap.get(key);
			File file = (File) o;

			if (hisMap.containsKey(key)) {
				Object oo = hisMap.get(key);

				if (file.isDirectory()) {
					IODirectory mydir = new IODirectory(file);

					try {
						IODirectory otherdir = new IODirectory((File) oo);
						mydir.compare(otherdir, listener);
					} catch (final FileNotFoundException ex) {
						listener.shouldBeADirectory(new File(dir, file.getName()), file);
					}
				} else {
					IOFile myfile = new IOFile(file);

					try {
						IOFile otherfile = new IOFile((File) oo);
						boolean same = false;

						same = myfile.compare2(otherfile);

						if (same) {
							listener.same(otherfile, file);
						} else {
							listener.differs(otherfile, file);
						}
					} catch (final IOException ex) {
						listener.shouldBeAFile(new File(dir, file.getName()), file);
					}
				}

				hisMap.remove(key);
			} else {
				if (file.isDirectory()) {
					listener.dirDoesNotExists(new File(dir, file.getName()), file);
				} else {
					listener.doesNotExists(new File(dir, file.getName()), file);
				}
			}
		}

		Iterator<String> keys2 = hisMap.keySet().iterator();

		while (keys2.hasNext()) {
			String key = keys2.next().toString();
			Object o = hisMap.get(key);
			File file = (File) o;
			listener.extra(file);
		}

		return this;
	}

	/**
	 * does this directory contains given file at any level
	 *
	 * @param dir
	 *            IOFfile
	 *
	 * @return boolean
	 */
	public boolean contains(final IODirectory dir) {
		return dir.getAbsolutePath().startsWith(this.getAbsolutePath());
	}

	/**
	 * does this directory contains given file at any level
	 *
	 * @param file
	 *            IOFfile
	 *
	 * @return boolean
	 */
	public boolean contains(final IOFile file) {
		return file.getAbsolutePath().startsWith(this.getAbsolutePath());
	}

	/**
	 * does this directory contains given file at first level
	 *
	 * @param dir
	 *            IOFfile
	 *
	 * @return boolean
	 */
	public boolean containsDirectly(final IODirectory dir) {
		return dir.getParentDirectory().getAbsolutePath().equals(this.getAbsolutePath());
	}

	/**
	 * does this directory contains given file at first level
	 *
	 * @param file
	 *            IOFfile
	 *
	 * @return boolean
	 */
	public boolean containsDirectly(final IOFile file) {
		return file.getParentDirectory().getAbsolutePath().equals(this.getAbsolutePath());
	}

	/**
	 * copies a complete directory to another directory (binary copy), does not overwrite, does not delete source, always recursive
	 *
	 * @param destinationDir
	 *            : File : target directory
	 *
	 * @return : IODirectory : destination
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IODirectory copy2(final IODirectory destinationDir) throws IOException {
		return this.copy2(destinationDir, IODirectory.DEFAULTS);
	}

	/**
	 * copies a complete directory to another directory (binary copy), does not delete source, always recursive, use parameters to define when to overwrite
	 *
	 * @param destination
	 *            : IODirectory : destinationDir directory
	 * @param parameter
	 *            : int : OVERWRITE_ALWAYS, OVERWRITE_WHEN_NEWER, OVERWRITE_WHEN_LARGER, RENAME_EXISTING
	 *
	 * @return : IODirectory : destination directory
	 *
	 * @throws IOException
	 *             : thrown exception
	 */
	public IODirectory copy2(final IODirectory destination, final int parameter) throws IOException {
		return destination.join(this, (parameter | IODirectory.RECURSIVE) & ~IODirectory.DELETE_SOURCE, -1);
	}

	/**
	 * calls mkdirs();
	 *
	 * @return this
	 */
	public IODirectory create() {
		this.mkdirs();
		return this;
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
	 * @see java.io.File#delete()
	 */
	public boolean delete0() {
		return super.delete();
	}

	/**
	 * removes all doubles from directory checking the size, then the the first bytes from the file (bounded by MAX_COMPARE_SIZE) first and then if equal, the complete file
	 * (bounded by 16MB) (optimized for speed)
	 *
	 * @return : Vector : vector of deleted files
	 */
	public Vector<?> deleteDoubles() {
		return this.deleteDoubles(16 * 1024 * 1024, null);
	}

	/**
	 * removes all doubles from directory checking the size, then the the first bytes from the file (bounded by MAX_COMPARE_SIZE) first and then if equal, the complete file
	 * (bounded by superSize) (optimized for speed)
	 *
	 * @param superSize
	 *            : int : compare size when file match first block (can be set to zero to suppress second check)
	 *
	 * @return : Vector : vector of deleted files
	 */
	public Vector<?> deleteDoubles(final int superSize) {
		return this.deleteDoubles(superSize, null);
	}

	/**
	 * removes all doubles from directory checking the size, then the the first bytes from the file (bounded by MAX_COMPARE_SIZE) first and then if equal, the complete file
	 * (bounded by superSize) (optimized for speed)
	 *
	 * @param superSize
	 *            : int : compare size when file match first block (can be set to zero to suppress second check)
	 * @param trashCanDir
	 *            : IODirectory : directory to put trash to (null permanently removes files) (see function checkFileIndex for naming of moved files)
	 *
	 * @return : Vector : vector of deleted files
	 */
	public Vector<?> deleteDoubles(final int superSize, final IODirectory trashCanDir) {
		try {
			this.checkExistence();
		} catch (FileNotFoundException ex1) {
			throw new RuntimeException(ex1);
		}

		IOFile[] children = this.listIOFiles();
		Arrays.sort(children, IOGeneralFile.fileSizeComparator);

		Vector<IOFile> deleted = new Vector<>();

		IODirectory oldTrashCan = null;

		if (trashCanDir != null) {
			oldTrashCan = RecyclerFactory.getDefaultRecycleDirectory();
			RecyclerFactory.setDefaultRecycleDirectory(trashCanDir);
		}

		for (int i = 0; i < children.length; i++) {
			boolean comp = true;

			for (int j = i - 1; comp && (j >= 0); j--) {
				if (children[j].exists() && (children[j].length() > 0)) {
					if (children[i].length() != children[j].length()) {
						comp = false;
					} else {
						try {
							IOFile child1 = new IOFile(children[i]);
							IOFile child2 = new IOFile(children[j]);

							if (child1.compare2(child2)) {
								if (child1.compare2(child2, superSize)) {
									if (trashCanDir == null) {
										if (child1.lastModified() < child2.lastModified()) {
											deleted.add(child1);
											child1.erase();
										} else {
											deleted.add(child2);
											child2.erase();
										}
									} else {
										if (child1.lastModified() < child2.lastModified()) {
											deleted.add(child1);
											RecyclerFactory.recycle(child1);
										} else {
											deleted.add(child2);
											RecyclerFactory.recycle(child2);
										}
									}
								}
							}
						} catch (final IOException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		}

		if (trashCanDir != null) {
			RecyclerFactory.setDefaultRecycleDirectory(oldTrashCan);
		}

		return deleted;
	}

	/**
	 * removes all doubles from directory checking the size, then the the first bytes from the file (bounded by MAX_COMPARE_SIZE) first and then if equal, the complete file
	 * (bounded by 16MB) (optimized for speed)
	 *
	 * @param trashCanDir
	 *            : IODirectory : directory to put trash to (null permanently removes files)
	 *
	 * @return : Vector : vector of deleted files
	 */
	public Vector<?> deleteDoubles(final IODirectory trashCanDir) {
		return this.deleteDoubles(16 * 1024 * 1024, trashCanDir);
	}

	/**
	 * na
	 *
	 * @param relativePath
	 *            na
	 *
	 * @return
	 */
	public IODirectory dir(String relativePath) {
		return new IODirectory(this, relativePath);
	}

	/**
	 * erases a complete directory (no warning)
	 *
	 * @return : boolean : success
	 */
	public boolean erase() {
		if (!this.exists()) {
			return true;
		}

		for (IOFile subfiles : this.listIOFiles()) {
			subfiles.erase();
		}

		for (IODirectory subdirs : this.listIODirectories()) {
			subdirs.erase();
		}

		return super.delete();
	}

	/**
	 * na
	 *
	 * @param relativePath
	 *            na
	 *
	 * @return
	 */
	public IOFile file(String relativePath) {
		return new IOFile(this, relativePath);
	}

	/**
	 * flattens this directory, copies all files in all subdirectories to this directory, do not copy doubles, rename if file already exists and isn't the same, delete all
	 * subdirectories afterwards
	 *
	 * @return this
	 *
	 * @throws IOException
	 *             : thrown exceptions
	 */
	public IODirectory flatten() throws IOException {
		IODirectory[] iod = this.listIODirectories();

		for (IODirectory element : iod) {
			this.join(element, IODirectory.DEFAULTS | IODirectory.DELETE_SOURCE | IODirectory.FLATTEN | IODirectory.RENAME_EXISTING | IODirectory.RECURSIVE, 9999999);
		}

		return this;
	}

	/**
	 * returns this object
	 *
	 * @return : IODirectory : this object
	 */
	public IODirectory getDirectory() {
		return this;
	}

	/**
	 * get Drive for this file (windows only)
	 *
	 * @return
	 */
	public IODirectory getDrive() {
		String absolutePath = this.getAbsolutePath();

		return new IODirectory(absolutePath.substring(0, absolutePath.indexOf(':') + 1));
	}

	/**
	 * returns the total size of all files in this directory
	 *
	 * @return : long : total files size
	 */
	@Override
	public long getLength() {
		long total = 0L;
		IOFile[] f = this.listIOFiles();

		for (IOFile element : f) {
			total += element.getLength();
		}

		return total;
	}

	/**
	 * returns the total siza of all files in this directory and it's subdirectories
	 *
	 * @return : long : total files size
	 */
	public long getRecursiveLength() {
		long total = this.getLength();

		IODirectory[] d = this.listIODirectories();

		for (IODirectory element : d) {
			total += element.getRecursiveLength();
		}

		return total;
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
	 * used by copy, move, etc. functions or atomic use, don't forget that the current directory is the TARGET unlike other functions where the GIVEN dir is the target
	 *
	 * @param source
	 *            : IODirectory : target directory
	 * @param modifier
	 *            : int : combination (with |) of RECURSIVE, (RENAME_EXISTING or ALWAYS_OVERWRITE or OVERWRITE_WHEN_NEWER or OVERWRITE_WHEN_LARGER in this order of importance ),
	 *            DELETE_SOURCE
	 *
	 * @return : IODirectory : this object
	 *
	 * @throws IOException
	 *             : when something fails
	 */
	public IODirectory join(final IODirectory source, final int modifier) throws IOException {
		return this.join(source, modifier, IOFile.MAX_COMPARE_SIZE);
	}

	/**
	 * used by copy, move, etc. functions or atomic use, don't forget that the current directory is the TARGET unlike other functions where the GIVEN dir is the target
	 *
	 * @param source
	 *            : IODirectory : target directory
	 * @param modifier
	 *            : int : combination (with |) of RECURSIVE, (RENAME_EXISTING or ALWAYS_OVERWRITE or OVERWRITE_WHEN_NEWER or OVERWRITE_WHEN_LARGER in this order of importance ),
	 *            DELETE_SOURCE
	 * @param maxcomparesize
	 *            : int : maximum size to compare
	 *
	 * @return : IODirectory : this object
	 *
	 * @throws IOException
	 *             : when something fails
	 */
	public IODirectory join(final IODirectory source, final int modifier, final int maxcomparesize) throws IOException {
		source.checkExistence();

		this.join0(source, modifier, maxcomparesize);

		return this;
	}

	/**
	 * used by join function <br>
	 * <br>
	 *
	 * <pre>
	 * 0 0 0 0 0 0 0 0 (defaults)
	 *  - - - - - - - 1 (recursive)
	 *  - - - - - - 1 - (rename when file already exists)
	 *  - - - - - 1 - - (overwrite when newer)
	 *  - - - - 1 1 - - (always overwrite)
	 *  - - - 1 - - - - (delete source - move action) - - 1 - - - - - (flatten)
	 *  - 0 - - - - - - (not used, future parameter)
	 *  0 - - - - - - - (not used, future parameter)
	 * </pre>
	 *
	 * <br>
	 * <br>
	 * if renaming: all overwrite options will be false (___?001?<br>
	 *
	 * @param source
	 *            : IODirectory : target directory
	 * @param modifier
	 *            : int : combination (with |) of RECURSIVE, (RENAME_EXISTING or ALWAYS_OVERWRITE or OVERWRITE_WHEN_NEWER or in this order of importance ), DELETE_SOURCE
	 * @param maxcomparesize
	 *            : int : maximum size to compare
	 *
	 * @throws IOException
	 *             : when something fails
	 */
	protected void join0(final IODirectory source, final int modifier, final int maxcomparesize) throws IOException {
		// System.out.println("copying/moving everything from " + source + " to
		// " + this + ", modifier=" + modifier + ", maxcomparesize=" +
		// maxcomparesize); //$NON-NLS-1$
		// //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		boolean recursive = (modifier & IODirectory.RECURSIVE) == IODirectory.RECURSIVE;
		boolean delete = (modifier & IODirectory.DELETE_SOURCE) == IODirectory.DELETE_SOURCE;
		boolean rename = (modifier & IODirectory.RENAME_EXISTING) == IODirectory.RENAME_EXISTING;
		boolean flatten = (modifier & IODirectory.FLATTEN) == IODirectory.FLATTEN;

		boolean overwritenewer = false;
		boolean overwritealways = false;

		if (!rename) {
			overwritenewer = (modifier & IODirectory.OVERWRITE_WHEN_NEWER) == IODirectory.OVERWRITE_WHEN_NEWER;
			overwritealways = (modifier & IODirectory.OVERWRITE_ALWAYS) == IODirectory.OVERWRITE_ALWAYS;
		}

		// System.out.println("recursive=" + recursive + ", rename=" + rename +
		// ", overwritenewer=" + overwritenewer + ", overwritealways=" +
		// overwritealways + ", delete=" +
		// delete); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		// //$NON-NLS-5$

		this.create();

		for (IOFile subfile : source.listIOFiles()) {
			// System.out.println("join0(IODirectory, int, int) - subfile=" +
			// subfile); //$NON-NLS-1$

			IOFile target = new IOFile(this, subfile.getName());

			if (target.exists() && (maxcomparesize != -1)) {
				if (target.compare2(subfile, maxcomparesize)) {
					if (delete) {
						subfile.erase();
					}
				} else {
					subfile.move_copy(target, delete, overwritealways, overwritenewer, false, rename);
				}
			} else {
				subfile.move_copy(target, delete, overwritealways, overwritenewer, false, rename);
			}
		}

		if (recursive) {
			for (IODirectory subdir : source.listIODirectories()) {
				// System.out.println("join0(IODirectory, int, int) - subdir=" +
				// subdir); //$NON-NLS-1$

				if (flatten) {
					this.join0(subdir, modifier, maxcomparesize);
				} else {
					new IODirectory(this, subdir.getName()).join0(subdir, modifier, maxcomparesize);
				}
			}
		}

		java.io.File[] tmp = source.listFiles();

		if (delete && ((tmp == null) || (tmp.length == 0))) {
			/* boolean erased = */source.erase();
			// System.out.println("erasing " + source + " => " + erased);
			// //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * lists all existing subdirectories
	 *
	 * @return : IODirectory[] : all subdirectories
	 */
	public IODirectory[] listIODirectories() {
		File[] f = this.listFiles((FileFilter) new OnlyDirectoriesFileFilter());

		if (f == null) {
			return new IODirectory[0];
		}

		IODirectory[] d = new IODirectory[f.length];

		for (int i = 0; i < f.length; i++) {
			d[i] = new IODirectory(f[i]);
		}

		return d;
	}

	/**
	 * lists all existing subdirectories
	 *
	 * @param ff
	 *            : AbstractFileFilter : additional file filter
	 *
	 * @return : IODirectory[] : all subdirectories
	 */
	public IODirectory[] listIODirectories(final FileFilter ff) {
		File[] f = this.listFiles((FileFilter) new OnlyDirectoriesFileFilter());

		if (f == null) {
			return new IODirectory[0];
		}

		Vector<IODirectory> v = new Vector<>();

		for (File element : f) {
			if (ff.accept(element)) {
				v.add(new IODirectory(element));
			}
		}

		IODirectory[] d = new IODirectory[v.size()];
		Object[] o = v.toArray();

		for (int i = 0; i < o.length; i++) {
			d[i] = (IODirectory) o[i];
		}

		return d;
	}

	public IODirectory[] listIODirectoriesRecursive() {
		return this.listIODirectoriesRecursive(new OnlyDirectoriesFileFilter());
	}

	public IODirectory[] listIODirectoriesRecursive(final FileFilter ff) {
		Vector<IODirectory> v = new Vector<>();

		IODirectory._listIODirectoriesRecursive(this, v, ff);

		IODirectory[] d = new IODirectory[v.size()];
		Object[] o = v.toArray();

		for (int i = 0; i < o.length; i++) {
			d[i] = (IODirectory) o[i];
		}

		return d;
	}

	/**
	 * lists all existing files in this directories
	 *
	 * @return : IOFile[] : all files in this directory
	 */
	public IOFile[] listIOFiles() {
		File[] f = this.listFiles(new OnlyFilesFilter().castFileFilter());

		if (f == null) {
			return new IOFile[0];
		}

		IOFile[] d = new IOFile[f.length];

		for (int i = 0; i < f.length; i++) {
			d[i] = new IOFile(f[i]);
		}

		return d;
	}

	/**
	 * lists all existing subdirectories
	 *
	 * @param ff
	 *            : AbstractFileFilter : additional file filter
	 *
	 * @return : IODirectory[] : all subdirectories
	 */
	public IOFile[] listIOFiles(final FileFilter ff) {
		File[] files = this.listFiles((FileFilter) new OnlyFilesFilter());

		if (files == null) {
			return new IOFile[0];
		}

		Vector<IOFile> v = new Vector<>();

		for (File file : files) {
			if (ff.accept(file)) {
				v.add(new IOFile(file));
			}
		}

		IOFile[] f = new IOFile[v.size()];
		Object[] o = v.toArray();

		for (int i = 0; i < o.length; i++) {
			f[i] = (IOFile) o[i];
		}

		return f;
	}

	/**
	 * lists all existing files in this directories and its subdirectories
	 *
	 * @return : IOFile[] : all files
	 */
	public IOFile[] listIOFilesRecursive() {
		return this.listIOFilesRecursive(new OnlyFilesFilter());
	}

	/**
	 * lists all existing files in this directories and its subdirectories
	 *
	 * @param ff
	 *            : AbstractFileFilter : file filter
	 *
	 * @return : IOFile[] : all files
	 */
	public IOFile[] listIOFilesRecursive(final FileFilter ff) {
		Vector<IOFile> v = new Vector<>();
		IODirectory._listIOFilesRecursive(v, this, ff);

		IOFile[] files = new IOFile[v.size()];

		for (int i = 0; i < v.size(); i++) {
			Object o = v.get(i);
			files[i] = (IOFile) o;
		}

		return files;
	}

	public IOGeneralFile<?>[] listIOGeneralFile() {
		File[] f = this.listFiles();

		if (f == null) {
			return new IOGeneralFile[0];
		}

		IOGeneralFile<?>[] d = new IOGeneralFile[f.length];

		for (int i = 0; i < f.length; i++) {
			d[i] = f[i].isDirectory() ? new IODirectory(f[i]) : new IOFile(f[i]);
		}

		return d;
	}

	/**
	 * moves (copy & delete) a complete directory to another directory (binary copy), does never overwrite, always recursive
	 *
	 * @param destinationDir
	 *            : IODirectory : destinationDir directory
	 *
	 * @return : IODirectory : destinationDir directory
	 *
	 * @throws IOException
	 *             : IOException
	 */
	public IODirectory move2(final IODirectory destinationDir) throws IOException {
		return this.move2(destinationDir, IODirectory.DEFAULTS);
	}

	/**
	 * moves (copy & delete) a complete directory to another directory (binary copy), use parameters to define when to overwrite, always recursive
	 *
	 * @param destinationDir
	 *            : IODirectory : destinationDir directory
	 * @param parameter
	 *            : int : OVERWRITE_ALWAYS, OVERWRITE_WHEN_NEWER, OVERWRITE_WHEN_LARGER, RENAME_EXISTING
	 *
	 * @return : IODirectory : destinationDir directory
	 *
	 * @throws IOException
	 *             : IOException
	 */
	public IODirectory move2(final IODirectory destinationDir, final int parameter) throws IOException {
		return destinationDir.join(this, (parameter | IODirectory.RECURSIVE | IODirectory.DELETE_SOURCE), IOFile.MAX_COMPARE_SIZE);
	}

	/**
	 *
	 * @see util.io.jni.Recycler#recycle(java.io.File)
	 */
	public boolean recycle() {
		return RecyclerFactory.recycle(this);
	}

	/**
	 * recycle doubles in directory using the default trash directory or OS dependant recycling (if available)
	 *
	 * @return : Vector : vector of recycled files
	 */
	public Vector<?> recycleDoubles() {
		return this.deleteDoubles(16 * 1024 * 1024, new IODirectory(RecyclerFactory.getDefaultRecycleDirectory()));
	}

	/**
	 * recycle doubles in directory using the default trash directory or OS dependant recycling (if available)
	 *
	 * @param superSize
	 *            : int : compare size when file match first block (can be set to zero to suppress second check)
	 *
	 * @return : Vector : vector of recycled files
	 */
	public Vector<?> recycleDoubles(final int superSize) {
		return this.deleteDoubles(superSize, new IODirectory(RecyclerFactory.getDefaultRecycleDirectory()));
	}

	/**
	 * sync
	 *
	 * @param other
	 *
	 * @throws IOException
	 */
	public void sync(IODirectory other) throws IOException {
		this.sync(other, Pattern.compile("."), null);
	}

	/**
	 * sync
	 *
	 * @param other
	 * @param listener
	 *
	 * @throws IOException
	 */
	public void sync(IODirectory other, IOCopyListener listener) throws IOException {
		this.sync(other, Pattern.compile("."), listener);
	}

	/**
	 * sync
	 *
	 * @param other
	 * @param pattern
	 *
	 * @throws IOException
	 */
	public void sync(IODirectory other, Pattern pattern) throws IOException {
		this.sync(other, pattern, null);
	}

	/**
	 * sync
	 *
	 * @param other
	 * @param pattern
	 * @param listener
	 *
	 * @throws IOException
	 */
	public void sync(IODirectory other, Pattern pattern, IOCopyListener listener) throws IOException {
		this.syncOneWay(pattern, other, listener);
		other.syncOneWay(pattern, this, listener);
	}

	/**
	 * sync
	 *
	 * @param pattern
	 * @param other
	 * @param listener
	 *
	 * @throws IOException
	 */
	public void syncOneWay(Pattern pattern, IODirectory other, IOCopyListener listener) throws IOException {
		for (IOFile source : this.listIOFiles()) {
			IOFile target = new IOFile(other, source.getName());
			boolean matches = pattern.matcher(source.getName()).find();
			boolean newf = !target.exists();
			Date td = new Date(target.lastModified());
			Date sd = new Date(source.lastModified());
			boolean recent = td.before(sd);

			if (matches) {
				if (newf) {
					source.copy2(target, listener);
				} else if (recent) {
					boolean equal = source.compare2(target);

					if (!equal) {
						source.copy2(target, listener);
					}
				}
			}
		}

		for (IODirectory source : this.listIODirectories()) {
			IODirectory target = new IODirectory(other, source.getName());
			source.syncOneWay(pattern, target, listener);
		}
	}
}
