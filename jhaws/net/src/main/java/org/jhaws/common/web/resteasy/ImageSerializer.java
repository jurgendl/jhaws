package org.jhaws.common.web.resteasy;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ImageSerializer extends JsonSerializer<Image> {
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
        // System.out.println("serialize " + string.length());
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