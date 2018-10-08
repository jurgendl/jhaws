package org.jhaws.common.lang;

import java.util.Map;
import java.util.function.Function;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;

@XmlRootElement
public class KeyValue<K, V> extends Value<V> implements Map.Entry<K, V> {
    private static final long serialVersionUID = 5148190064982535265L;

    public static <T, U> KeyValue<T, U> keyValue(T key, U value) {
        return new KeyValue<>(key, value);
    }

    protected K key;

    public KeyValue() {
        super();
    }

    public KeyValue(Map.Entry<K, V> entry) {
        this(entry.getKey(), entry.getValue());
    }

    public KeyValue(K key, V value) {
        super(value);
        this.key = key;
    }

    @Override
    public K getKey() {
        return key;
    }

    public K setKey(K key) {
        return this.key = key;
    }

    @Override
    public String toString() {
        return key + "=" + value;
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
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        KeyValue<?, ?> other = (KeyValue<?, ?>) obj;
        if (key == null) {
            if (other.key != null) return false;
        } else if (!key.equals(other.key)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    public <I> boolean isEquals(Function<K, I> kf, Function<V, I> vf) {
        return new EqualsBuilder().append(kf.apply(key), vf.apply(value)).isEquals();
    }

    public <I> boolean notEquals(Function<K, I> kf, Function<V, I> vf) {
        return !isEquals(kf, vf);
    }

    public boolean isEquals() {
        return new EqualsBuilder().append(key, value).isEquals();
    }

    public boolean notEquals() {
        return !isEquals();
    }
}
