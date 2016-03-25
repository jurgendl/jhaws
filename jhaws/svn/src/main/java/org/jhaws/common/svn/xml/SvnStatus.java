package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jhaws.common.svn.xml.Svn.RootBeanImpl;

@XmlRootElement(name = "status")
@XmlAccessorType(XmlAccessType.FIELD)
public class SvnStatus extends RootBeanImpl implements Iterable<ChangeList> {
	private Target target;

	private List<ChangeList> changelist;

	@SuppressWarnings("unchecked")
	public List<ChangeList> getChangelist() {
		if (changelist == null) {
			return Collections.EMPTY_LIST;
		}
		return changelist;
	}

	public Target getTarget() {
		return target;
	}

	public void setChangelist(List<ChangeList> changelist) {
		this.changelist = changelist;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	@Override
	public Iterator<ChangeList> iterator() {
		return getChangelist().iterator();
	}
}