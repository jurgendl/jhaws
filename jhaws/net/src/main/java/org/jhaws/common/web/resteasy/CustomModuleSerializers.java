package org.jhaws.common.web.resteasy;

import java.awt.Image;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.type.ReferenceType;

@SuppressWarnings("serial")
public class CustomModuleSerializers extends Serializers.Base implements java.io.Serializable {
	@Override
	public JsonSerializer<?> findReferenceSerializer(SerializationConfig config, ReferenceType refType,
			BeanDescription beanDesc, TypeSerializer contentTypeSerializer,
			JsonSerializer<Object> contentValueSerializer) {
		final Class<?> raw = refType.getRawClass();
		// System.out.println("? " + raw);
		if (Image.class.isAssignableFrom(raw)) {
			return new ImageSerializer();
		}
		return null;
	}

	@Override
	public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
		final Class<?> raw = type.getRawClass();
		// System.out.println("? " + raw);
		if (Image.class.isAssignableFrom(raw)) {
			return new ImageSerializer();
		}
		return null;
	}
}