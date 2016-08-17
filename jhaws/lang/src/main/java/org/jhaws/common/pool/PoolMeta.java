package org.jhaws.common.pool;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PoolMeta implements Serializable {
	private static final long serialVersionUID = 7114977981759391109L;
	protected Long enqueued;
	protected Long started;
	protected Long completed;
	protected Map<String, Object> extra = Collections.synchronizedMap(new LinkedHashMap<>());

	public Long getEnqueued() {
		return enqueued;
	}

	public Long getStarted() {
		return started;
	}

	public Long getCompleted() {
		return completed;
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	public PoolMeta put(String key, Object value) {
		extra.put(key, value);
		return this;
	}

	@Override
	public String toString() {
		if (extra.size() > 0) {
			return extra.toString();
		}
		return "PoolMeta [enqueued=" + enqueued + ", started=" + started + ", completed=" + completed + "]";
	}
}
