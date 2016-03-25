package org.swingeasy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.itextpdf.text.DocumentException;

/**
 * @author Jurgen
 */
public abstract class EComponentExporterImpl<T extends JComponent & EComponentI> implements EComponentStreamExporter<T> {
	public static <T extends JComponent & EComponentI> void exportHtmlToPdf(EComponentStreamExporter<T> exporter, T component, OutputStream out) throws IOException {
		try {
			Stream stream = StreamFactory.create();
			exporter.exportStream(component, stream.getOutputStream());
			ITextRenderer itextRenderer = new ITextRenderer();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(stream.getInputStream());
			itextRenderer.setDocument(doc, null);
			itextRenderer.layout();
			itextRenderer.createPDF(out);
			itextRenderer.finishPDF();
			out.close();
		} catch (RuntimeException ex) {
			throw ex;
		} catch (ParserConfigurationException ex) {
			throw new RuntimeException(ex);
		} catch (SAXParseException ex) {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			exporter.exportStream(component, bout);
			throw new RuntimeException("line " + ex.getLineNumber() + ", column " + ex.getColumnNumber() + "\n\n" + new String(bout.toByteArray()), ex);
		} catch (SAXException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} catch (DocumentException ex) {
			throw new RuntimeException(ex);
		}
	}

	protected EComponentExporterFileChooserCustomizer<T> customizer;

	protected String overwriteWarningMessage = "EComponentExporter.overwrite.warning.message";

	protected String overwriteWarningTitle = "EComponentExporter.overwrite.warning.title";

	protected String completionMessage = "EComponentExporter.completion.message";

	protected String completionTitle = "EComponentExporter.completion.title";

	protected volatile byte[] style;

	public EComponentExporterImpl() {
		this.customizer = new EComponentExporterFileChooserCustomizer<T>(this);
	}

	protected boolean canOverwrite(T component) {
		if (ResultType.OK != CustomizableOptionPane.showCustomDialog(component, new JLabel(Messages.getString((Locale) null, this.overwriteWarningMessage)),
				Messages.getString((Locale) null, this.overwriteWarningTitle), MessageType.WARNING, OptionType.OK_CANCEL, null, new CenteredOptionPaneCustomizer())) {
			return false;
		}
		return true;
	}

	public void export(T component) {
		try {
			File exportTo = CustomizableOptionPane.showFileChooserDialog(component, FileChooserType.SAVE, this.customizer);
			if (exportTo != null) {
				final String ext = this.getFileExtension();
				if (!exportTo.getName().endsWith(ext)) {
					exportTo = new File(exportTo.getParentFile(), exportTo.getName() + "." + ext);
				}
				if (exportTo.exists()) {
					if (!this.canOverwrite(component)) {
						return;
					}
				}
				Stream stream = StreamFactory.create();
				this.exportStream(component, stream.getOutputStream());
				try (InputStream data = stream.getInputStream(); FileOutputStream fout = new FileOutputStream(exportTo)) {
					byte[] buffer = new byte[1024 * 4];
					int read;
					while ((read = data.read(buffer)) != -1) {
						fout.write(buffer, 0, read);
					}
					data.close();
					fout.close();
					EComponentExporterFileChooserCustomizer.lastFile = exportTo;
					this.whenDone(component);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see org.swingeasy.EComponentStreamExporter#exportStream(javax.swing.JComponent, java.io.OutputStream)
	 */
	@Override
	public abstract void exportStream(T component, OutputStream out) throws IOException;

	public abstract String getFileExtension();

	public Icon getIcon() {
		return UIUtils.getIconForFileType(this.getFileExtension());
	}

	protected byte[] getStyle() throws IOException {
		if (this.style == null) {
			this.style = Resources.getDataResource("exporter/css.txt");
		}
		return this.style;
	}

	protected void whenDone(T component) {
		CustomizableOptionPane.showCustomDialog(component, new JLabel(Messages.getString((Locale) null, this.completionMessage)),
				Messages.getString((Locale) null, this.completionTitle), MessageType.INFORMATION, OptionType.OK, null, new CenteredOptionPaneCustomizer());
	}
}
