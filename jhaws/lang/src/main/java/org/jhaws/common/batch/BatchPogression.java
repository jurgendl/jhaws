package org.jhaws.common.batch;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jhaws.common.jaxb.XmlWrapper;
import org.jhaws.common.jaxb.adapters.XmlWrapperAdapter;

@XmlRootElement(name = "progression")
public class BatchPogression {
	@XmlJavaTypeAdapter(XmlWrapperAdapter.class)
	private XmlWrapper<?> config;

	private BatchStep progression;

	public BatchPogression() {
		super();
	}

	public BatchPogression(XmlWrapper<?> config, BatchStep progression) {
		super();
		this.config = config;
		this.progression = progression;
	}

	public BatchStep getProgression() {
		return this.progression;
	}

	public void setProgression(BatchStep progression) {
		this.progression = progression;
	}

	public XmlWrapper<?> getConfig() {
		return this.config;
	}

	public void setConfig(XmlWrapper<?> config) {
		this.config = config;
	}
}
