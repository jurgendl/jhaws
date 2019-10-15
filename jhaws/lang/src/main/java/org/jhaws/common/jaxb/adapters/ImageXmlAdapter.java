package org.jhaws.common.jaxb.adapters;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ImageXmlAdapter extends XmlAdapter<String, Image> {
	@SuppressWarnings("unused")
	private ImageObserver imageObserver = new Component() {
		//
	};

	@Override
	public Image unmarshal(String str) throws Exception {
		if (str == null)
			return null;
		byte[] imagedata = DatatypeConverter.parseBase64Binary(str.substring(str.indexOf(",") + 1));
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));
		return bufferedImage;
	}

	@Override
	public String marshal(Image v) throws Exception {
		if (v == null)
			return null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		boolean ok = ImageIO.write(toBufferedImage(v), "png", output);
		output.close();
		String string = "data:image/png;base64," + DatatypeConverter.printBase64Binary(output.toByteArray());
		return string;
	}

	public BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		BufferedImage bimage = new BufferedImage(img.getWidth(imageObserver), img.getHeight(imageObserver),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, imageObserver);
		bGr.dispose();
		return bimage;
	}
}
