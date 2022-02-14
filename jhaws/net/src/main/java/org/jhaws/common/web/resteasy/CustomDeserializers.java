package org.jhaws.common.web.resteasy;

import java.awt.Image;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ReferenceType;

@SuppressWarnings("serial")
public class CustomDeserializers extends Deserializers.Base implements java.io.Serializable {
    @Override
    public JsonDeserializer<?> findReferenceDeserializer(ReferenceType refType, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer contentTypeDeserializer, JsonDeserializer<?> contentDeserializer) throws JsonMappingException {
        final Class<?> raw = refType.getRawClass();
        // System.out.println("? " + raw);
        if (Image.class.isAssignableFrom(raw)) {
            return new ImageDeserializer();
        }
        return null;
    }

    @Override
    public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        final Class<?> raw = type.getRawClass();
        // System.out.println("? " + raw);
        if (Image.class.isAssignableFrom(raw)) {
            return new ImageDeserializer();
        }
        return null;
    }
}