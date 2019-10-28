package org.jhaws.common.net.client;

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
import java.util.Map;

public class Form implements Serializable, Iterable<InputElement> {
	public static Form deserialize(InputStream in) throws IOException, ClassNotFoundException {
		try (ObjectInputStream encoder = new ObjectInputStream(new BufferedInputStream(in))) {
			Object object = encoder.readObject();
			encoder.close();
			return (Form) object;
		}
	}

	private static final long serialVersionUID = 7602293891493494638L;

	private Map<String, InputElement> inputElements = new LinkedHashMap<>();

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

	public Form(URI url, org.jsoup.nodes.FormElement formnode) {
		this.url = url;
		this.id = formnode.attr("id");
		this.method = formnode.attr("method");
		this.action = formnode.attr("action");
		formnode.select("input").forEach((org.jsoup.nodes.Element inputnode) -> {
			String type = inputnode.attr("type");
			if ("checkbox".equals(type) || "radio".equals(type)) {
				InputSelection newe = new InputSelection(inputnode);
				InputSelection e = (InputSelection) this.inputElements.get(newe.getNameOrId());
				if (e == null) {
					this.inputElements.put(newe.getNameOrId(), newe);
				} else {
					e.addOption(inputnode.attr("value"));
				}
			} else if ("password".equals(type)) {
				Password e = new Password(inputnode);
				this.inputElements.put(e.getNameOrId(), e);
			} else if ("file".equals(type)) {
				FileInput e = new FileInput(inputnode);
				this.inputElements.put(e.getNameOrId(), e);
			} else {
				Input e = new Input(inputnode);
				this.inputElements.put(e.getNameOrId(), e);
			}
		});
		formnode.select("select").forEach((org.jsoup.nodes.Element selectnode) -> {
			Selection e = new Selection(selectnode);
			this.inputElements.put(e.getNameOrId(), e);
		});
		formnode.select("textarea").forEach((org.jsoup.nodes.Element textareanode) -> {
			TextArea e = new TextArea(textareanode);
			this.inputElements.put(e.getNameOrId(), e);
		});
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

	public Form value(String name, String value) {
		setValue(name, value);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("form: id=").append(this.id).append(",method=").append(this.method)
				.append(",action=").append(this.action).append(";");
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
