package org.jhaws.common.jaxb.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

public class MapEntries {
	@XmlAttribute(name = "type", required = true)
	private String mapType;

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item", required = false)
	private MapEntry[] mapEntries;

	@XmlAttribute(name = "size", required = true)
	private int size;

	public MapEntries() {
		super();
	}

	public MapEntries(Map<Object, Object> map) {
		setMap(map);
	}

	@XmlTransient
	public Map<Object, Object> getMap() {
		Map<Object, Object> map;

		if ("SortedMap".equals(mapType)) {
			map = new TreeMap<>();
		} else if ("Map".equals(mapType)) {
			map = new HashMap<>();
		} else {
			throw new IllegalArgumentException("not supported type: " + mapType);
		}

		for (MapEntry mapEntry : mapEntries) {
			map.put(mapEntry.key.getValue(), mapEntry.value.getValue());
		}

		return map;
	}

	@SuppressWarnings("cast")
	public void setMap(Map<Object, Object> map) {
		if (map == null) {
			return;
		}

		mapEntries = new MapEntry[map.size()];
		int i = 0;

		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			mapEntries[i++] = new MapEntry(entry.getKey(), entry.getValue());
		}

		size = i;

		if (map instanceof SortedMap) {
			mapType = "SortedMap";
		} else if (map instanceof Map) {
			mapType = "Map";
		} else {
			throw new IllegalArgumentException("not supported type: " + map.getClass().getName());
		}
	}
}
