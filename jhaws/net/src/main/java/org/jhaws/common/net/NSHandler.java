package org.jhaws.common.net;

import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NSHandler extends HashMap<String, String> implements NamespaceContext {
    private static final long serialVersionUID = -5509377982038203847L;

    public static final String DEFAULT = "default";

    private static final String XMLNS = "xmlns";

    public NSHandler(NamedNodeMap basenode) {
        this.init(basenode);
    }

    /**
     * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
     */
    @Override
    public String getNamespaceURI(String prefix) {
        for (String key : this.keySet()) {
            if (prefix.equals(key)) {
                return this.get(key);
            }
        }
        return this.get(NSHandler.DEFAULT);
    }

    /** Dummy implemenation - not used! */
    @Override
    public String getPrefix(String uri) {
        return null;
    }

    /** Dummy implemenation - not used! */
    @Override
    public Iterator<String> getPrefixes(String namespaceURI) {

        return null;
    }

    protected void init(NamedNodeMap projectAtts) {
        for (int i = 0; i < projectAtts.getLength(); i++) {
            Node item = projectAtts.item(i);
            String ns = item.getNodeName();
            if (ns.startsWith(NSHandler.XMLNS)) {
                int index = ns.indexOf(':');
                if (index == -1) {
                    ns = NSHandler.DEFAULT;
                } else {
                    ns = ns.substring(index + 1);
                }
                String v = item.getNodeValue();
                this.put(ns, v);
            }
        }
    }
}