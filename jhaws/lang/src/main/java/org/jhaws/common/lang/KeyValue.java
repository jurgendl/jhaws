package org.jhaws.common.lang;

import java.io.Serializable;
import java.util.function.Function;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class KeyValue<K, V> implements Serializable {
	private static final long serialVersionUID = 5148190064982535265L;

	private K key;

	private V value;

	public KeyValue() {
		super();
	}

	public KeyValue(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "KeyValue [key=" + key + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyValue<?, ?> other = (KeyValue<?, ?>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else
			if (!key.equals(other.key))
				return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else
			if (!value.equals(other.value))
				return false;
		return true;
	}

	public boolean isEquals(Function<K, ?> kf, Function<V, ?> vf) {
		return new EqualsBuilder().append(kf.apply(key), vf.apply(value)).isEquals();
	}

	public boolean notEquals(Function<K, ?> kf, Function<V, ?> vf) {
		return !isEquals(kf, vf);
	}

	public boolean isEquals() {
		return new EqualsBuilder().append(key, value).isEquals();
	}

	public boolean notEquals() {
		return !isEquals();
	}
}
