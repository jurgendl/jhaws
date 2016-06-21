package org.jhaws.common.net.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.jhaws.common.io.FilePath;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class HTTPClientUtils {
	/** mimeTypes */
	protected static MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();

	/**
	 * build relative url
	 */
	public static String relativeURL(String original, String newRelative) {
		try {
			return URIUtils.resolve(new URI(original), newRelative).toString();
		} catch (URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * build relative url
	 */
	public static URL relativeURL(URL original, String newRelative) {
		try {
			return URIUtils.resolve(original.toURI(), newRelative).toURL();
		} catch (URISyntaxException ex) {
			throw new RuntimeException(ex);
		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * build relative url
	 */
	public static URI relativeURL(URI original, String newRelative) {
		return URIUtils.resolve(original, newRelative);
	}

	public static synchronized String getMimeType(FilePath file) {
		return mimeTypes.getContentType(file.toFile());
	}

	public static synchronized String getMimeType(File file) {
		return mimeTypes.getContentType(file);
	}

	/** formatted/readable xml */
	public static String pretty(String xml) {
		try {
			// java.lang.System.setProperty("javax.xml.transform.TransformerFactory",
			// "org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document original = dBuilder.parse(new InputSource(new InputStreamReader(new ByteArrayInputStream(xml.getBytes("UTF-8")))));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory tf = TransformerFactory.newInstance();
			tf.setAttribute("indent-number", 4);
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(new DOMSource(original), xmlOutput);
			return xmlOutput.getWriter().toString();
		} catch (Exception ex) {
			// ex.printStackTrace();
			return xml;
		}
	}

	public static URI encode(String url) {
		return encode(url, false);
	}

	public static URI encode(String url, boolean hasParameters) {
		String regex = hasParameters
				? "^((?<scheme>[^:]+)://)?(?<host>[^/]+/){1}(?<paths>([^/]+/)*)(?<file>[^\\?]+)(?<paramsstart>\\??)(?<params>([^=]+=[^&]+&){0,}([^=]+=.+){0,1})$"
				: "^((?<scheme>[^:]+)://)?(?<host>[^/]+/){1}(?<paths>([^/]+/)*)(?<file>.+)$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		if (m.find() && url.equals(m.group())) {
			URIBuilder urib = new URIBuilder().setHost(m.group("host"));
			if (StringUtils.isNotBlank(m.group("scheme"))) {
				urib.setScheme(m.group("scheme"));
			}
			String path = Arrays.stream(m.group("paths").split("/")).map(t -> t + "/").collect(Collectors.joining());
			String file = Optional.of(m.group("file")).orElse("").replaceAll("&amp;", "&");
			urib.setPath(path + file);
			if (hasParameters && StringUtils.isNotBlank(m.group("paramsstart"))) {
				Arrays.stream(m.group("params").split("&")).map(kv -> kv.split("=")).forEach((String[] kva) -> {
					urib.addParameter(kva[0], kva[1]);
				});
			}
			try {
				return urib.build();
			} catch (URISyntaxException ex) {
				throw new IllegalArgumentException(ex);
			}
		}
		throw new IllegalArgumentException("encodeURL: " + url);
	}

}
