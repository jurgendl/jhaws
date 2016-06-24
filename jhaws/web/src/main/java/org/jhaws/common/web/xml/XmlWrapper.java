package org.jhaws.common.web.xml;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jhaws.common.web.xml.adapaters.ArrayAdapter;
import org.jhaws.common.web.xml.adapaters.CollectionAdapter;
import org.jhaws.common.web.xml.adapaters.EnumAdapter;
import org.jhaws.common.web.xml.adapaters.MapAdapter;

@XmlRootElement(name = "any")
public class XmlWrapper<T> implements Serializable {
	private static final long serialVersionUID = 3422118670925672560L;

	@XmlAttribute(name = "type", required = false)
	private String valueType;

	@XmlAttribute(name = "string", required = false)
	private String string;

	@XmlElement(name = "value", required = false)
	private Object simpleValue;

	@XmlElement(name = "collection", required = false)
	@XmlJavaTypeAdapter(CollectionAdapter.class)
	private Collection<Object> collection;

	@XmlElement(name = "map", required = false)
	@XmlJavaTypeAdapter(MapAdapter.class)
	private Map<Object, Object> map;

	@XmlElement(name = "array", required = false)
	@XmlJavaTypeAdapter(ArrayAdapter.class)
	private Object[] array;

	@XmlElement(name = "bytes", required = false)
	private byte[] bytes;

	@XmlElement(name = "enum", required = false)
	@XmlJavaTypeAdapter(EnumAdapter.class)
	private Enum<?> enumValue;

	private transient Object transientValue;

	public XmlWrapper() {
		super();
	}

	public XmlWrapper(T value) {
		setValue(value);
	}

	@SuppressWarnings("unchecked")
	@XmlTransient
	public T getValue() {
		if (transientValue == null) {
			if ("Collection".equals(valueType)) {
				transientValue = (T) collection;
			} else if ("Map".equals(valueType)) {
				transientValue = (T) map;
			} else if ("Array".equals(valueType)) {
				transientValue = (T) array;
			} else if ("Bytes".equals(valueType)) {
				transientValue = (T) bytes;
			} else if (string != null) {
				transientValue = string;
			} else if (enumValue != null) {
				transientValue = enumValue;
			} else {
				transientValue = simpleValue;
			}
		}

		return (T) transientValue;
	}

	@SuppressWarnings("unchecked")
	public void setValue(T value) {
		transientValue = value;

		valueType = null;

		if (value == null) {
			simpleValue = null;
		} else if (value instanceof Enum) {
			enumValue = (Enum<?>) value;
		} else if (value instanceof String) {
			string = (String) value;
		} else if (value instanceof Collection) {
			valueType = "Collection";
			collection = (Collection<Object>) value;
		} else if (value instanceof byte[]) {
			valueType = "Bytes";
			bytes = (byte[]) value;
		} else if (value.getClass().isArray()) {
			valueType = "Array";
			array = (Object[]) value;
		} else if (value instanceof Map) {
			valueType = "Map";
			map = (Map<Object, Object>) value;
		} else {
			simpleValue = value;
		}
	}

	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getValue() == null ? null : getValue().toString();
	}
}
