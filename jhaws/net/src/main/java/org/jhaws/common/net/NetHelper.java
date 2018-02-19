package org.jhaws.common.net;

import java.net.URI;

public class NetHelper {
    public static String getName(URI url) {
        String tmp = url.toString();
        int beginIndex = tmp.lastIndexOf("/") + 1;
        int endIndex = tmp.indexOf("?", beginIndex);
        if (endIndex == -1) endIndex = tmp.length();
        String n = tmp.substring(beginIndex, endIndex);
        if (n.length() == 0) n = null;
        return n;
    }
}
