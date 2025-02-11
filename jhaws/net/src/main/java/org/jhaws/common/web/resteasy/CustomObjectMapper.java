package org.jhaws.common.web.resteasy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("serial")
public class CustomObjectMapper extends ObjectMapper {
    public CustomObjectMapper() {
        super(new JsonFactory());

        CustomModule module = new CustomModule();
        registerModule(module);
        // System.out.println(module);

        // findAndRegisterModules();
        // findModules().forEach(System.out::println);
        // https://github.com/FasterXML/jackson-modules-java8
        // registerModule(new
        // com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule());
        try {
            registerModule(new com.fasterxml.jackson.datatype.jsr353.JSR353Module());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            registerModule(new com.fasterxml.jackson.datatype.joda.JodaModule());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            registerModule(new com.fasterxml.jackson.datatype.jdk8.Jdk8Module());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // registerModule(new
        // com.fasterxml.jackson.module.paramnames.ParameterNamesModule());
        // registerModule(new
        // com.fasterxml.jackson.datatype.guava.GuavaModule());
        // registerModule(new
        // com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module());

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

    @Override
    public ObjectMapper copy() {
        return new CustomObjectMapper();
    }
}
