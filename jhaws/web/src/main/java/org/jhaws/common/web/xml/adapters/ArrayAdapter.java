package org.jhaws.common.web.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jhaws.common.web.xml.XmlWrapper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ArrayAdapter extends XmlAdapter<XmlWrapper[], Object[]> {
	@Override
	public XmlWrapper[] marshal(Object[] array) throws Exception {
		if (array == null) {
			return null;
		}
		XmlWrapper[] items = new XmlWrapper[array.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new XmlWrapper(array[i]);
		}
		return items;
	}

	@Override
	public Object[] unmarshal(XmlWrapper[] arrayEntries) throws Exception {
		if (arrayEntries == null) {
			return null;
		}

		Object[] items = new Object[arrayEntries.length];
		for (int i = 0; i < arrayEntries.length; i++) {
			items[i] = arrayEntries[i].getValue();
		}
		return items;
	}
}