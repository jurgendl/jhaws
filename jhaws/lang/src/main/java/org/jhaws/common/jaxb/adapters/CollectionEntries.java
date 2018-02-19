package org.jhaws.common.jaxb.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.jhaws.common.jaxb.XmlWrapper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CollectionEntries {
    @XmlAttribute(name = "type", required = true)
    private String collectionType;

    @XmlAttribute(name = "size", required = true)
    private int size;

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item", required = false)
    private XmlWrapper[] collectionEntries;

    public CollectionEntries() {
        super();
    }

    public CollectionEntries(Collection<Object> collection) {
        setCollection(collection);
    }

    @XmlTransient
    public Collection<Object> getCollection() {
        Collection<Object> collection;

        if ("SortedSet".equals(collectionType)) {
            collection = new TreeSet<>();
        } else if ("Set".equals(collectionType)) {
            collection = new LinkedHashSet<>();
        } else if ("List".equals(collectionType)) {
            collection = new ArrayList<>();
        } else {
            throw new IllegalArgumentException("not supported type: " + collectionType);
        }

        for (XmlWrapper collectionEntry : collectionEntries) {
            collection.add(collectionEntry.getValue());
        }

        return collection;
    }

    public void setCollection(Collection<Object> collection) {
        if (collection == null) {
            return;
        }

        collectionEntries = new XmlWrapper[collection.size()];
        int i = 0;

        for (Object entry : collection) {
            collectionEntries[i++] = new XmlWrapper(entry);
        }

        size = i;

        if (collection instanceof SortedSet) {
            collectionType = "SortedSet";
        } else if (collection instanceof Set) {
            collectionType = "Set";
        } else if (collection instanceof List) {
            collectionType = "List";
        } else {
            throw new IllegalArgumentException("not supported type: " + collection.getClass().getName());
        }
    }
}
