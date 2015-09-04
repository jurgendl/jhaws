package org.jhaws.common.net.client.forms;

import org.htmlcleaner.TagNode;
import org.jhaws.common.io.security.SecureMeBC;
import org.jhaws.common.io.security.Security;

public class Password extends Input {
	private static final long serialVersionUID = -3366809266365586314L;

	private Security secure = new SecureMeBC();

	private byte[] data;

	public Password(String id, String name) {
		super(InputType.password, id, name);
	}

	public Password(TagNode inputnode) {
		super(inputnode);
	}

	private String decrypt() {
		try {
			return this.secure.decrypt(this.data);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private void encrypt(String string) {
		try {
			this.data = this.secure.encrypt(string);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String getValue() {
		return (this.data == null) ? null : this.decrypt();
	}

	public void reset() {
		this.setValue(null);
	}

	@Override
	public void setValue(String value) {
		if (value == null) {
			this.data = null;
		} else {
			this.encrypt(value);
		}
	}
}
