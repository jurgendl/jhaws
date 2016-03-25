package org.swingeasy;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * @see http://docs.oracle.com/javase/tutorial/uiswing/dnd/together.html
 * @see http://www.javaworld.com/javaworld/jw-08-1999/jw-08-draganddrop.html
 * @see http://docs.oracle.com/javase/tutorial/uiswing/dnd/dropmodedemo.html
 * @author Jurgen
 */
public class EListTransferHandler<T> extends TransferHandler {
	private static final long serialVersionUID = 198002516353951169L;

	protected int[] indices = null;

	protected int addIndex = -1; // Location where items were added

	protected int addCount = 0; // Number of items added.

	protected int index;

	protected boolean insert;

	/**
	 * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
	 */
	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
		return this.canImportFlavor(transferFlavors);
	}

	/**
	 * @see javax.swing.TransferHandler#canImport(javax.swing.TransferHandler.TransferSupport)
	 */
	@Override
	public boolean canImport(TransferSupport support) {
		return this.canImportFlavor(support.getDataFlavors());
	}

	protected boolean canImportFlavor(DataFlavor[] transferFlavors) {
		for (DataFlavor df : transferFlavors) {
			if (df.equals(this.getLocalDataFlavor())) {
				return true;
			}
			if (df.equals(this.getRemoteDataFlavor())) {
				return true;
			}
			if (df.equals(this.getStringDataFlavor())) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected T[] castData(Object obj) {
		return (T[]) obj;
	}

	@SuppressWarnings("unchecked")
	protected EList<T> castEList(Component comp) {
		return (EList<T>) comp;
	}

	@SuppressWarnings("unchecked")
	protected Collection<EListRecord<T>> castEListRecordCollection(Object obj) {
		return (Collection<EListRecord<T>>) obj;
	}

	// If the remove argument is true, the drop has been
	// successful and it's time to remove the selected items
	// from the list. If the remove argument is false, it
	// was a Copy operation and the original list is left
	// intact.
	protected void cleanup(JComponent c, boolean remove) {
		if (remove && (this.indices != null)) {
			EList<T> source = this.castEList(c);
			@SuppressWarnings("unchecked")
			DefaultListModel<T> model = (DefaultListModel<T>) source.getModel();
			// If we are moving items around in the same list, we
			// need to adjust the indices accordingly, since those
			// after the insertion point have moved.
			if (this.addCount > 0) {
				for (int i = 0; i < this.indices.length; i++) {
					if (this.indices[i] > this.addIndex) {
						this.indices[i] += this.addCount;
					}
				}
			}
			for (int i = this.indices.length - 1; i >= 0; i--) {
				model.remove(this.indices[i]);
			}
		}
		this.indices = null;
		this.addCount = 0;
		this.addIndex = -1;
	}

	protected Transferable createLocalDataFlavor(final EListRecord<T>[] data) {
		final DataFlavor localDataFlavor = this.getLocalDataFlavor();
		Transferable local = new Transferable() {
			/**
			 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
			 */
			@Override
			public Object getTransferData(DataFlavor f) throws UnsupportedFlavorException, IOException {
				return data;
			}

			/**
			 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
			 */
			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { localDataFlavor };
			}

			/**
			 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
			 */
			@Override
			public boolean isDataFlavorSupported(DataFlavor f) {
				return localDataFlavor.equals(f);
			}
		};
		return local;
	}

	protected Transferable createRemoteDataFlavor(final T[] data) {
		final DataFlavor remoteDataFlavor = this.getRemoteDataFlavor();
		Transferable remote = new Transferable() {
			/**
			 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
			 */
			@Override
			public Object getTransferData(DataFlavor f) throws UnsupportedFlavorException, IOException {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
					oos.writeObject(data);
					oos.close();
					return new ByteArrayInputStream(baos.toByteArray());
				}
			}

			/**
			 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
			 */
			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { remoteDataFlavor };
			}

			/**
			 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
			 */
			@Override
			public boolean isDataFlavorSupported(DataFlavor f) {
				return remoteDataFlavor.equals(f);
			}
		};
		return remote;
	}

	protected StringSelection createStringFlavor(final T[] data) {
		return new StringSelection(Arrays.toString(data));
	}

	/**
	 * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
	 */
	@Override
	protected Transferable createTransferable(JComponent comp) {
		EList<T> list = this.castEList(comp);
		Collection<EListRecord<T>> selectedRecords = list.getSelectedRecords();
		T[] data = this.castData(new Object[selectedRecords.size()]);
		int i = 0;
		for (EListRecord<T> record : selectedRecords) {
			data[i++] = record.get();
		}
		@SuppressWarnings("unchecked")
		Transferable local = this.createLocalDataFlavor(selectedRecords.toArray(new EListRecord[0]));
		Transferable remote = this.createRemoteDataFlavor(data);
		Transferable string = this.createStringFlavor(data);
		DelegatingTransferable delegatingTransferable = new DelegatingTransferable(local, remote, string);
		return delegatingTransferable;
	}

	/**
	 * @see javax.swing.TransferHandler#exportDone(javax.swing.JComponent, java.awt.datatransfer.Transferable, int)
	 */
	@Override
	protected void exportDone(JComponent c, Transferable data, int action) {
		this.cleanup(c, action == TransferHandler.MOVE);
	}

	// Bundle up the selected items in the list
	// as a single string, for export.
	protected String exportString(EList<T> list) {
		this.indices = list.getSelectedIndices();
		@SuppressWarnings("deprecation")
		Object[] values = list.getSelectedValues();
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			Object val = values[i];
			buff.append(val == null ? "" : val.toString());
			if (i != (values.length - 1)) {
				buff.append("\n");
			}
		}

		return buff.toString();
	}

	protected DataFlavor getLocalDataFlavor() {
		try {
			return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	protected DataFlavor getRemoteDataFlavor() {
		try {
			return new DataFlavor(DataFlavor.javaRemoteObjectMimeType);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
	 */
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}

	protected DataFlavor getStringDataFlavor() {
		return DataFlavor.stringFlavor;
	}

	/**
	 *
	 * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
	 */
	@Override
	public boolean importData(JComponent comp, Transferable t) {
		EList<T> elist = this.castEList(comp);
		Collection<EListRecord<T>> records = null;
		records = this.importLocalDataFlavor(t, records);
		if (records == null) {
			records = this.importRemoteDataFlavor(t, records);
		}
		if (records == null) {
			return false;
		}
		elist.removeRecords(records);
		int i = this.index;
		for (EListRecord<T> record : records) {
			elist.insertRecord(i, record);
		}
		return true;
	}

	/**
	 * @see javax.swing.TransferHandler#importData(javax.swing.TransferHandler.TransferSupport)
	 */
	@Override
	public boolean importData(TransferSupport support) {
		EList.DropLocation dl = (EList.DropLocation) support.getDropLocation();
		this.index = dl.getIndex();
		this.insert = dl.isInsert();
		return super.importData(support);
	}

	protected Collection<EListRecord<T>> importLocalDataFlavor(Transferable t, Collection<EListRecord<T>> records) {
		try {
			Object transferData = t.getTransferData(this.getLocalDataFlavor());
			@SuppressWarnings("unchecked")
			EListRecord<T>[] array = (EListRecord<T>[]) transferData;
			records = Arrays.asList(array);
		} catch (RuntimeException ex) {
			System.out.println(ex);
		} catch (UnsupportedFlavorException ex) {
			System.out.println(ex);
		} catch (IOException ex) {
			System.out.println(ex);
		}
		return records;
	}

	protected Collection<EListRecord<T>> importRemoteDataFlavor(Transferable t, Collection<EListRecord<T>> records) {
		try {
			InputStream is = InputStream.class.cast(t.getTransferData(this.getRemoteDataFlavor()));
			ObjectInputStream ois = new ObjectInputStream(is);
			Object transferData = ois.readObject();
			records = new ArrayList<EListRecord<T>>();
			for (T data : this.castData(transferData)) {
				records.add(this.newEListRecord(data));
			}
		} catch (RuntimeException ex) {
			System.out.println(ex);
		} catch (UnsupportedFlavorException ex) {
			System.out.println(ex);
		} catch (IOException ex) {
			System.out.println(ex);
		} catch (ClassNotFoundException ex) {
			System.out.println(ex);
		}
		return records;
	}

	protected EListRecord<T> newEListRecord(T data) {
		return new EListRecord<T>(data);
	}
}