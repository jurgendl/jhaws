package org.jhaws.common.net.client.obsolete;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.TagNode;

public class Form implements Serializable, Iterable<InputElement> {
	public static Form deserialize(InputStream in) throws IOException, ClassNotFoundException {
		try (ObjectInputStream encoder = new ObjectInputStream(new BufferedInputStream(in))) {
			Object object = encoder.readObject();
			encoder.close();
			return (Form) object;
		}
	}

	private static final long serialVersionUID = 7602293891493494638L;

	private Map<String, InputElement> inputElements = new LinkedHashMap<String, InputElement>();

	private String action;

	private String id;

	private String method;

	private URI url;

	public Form() {
		super();
	}

	public Form(String id) {
		this.id = id;
	}

	public Form(URI url, TagNode formnode) {
		this.url = url;
		this.id = formnode.getAttributeByName("id");
		this.method = formnode.getAttributeByName("method");
		this.action = formnode.getAttributeByName("action");

		List<? extends TagNode> inputlist = formnode.getElementListByName("input", true);

		for (TagNode inputnode : inputlist) {
			String type = inputnode.getAttributeByName("type");

			if ("checkbox".equals(type) || "radio".equals(type)) {
				InputSelection newe = new InputSelection(inputnode);
				InputSelection e = (InputSelection) this.inputElements.get(newe.getName());

				if (e == null) {
					this.inputElements.put(newe.getName(), newe);
				} else {
					e.addOption(inputnode.getAttributeByName("value"));
				}
			} else if ("password".equals(type)) {
				final Password e = new Password(inputnode);
				this.inputElements.put(e.getName(), e);
			} else if ("file".equals(type)) {
				final FileInput e = new FileInput(inputnode);
				this.inputElements.put(e.getName(), e);
			} else {
				final Input e = new Input(inputnode);
				this.inputElements.put(e.getName(), e);
			}
		}

		List<? extends TagNode> selectlist = formnode.getElementListByName("select", true);

		for (TagNode selectnode : selectlist) {
			final Selection e = new Selection(selectnode);
			this.inputElements.put(e.getName(), e);
		}

		List<? extends TagNode> textarealist = formnode.getElementListByName("textarea", true);

		for (TagNode textareanode : textarealist) {
			final TextArea e = new TextArea(textareanode);
			this.inputElements.put(e.getName(), e);
		}
	}

	public boolean contains(String string) {
		return this.inputElements.keySet().contains(string);
	}

	public String getAction() {
		return this.action;
	}

	public String getId() {
		return this.id;
	}

	public InputElement getInputElement(String name) {
		return this.inputElements.get(name);
	}

	public Collection<InputElement> getInputElements() {
		return this.inputElements.values();
	}

	public String getMethod() {
		return this.method;
	}

	public URI getUrl() {
		return this.url;
	}

	@Override
	public Iterator<InputElement> iterator() {
		return this.inputElements.values().iterator();
	}

	public void removeInputElement(String name) {
		this.inputElements.remove(name);
	}

	public Form serialize(OutputStream out) throws IOException {
		try (ObjectOutputStream encoder = new ObjectOutputStream(new BufferedOutputStream(out))) {
			encoder.writeObject(this);
			encoder.close();
			return this;
		}
	}

	public InputElement setValue(String name, String value) {
		InputElement inputElement = this.getInputElement(name);
		if (inputElement == null) {
			inputElement = new Input(null, name, name);
			this.inputElements.put(name, inputElement);
		}
		inputElement.setValue(value);
		return inputElement;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("form: id=").append(this.id).append(",method=").append(this.method).append(",action=").append(this.action).append(";");
		for (InputElement element : this.getInputElements()) {
			sb.append("\n\t").append(element);
		}
		return sb.toString();
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setInputElements(Map<String, InputElement> inputElements) {
		this.inputElements = inputElements;
	}

	public void addInputElements(String key, InputElement inputElement) {
		inputElements.put(key, inputElement);
	}

	public void removeInputElements(String key) {
		inputElements.remove(key);
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUrl(URI url) {
		this.url = url;
	}
}
