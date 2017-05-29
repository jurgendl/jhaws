package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jhaws.common.io.FilePath;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @see https://www.tutorialspoint.com/tika/tika_extracting_odf.htm
 */
public class HtmlFileTextExtracter implements FileTextExtracter {
    @Override
    public List<String> accepts() {
        return Arrays.asList("htm", "html");
    }

    @Override
    public void extract(InputStream html, FilePath txt) throws IOException {
        List<String> lines = new ArrayList<>();
        Document document = Jsoup.parse(html, "utf-8", "http://localhost/");
        Elements allElements = document.getAllElements();
        for (Element element : allElements) {
            lines.add(element.ownText());
        }
        txt.write(lines.stream().collect(Collectors.joining("\n")));
    }
}
