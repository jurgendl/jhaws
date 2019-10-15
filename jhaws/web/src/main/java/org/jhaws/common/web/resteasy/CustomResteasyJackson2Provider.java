package org.jhaws.common.web.resteasy;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            // https://stackoverflow.com/questions/33820327/excluding-null-fields-in-pojo-response
            setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
            // mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

            // https://developer.jboss.org/thread/215591
            configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            configure(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT, false);

            configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            configure(com.fasterxml.jackson.core.JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
            // FIXME configure(com.fasterxml.jackson.core.json.JsonWriteFeature.ESCAPE_NON_ASCII, true);

            // mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);

            // https://github.com/FasterXML/jackson-modules-java8
            registerModule(new com.fasterxml.jackson.datatype.joda.JodaModule());
            registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            registerModule(new com.fasterxml.jackson.datatype.jdk8.Jdk8Module());
            registerModule(new com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module());

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
            // @JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
        }
    }
}
