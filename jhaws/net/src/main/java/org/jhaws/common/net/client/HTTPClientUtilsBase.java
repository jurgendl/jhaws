package org.jhaws.common.net.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

import org.apache.hc.core5.net.URIBuilder;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class HTTPClientUtilsBase {

	protected static MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();

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
			Document original = dBuilder.parse(
					new InputSource(new InputStreamReader(new ByteArrayInputStream(xml.getBytes(StringUtils.UTF8)))));
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

	public static String encodeSimple(String part) {
		try {
			return URLEncoder.encode(part, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static URI encode(String url) {
		// FIXME multiple paths with matrix parameters:
		// http://example.com/res/categories;name=foo/objects;name=green/?page=1
		boolean hasQueryParameters = url.contains("?");
		boolean hasMatrixParameters = url.contains(";");
		String hostGr = "host";
		String schemeGr = "scheme";
		String pathsGr = "paths";
		String fileGr = "file";
		String paramsstartGr = "paramsstart";
		String paramsGr = "params";
		String regex;
		{
			regex = "^((?<" + schemeGr + ">[^:]+)://)?(?<" + hostGr + ">[^/]+/){1}(?<" + pathsGr + ">([^/]+/)*)(?<"
					+ fileGr + ">";
			if (hasQueryParameters) {
				regex = regex + "[^\\?]+)(?<" + paramsstartGr + ">\\??)(?<" + paramsGr
						+ ">([^=]+=[^&]+&){0,}([^=]+=.+){0,1})";
			} else if (hasMatrixParameters) {
				regex = regex + "[^;]+)(?<" + paramsstartGr + ">;)(?<" + paramsGr
						+ ">([^=]+=[^;]+;){0,}([^=]+=.+){0,1})";
			} else {
				regex = regex + ".+)";
			}
			regex = regex + "$";
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		if (m.find() && url.equals(m.group())) {
			URIBuilder urib = new URIBuilder().setHost(m.group(hostGr));
			if (StringUtils.isNotBlank(m.group(schemeGr))) {
				urib.setScheme(m.group(schemeGr));
			}
			String path = Arrays.stream(m.group(pathsGr).split("/")).map(t -> t + "/").collect(Collectors.joining());
			String file = Optional.of(m.group(fileGr)).orElse("").replace("&amp;", "&");
			String matrix = "";
			if (hasMatrixParameters && StringUtils.isNotBlank(m.group(paramsstartGr))) {
				matrix = ";" + Arrays.stream(m.group(paramsGr).split(";")).map(kv -> kv.split("="))
						.map((String[] kva) -> (encodeSimple(kva[0]) + "=" + encodeSimple(kva[1])))
						.collect(Collectors.joining(";"));
			}
			urib.setPath(("/".equals(path) ? "" : path) + file + matrix);
			if (hasQueryParameters && StringUtils.isNotBlank(m.group(paramsstartGr))) {
				Arrays.stream(m.group(paramsGr).split("&")).map(kv -> kv.split("="))
						.forEach((String[] kva) -> urib.addParameter(kva[0], kva[1]));
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
