package org.jhaws.common.net.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jhaws.common.lang.StringUtils;
import org.w3c.tidy.Tidy;

/**
 * HtmlCleanup
 */
public class HtmlCleanup {
    public ByteArrayOutputStream cleanupHtml(ByteArrayOutputStream bin) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); ByteArrayInputStream in = new ByteArrayInputStream(bin.toByteArray())) {
            Tidy tidy = new Tidy();
            tidy.setMakeClean(true);
            tidy.setWraplen(1000);
            tidy.setXmlOut(true);
            tidy.setInputEncoding(StringUtils.UTF8);
            tidy.setFixBackslash(true);
            tidy.setFixComments(true);
            tidy.setDropEmptyParas(true);
            tidy.setMakeClean(true);
            tidy.parse(in, out);
            in.close();
            out.close();
            return out;
        }
    }
}
