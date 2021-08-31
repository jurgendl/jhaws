package org.jhaws.common.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebUtilities {
    public static final String IMAGE_PLACEHOLDER = "data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==";

    public static URI convert(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String fixHtml(String html) {
        if (StringUtils.isBlank(html)) return null;

        // tinymce
        html = html.replace("<br data-mce-bogus=\"1\"/>", "").replace("<br data-mce-bogus=\"1\">", "");

        Document document = Jsoup.parse(html);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        Elements els = document.body().getAllElements();
        for (Element e : els) {
            {
                // https://stackoverflow.com/questions/7808968/what-do-op-elements-do-anyway/7809422
                // [Fatal Error] :1:76: The prefix "o" for element "o:p" is not bound.
                // https://stackoverflow.com/questions/9008969/how-to-replace-a-tag-using-jsoup
                if (e.tagName().startsWith("o:")) {
                    e.tagName(e.tagName().replaceFirst("o:", ""));
                }
            }
        }

        html = document.select("body").html();

        // https://coderanch.com/t/435548/java/entity-nbsp-referenced-declared
        html = html.replace("&nbsp;", "&#160;");

        return html;
    }
}
