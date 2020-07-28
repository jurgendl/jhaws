package org.jhaws.common.web.wicket.forms.common;

// https://developer.snapappointments.com/bootstrap-select/examples/
@SuppressWarnings("serial")
public class BootstrapMultiSelectSettings extends AbstractSelectSettings<BootstrapMultiSelectSettings> {
	private Integer max = null;

	private Integer size = null;

	public BootstrapMultiSelectSettings() {
		super();
	}

	public BootstrapMultiSelectSettings(BootstrapMultiSelectSettings other) {
		super(other);
	}

	public BootstrapMultiSelectSettings(boolean required) {
		super(required);
	}

	@Override
	public boolean isNullValid() {
		return this.nullValid;
	}

	@Override
	public BootstrapMultiSelectSettings setNullValid(boolean nullValid) {
		this.nullValid = nullValid;
		return this;
	}

	public Integer getMax() {
		return this.max;
	}

	public BootstrapMultiSelectSettings setMax(Integer max) {
		this.max = max;
		return this;
	}

	public Integer getSize() {
		return this.size;
	}

	public BootstrapMultiSelectSettings setSize(Integer size) {
		this.size = size;
		return this;
	}
}
