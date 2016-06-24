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

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class HTTPClientUtils {
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
			Document original = dBuilder.parse(new InputSource(new InputStreamReader(new ByteArrayInputStream(xml.getBytes(StringUtils.UTF8)))));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory tf = TransformerFactory.newInstance();
			tf.setAttribute("indent-number", 4);
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, StringUtils.UTF8);
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
		String hostGr = "host";
		String schemeGr = "scheme";
		String pathsGr = "paths";
		String fileGr = "file";
		String paramsstartGr = "paramsstart";
		String paramsGr = "params";
		String regexCommon = "^((?<" + schemeGr + ">[^:]+)://)?(?<" + hostGr + ">[^/]+/){1}(?<" + pathsGr + ">([^/]+/)*)(?<" + fileGr + ">";
		String regex = !hasParameters ? (regexCommon + ".+)$") : (regexCommon + "[^\\?]+)(?<" + paramsstartGr + ">\\??)(?<" + paramsGr + ">([^=]+=[^&]+&){0,}([^=]+=.+){0,1})$");
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		if (m.find() && url.equals(m.group())) {
			URIBuilder urib = new URIBuilder().setHost(m.group(hostGr));
			if (StringUtils.isNotBlank(m.group(schemeGr))) {
				urib.setScheme(m.group(schemeGr));
			}
			String path = Arrays.stream(m.group(pathsGr).split("/")).map(t -> t + "/").collect(Collectors.joining());
			String file = Optional.of(m.group(fileGr)).orElse("").replaceAll("&amp;", "&");
			urib.setPath(path + file);
			if (hasParameters && StringUtils.isNotBlank(m.group(paramsstartGr))) {
				Arrays.stream(m.group(paramsGr).split("&")).map(kv -> kv.split("=")).forEach((String[] kva) -> urib.addParameter(kva[0], kva[1]));
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
