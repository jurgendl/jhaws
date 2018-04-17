package org.jhaws.common.svn.xml;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "status")
@XmlAccessorType(XmlAccessType.FIELD)
public class SvnStatus extends SvnRootBean implements Iterable<ChangeList> {
	private Target target;

	private List<ChangeList> changelist;

	public List<ChangeList> getChangelist() {
		if (changelist == null) {
			return Collections.emptyList();
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
