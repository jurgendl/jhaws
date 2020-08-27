package org.jhaws.common.web.resteasy;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ImageDeserializer extends JsonDeserializer<Image> {
	@Override
	public Image deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonToken t = p.getCurrentToken();
		String str = null;
		if (t == JsonToken.VALUE_STRING) {
			str = p.getText().trim();
		} else {
			throw new IllegalArgumentException();
		}
		// System.out.println("deserialize " + (str == null ? null :
		// str.length()));
		if (str == null) {
			return null;
		}
		byte[] imagedata = DatatypeConverter.parseBase64Binary(str.substring(str.indexOf(",") + 1));
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));
		return bufferedImage;
	}
}