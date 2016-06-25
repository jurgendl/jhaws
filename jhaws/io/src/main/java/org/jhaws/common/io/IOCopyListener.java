package org.jhaws.common.io;

/**
 * IOCopyListener
 */
@Deprecated
public interface IOCopyListener {
	/**
	 * copyFailed
	 * 
	 * @param ioFile
	 * @param target
	 * @param ex
	 */
	public void copyFailed(IOFile ioFile, IOFile target, Exception ex);

	/**
	 * copyFinished
	 * 
	 * @param ioFile
	 * @param target
	 */
	public void copyFinished(IOFile ioFile, IOFile target);

	/**
	 * copyProgress
	 * 
	 * @param total
	 */
	public void copyProgress(long total);

	/**
	 * copyStarted
	 * 
	 * @param ioFile
	 * @param target
	 */
	public void copyStarted(IOFile ioFile, IOFile target);
}
