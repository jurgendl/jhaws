package org.jhaws.common.batch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.lang.CollectionUtils8;

@XmlRootElement(name = "step")
public class BatchStep implements Serializable {
	private static final long serialVersionUID = -1875030147272862554L;

	private transient BatchStep parent;

	@XmlAttribute
	private String id;

	@XmlAttribute
	private String name;

	@XmlAttribute
	private BatchState state = BatchState.waiting;

	@XmlAttribute
	private int total = 0;

	@XmlAttribute
	private int progress = 0;

	@XmlAttribute
	private Date start;

	@XmlAttribute
	private Date end;

	@XmlAttribute
	private String info;

	// @XmlElement(name = "step")
	// @XmlElementWrapper(name = "steps")
	private List<BatchStep> steps;

	public BatchStep() {
		super();
	}

	public BatchStep(String id) {
		this.id = id;
		this.name = id;
	}

	public void progress() {
		progress++;
	}

	public BatchStep add(BatchStep step) {
		if (steps == null) steps = new ArrayList<>();
		step.parent = this;
		steps.add(step);
		total++;
		return step;
	}

	public String fullId() {
		List<BatchStep> stepsUp = new ArrayList<>();
		BatchStep current = this;
		while (current != null) {
			stepsUp.add(current);
			current = current.parent;
		}
		return CollectionUtils8.reverse(stepsUp.stream()).map(BatchStep::getId).map(s -> "[" + s + "]").collect(Collectors.joining("."));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BatchState getState() {
		return state;
	}

	public void setState(BatchState state) {
		this.state = state;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public List<BatchStep> getSteps() {
		return steps == null ? Collections.emptyList() : Collections.unmodifiableList(steps);
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = StringUtils.isBlank(info) ? null : StringEscapeUtils.escapeXml10(info);
	}

	@Override
	public String toString() {
		return "BatchStep[" + id + ":" + (total > 0 ? progress + "/" + total : "") + ":" + state + (steps == null ? "" : ":\n\t" + steps.stream().map(Object::toString).collect(Collectors.joining("\n\t")) + "\n") + "]";
	}
}
