package org.jhaws.common.web.wicket.forms.common;

// https://developer.snapappointments.com/bootstrap-select/examples/
@SuppressWarnings("serial")
public class BootstrapSelectSettings extends AbstractSelectSettings<BootstrapSelectSettings> {

	private Integer size = null;

	public BootstrapSelectSettings() {
		super();
	}

	public BootstrapSelectSettings(BootstrapSelectSettings other) {
		super(other);
	}

	public BootstrapSelectSettings(boolean required) {
		super(required);
	}

	@Override
	public boolean isNullValid() {
		return this.nullValid;
	}

	@Override
	public BootstrapSelectSettings setNullValid(boolean nullValid) {
		this.nullValid = nullValid;
		return this;
	}

	public Integer getSize() {
		return this.size;
	}

	public BootstrapSelectSettings setSize(Integer size) {
		this.size = size;
		return this;
	}
}
