package org.jhaws.common.net.client.forms;

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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.TagNode;

/**
 * Form
 */
public class Form implements Serializable, Iterable<InputElement> {

    private static final long serialVersionUID = 7602293891493494638L;

    public static Form deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream encoder = new ObjectInputStream(new BufferedInputStream(in));
        Object object = encoder.readObject();
        encoder.close();

        return (Form) object;
    }

    /** inputElements */
    private Map<String, InputElement> inputElements = new LinkedHashMap<String, InputElement>();

    /** action */
    private String action;

    /** id */
    private String id;

    /** method */
    private String method;

    /** url */
    private URI url;

    /**
     * Creates a new Form object.
     */
    public Form() {
        super();
    }

    @SuppressWarnings("unchecked")
    public Form(URI url, TagNode formnode) {
        this.url = url;
        this.id = formnode.getAttributeByName("id");
        this.method = formnode.getAttributeByName("method");
        this.action = formnode.getAttributeByName("action");

        List<TagNode> inputlist = formnode.getElementListByName("input", true);

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

        List<TagNode> selectlist = formnode.getElementListByName("select", true);

        for (TagNode selectnode : selectlist) {
            final Selection e = new Selection(selectnode);
            this.inputElements.put(e.getName(), e);
        }

        List<TagNode> textarealist = formnode.getElementListByName("textarea", true);

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
        return Collections.unmodifiableCollection(this.inputElements.values());
    }

    public String getMethod() {
        return this.method;
    }

    public URI getUrl() {
        return this.url;
    }

    /**
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<InputElement> iterator() {
        return this.inputElements.values().iterator();
    }

    public Form serialize(OutputStream out) throws IOException {
        ObjectOutputStream encoder = new ObjectOutputStream(new BufferedOutputStream(out));
        encoder.writeObject(this);
        encoder.close();

        return this;
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

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("form: id=").append(this.id).append(",method=").append(this.method).append(",action=")
                .append(this.action).append("[");

        for (InputElement element : this.getInputElements()) {
            sb.append("\n\t").append(element);
        }

        return sb.append("]\n").toString();
    }
}
