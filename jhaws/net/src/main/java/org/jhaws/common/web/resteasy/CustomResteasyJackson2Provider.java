package org.jhaws.common.web.resteasy;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.type.ReferenceType;

// extends and add these annotations for auto configuring
// @Provider
// @Consumes({"application/json", "application/*+json", "text/json"})
// @Produces({"application/json", "application/*+json", "text/json"})
public class CustomResteasyJackson2Provider extends ResteasyJackson2Provider {
    public CustomResteasyJackson2Provider() {
        setMapper(new CustomObjectMapper());
    }

    @SuppressWarnings("serial")
    public static class CustomObjectMapper extends ObjectMapper {
        public CustomObjectMapper() {
            CustomModule module = new CustomModule();
            registerModule(module);
            // System.out.println(module);

            // findAndRegisterModules();
            // findModules().forEach(System.out::println);
            // https://github.com/FasterXML/jackson-modules-java8
            // registerModule(new com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule());
            registerModule(new com.fasterxml.jackson.datatype.jsr353.JSR353Module());
            registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            registerModule(new com.fasterxml.jackson.datatype.joda.JodaModule());
            registerModule(new com.fasterxml.jackson.datatype.jdk8.Jdk8Module());
            // registerModule(new com.fasterxml.jackson.module.paramnames.ParameterNamesModule());
            // registerModule(new com.fasterxml.jackson.datatype.guava.GuavaModule());
            // registerModule(new com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module());

            // https://stackoverflow.com/questions/33820327/excluding-null-fields-in-pojo-response
            setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
            // mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

            // https://developer.jboss.org/thread/215591
            configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            // TODO set date format? read config prop jdoc

            configure(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT, false);

            configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            configure(com.fasterxml.jackson.core.JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
            // FIXME
            // configure(com.fasterxml.jackson.core.json.JsonWriteFeature.ESCAPE_NON_ASCII,
            // true);

            // mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);

            // https://stackoverflow.com/questions/7105745/how-to-specify-jackson-to-only-use-fields-preferably-globally
            setVisibility(getSerializationConfig().getDefaultVisibilityChecker()//
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)//
                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)//
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)//
                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));//
            // // alternatief:
            // setVisibility(PropertyAccessor.ALL, Visibility.NONE);
            // setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
            // // alternatief:
            // @JsonAutoDetect(fieldVisibility = Visibility.NONE,
            // getterVisibility = Visibility.NONE, setterVisibility =
            // Visibility.NONE)
        }
    }

    @SuppressWarnings("serial")
    public static class CustomModule extends com.fasterxml.jackson.databind.module.SimpleModule {
        @Override
        public String getModuleName() {
            return "CustomModule";
        }

        @Override
        public Version version() {
            return new Version(1, 0, 0, null, null, null);
        }

        @Override
        public void setupModule(SetupContext context) {
            context.addSerializers(new CustomModuleSerializers());
            context.addDeserializers(new CustomDeserializers());
        }
    }

    @SuppressWarnings("serial")
    public static class CustomModuleSerializers extends Serializers.Base implements java.io.Serializable {
        @Override
        public JsonSerializer<?> findReferenceSerializer(SerializationConfig config, ReferenceType refType, BeanDescription beanDesc,
                TypeSerializer contentTypeSerializer, JsonSerializer<Object> contentValueSerializer) {
            final Class<?> raw = refType.getRawClass();
            System.out.println("? " + raw);
            if (Image.class.isAssignableFrom(raw)) {
                return new ImageSerializer();
            }
            return null;
        }

        @Override
        public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
            final Class<?> raw = type.getRawClass();
            System.out.println("? " + raw);
            if (Image.class.isAssignableFrom(raw)) {
                return new ImageSerializer();
            }
            return null;
        }
    }

    @SuppressWarnings("serial")
    public static class CustomDeserializers extends Deserializers.Base implements java.io.Serializable {
        @Override
        public JsonDeserializer<?> findReferenceDeserializer(ReferenceType refType, DeserializationConfig config, BeanDescription beanDesc,
                TypeDeserializer contentTypeDeserializer, JsonDeserializer<?> contentDeserializer) throws JsonMappingException {
            final Class<?> raw = refType.getRawClass();
            System.out.println("? " + raw);
            if (Image.class.isAssignableFrom(raw)) {
                return new ImageDeserializer();
            }
            return null;
        }

        @Override
        public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc)
                throws JsonMappingException {
            final Class<?> raw = type.getRawClass();
            System.out.println("? " + raw);
            if (Image.class.isAssignableFrom(raw)) {
                return new ImageDeserializer();
            }
            return null;
        }
    }

    public static class ImageSerializer extends JsonSerializer<Image> {
        @SuppressWarnings({ "serial" })
        private ImageObserver imageObserver = new Component() {
            //
        };

        @Override
        public void serialize(Image v, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (v == null) return;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            @SuppressWarnings("unused")
            boolean ok = ImageIO.write(toBufferedImage(v), "png", output);
            output.close();
            String string = "data:image/png;base64," + DatatypeConverter.printBase64Binary(output.toByteArray());
            System.out.println("serialize " + string.length());
            gen.writeString(string);
        }

        public BufferedImage toBufferedImage(Image img) {
            if (img instanceof BufferedImage) {
                return (BufferedImage) img;
            }
            BufferedImage bimage = new BufferedImage(img.getWidth(imageObserver), img.getHeight(imageObserver), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(img, 0, 0, imageObserver);
            bGr.dispose();
            return bimage;
        }
    }

    public static class ImageDeserializer extends JsonDeserializer<Image> {
        @Override
        public Image deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = p.getCurrentToken();
            String str = null;
            if (t == JsonToken.VALUE_STRING) {
                str = p.getText().trim();
            } else {
                throw new IllegalArgumentException();
            }
            System.out.println("deserialize " + (str == null ? null : str.length()));
            if (str == null) {
                return null;
            }
            byte[] imagedata = DatatypeConverter.parseBase64Binary(str.substring(str.indexOf(",") + 1));
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));
            return bufferedImage;
        }
    }
}
