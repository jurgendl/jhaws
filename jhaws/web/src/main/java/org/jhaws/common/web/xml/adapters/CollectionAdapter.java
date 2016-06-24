package org.jhaws.common.web.xml.adapters;

import java.util.Collection;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CollectionAdapter extends XmlAdapter<CollectionEntries, Collection<Object>> {
    @Override
    public CollectionEntries marshal(Collection<Object> collection) throws Exception {
        return collection == null ? null : new CollectionEntries(collection);
    }

    @Override
    public Collection<Object> unmarshal(CollectionEntries collectionEntries) throws Exception {
        return collectionEntries == null ? null : collectionEntries.getCollection();
    }
}