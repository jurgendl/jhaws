package org.jhaws.common.web.xml.adapaters;

import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapAdapter extends XmlAdapter<MapEntries, Map<Object, Object>> {
    @Override
    public MapEntries marshal(Map<Object, Object> map) throws Exception {
        return map == null ? null : new MapEntries(map);
    }

    @Override
    public Map<Object, Object> unmarshal(MapEntries mapEntries) throws Exception {
        return mapEntries == null ? null : mapEntries.getMap();
    }
}
