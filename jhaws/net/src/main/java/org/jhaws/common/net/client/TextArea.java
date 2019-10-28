package org.jhaws.common.net.client;

public class TextArea implements InputElement {
	private static final long serialVersionUID = 1209409116199015320L;

	private final String id;

	private final String name;

	private String value;

	public TextArea(org.jsoup.nodes.Element textareanode) {
		this.name = textareanode.attr("name");
		this.id = textareanode.attr("id");
		this.value = textareanode.html().toString();
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public InputType getType() {
		return InputType.textarea;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "input[type=textarea,name=" + this.name + ",id=" + this.id + ",value=" + this.value + "]";
	}
}
