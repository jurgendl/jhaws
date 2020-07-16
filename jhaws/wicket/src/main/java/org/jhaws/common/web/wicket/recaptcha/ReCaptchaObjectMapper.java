package org.jhaws.common.web.wicket.recaptcha;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

// <dependency>
// <groupId>com.fasterxml.jackson.core</groupId>
// <artifactId>jackson-databind</artifactId>
// <version>2.11.0</version>
// </dependency>
@SuppressWarnings("serial")
public class ReCaptchaObjectMapper extends ObjectMapper {
	public ReCaptchaObjectMapper() {
		registerModule(new com.fasterxml.jackson.datatype.jsr353.JSR353Module());
		registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
		registerModule(new com.fasterxml.jackson.datatype.joda.JodaModule());
		registerModule(new com.fasterxml.jackson.datatype.jdk8.Jdk8Module());
		setVisibility(getSerializationConfig().getDefaultVisibilityChecker()//
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)//
				.withGetterVisibility(JsonAutoDetect.Visibility.NONE)//
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE)//
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));//
	}
}