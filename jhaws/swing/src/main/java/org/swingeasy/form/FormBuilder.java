package org.swingeasy.form;

import java.awt.Component;
import java.awt.Container;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.swingeasy.ELabel;
import org.swingeasy.HasValue;
import org.swingeasy.validation.EValidationPane;
import org.swingeasy.validation.ValidationFactory;
import org.swingeasy.validation.Validator;

import net.miginfocom.swing.MigLayout;

/**
 * @author Jurgen
 */
public class FormBuilder {
	public static enum HAlign {
		left, right, center;
	}

	public static enum VAlign {
		top, bottom, right, center;
	}

	protected static final String UNRELATED = "10px";

	protected final Container container;

	protected final EValidationPane validationPane;

	/** wanneer columnWidths is gezet wordt cols = columnWidths.length */
	protected final Integer cols;

	protected boolean initialized = false;

	protected boolean debug = false;

	protected String minimum = "25pt";

	protected ValidationFactory validationFactory;

	/**
	 * absoluut: bv 50,50,*,50 = 50 pixels,50 pixels,de rest,50 pixels<br>
	 * relatief: bv 1,2,3,4 = 1x, 2x, 3x, 4x grootte<br>
	 * wordt ook door {@link #setColumnLayout(String)} opgevuld
	 */
	protected final String[] columnWidths;

	public FormBuilder(int cols) {
		container = new JPanel();
		validationPane = new EValidationPane(new JPanel());
		this.cols = cols;
		columnWidths = null;
	}

	public FormBuilder(String[] columnWidths) {
		container = new JPanel();
		validationPane = new EValidationPane(new JPanel());
		cols = null;
		this.columnWidths = columnWidths;
	}

	@SuppressWarnings({ "unchecked" })
	public <T> FormBuilder addComponent(String label, JComponent component, int colspan, int rowspan, HAlign halign, VAlign valign, List<Validator<T>> validators) {
		this.debug(label);
		ELabel labelComponent = new ELabel(label);
		labelComponent.setLabelFor(component);
		addToContainer(labelComponent, "spany " + rowspan);
		StringBuilder sb = new StringBuilder("growx, growy").append(", spanx ").append(colspan * 2 - 1).append(", spany ").append(rowspan);
		if (halign != null) {
			sb.append(", alignx ").append(halign.name());
		}
		if (valign != null) {
			sb.append(", aligny ").append(valign.name());
		}
		addToContainer(component, this.debug(sb.toString()));

		if (validators != null && validators.size() > 0 && component instanceof HasValue) {
			getValidationFactory().install(validationPane, HasValue.class.cast(component), validators);
		}
		return this;
	}

	public <T> FormBuilder addComponent(String label, JComponent component, int colspan, int rowspan, List<Validator<T>> validators) {
		return this.addComponent(label, component, colspan, rowspan, null, null, validators);
	}

	public <T> FormBuilder addComponent(String label, JComponent component, int colspan, List<Validator<T>> validators) {
		return this.addComponent(label, component, colspan, 1, validators);
	}

	public <T> FormBuilder addComponent(String label, JComponent component, List<Validator<T>> validators) {
		return this.addComponent(label, component, 1, validators);
	}

	public FormBuilder addTitle(String title) {
		return this.addTitle(title, 1);
	}

	public FormBuilder addTitle(String title, int colspan) {
		this.debug(title);
		return addToContainer(new TitledLine(title), this.debug("growx, spanx " + colspan * 2));
	}

	protected FormBuilder addToContainer(Component component, String constraint) {
		if (!initialized) {
			container.setLayout(new MigLayout(this.debug(getMigLayoutContraints()), this.debug(getMigLayoutColumnContraints()), this.debug(getMigLayoutRowContraints())));
			initialized = true;
		}
		container.add(component, constraint);
		return this;
	}

	protected <T> T debug(T object) {
		if (debug) {
			System.out.println(object);
		}

		return object;
	}

	public Integer getCols() {
		return cols;
	}

	public String[] getColumnWidths() {
		return columnWidths;
	}

	public Container getContainer() {
		return container;
	}

	/**
	 * effectief aantal kolommen = columnWidths.length wanneer die niet null is (dus voorrang) of cols dewelke niet null kan zijn
	 */
	protected int getEffectiveColumnCount() {
		return columnWidths == null ? cols == null ? 2 : cols : columnWidths.length;
	}

	/**
	 * miglayout column-constraints
	 */
	protected String getMigLayoutColumnContraints() {
		int countStars = 0;
		int colnr = getEffectiveColumnCount();
		String[] grows = new String[colnr]; // om relatief velden tov elkaar te laten groeien (bv [1,2,3] (units) of [20,60,20] (percentages)
		String[] sizes = new String[colnr];
		if (columnWidths != null && columnWidths.length > 0) {
			for (String i : columnWidths) {
				if ("*".equals(i)) {
					countStars++;
				}
			}
			if (countStars == 0) {
				// relatieve nummers
				for (int i = 0; i < columnWidths.length; i++) {
					grows[i] = ",grow " + Integer.parseInt(columnWidths[i]);
					sizes[i] = "," + minimum + "::";
				}
			} else {
				// absolute nummers (pixels) en de rest relatief en evenredig
				for (int i = 0; i < columnWidths.length; i++) {
					if (!"*".equals(columnWidths[i])) {
						grows[i] = "";
						sizes[i] = "," + Integer.parseInt(columnWidths[i]) + "pt!";
					} else {
						grows[i] = ",grow " + String.valueOf(100 / countStars);
						sizes[i] = "";
					}
				}
			}
		} else {
			for (int i = 0; i < grows.length; i++) {
				grows[i] = ",grow 100";
				sizes[i] = "," + minimum + "::";
			}
		}

		// voor elke kolom (label & component paar)
		StringBuilder cc = new StringBuilder();
		for (int i = 0; i < colnr; i++) {
			// label met related gap (gerelateerd met component)
			cc.append("[min!]").append("rel");
			// component met unrelated gap (niet gerelateerd met volgend veld)
			cc.append("[shrink,fill").append(grows[i]).append(sizes[i]).append("]").append(FormBuilder.UNRELATED);
			// enkel voor de duidelijkheid om label&component pairs (=kolommen) duidelijker te laten uitkomen
			cc.append(" ");
		}
		return cc.toString();
	}

	/**
	 * miglayout layout-constraints
	 */
	protected String getMigLayoutContraints() {
		StringBuilder lc = new StringBuilder("insets ")// insets...
				.append(FormBuilder.UNRELATED).append(" ")// ...top: unrelated
				.append(FormBuilder.UNRELATED).append(" ") // ...left: unrelated
				.append(0).append(" ")// ...bottom: geen
				.append(FormBuilder.UNRELATED)// ...right: unrelated
				.append(", fillx") // componenten nemen altijd volledige ruimte in horizontaal
				.append(", wrap ").append(2 * cols); // wrap achter # (maw 2x aantal cols)
		if (debug) {
			lc.append(", debug");
		}
		return lc.toString();
	}

	/**
	 * miglayout row-constraints: bv ruimte tussen rows
	 */
	protected String getMigLayoutRowContraints() {
		// geen
		return "";
	}

	public ValidationFactory getValidationFactory() {
		if (validationFactory == null) {
			validationFactory = new ValidationFactory();
		}
		return validationFactory;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setValidationFactory(ValidationFactory validationFactory) {
		this.validationFactory = validationFactory;
	}
}
