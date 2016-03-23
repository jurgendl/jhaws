package org.jhaws.common.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class MatchImpl implements Match {
	private final String group;

	private final int groupCount;

	private final int start;

	private final int end;

	private final List<String> groups = new ArrayList<>();

	private final Map<Integer, String> int_group = new HashMap<>();

	// private final Map<String, String> string_group = new HashMap<>();

	private final Map<Integer, Integer> int_start = new HashMap<>();

	// private final Map<String, Integer> string_start = new HashMap<>();

	private final Map<Integer, Integer> int_end = new HashMap<>();

	// private final Map<String, Integer> string_end = new HashMap<>();

	public MatchImpl(Matcher matcher) {
		group = matcher.group();
		groupCount = matcher.groupCount();
		start = matcher.start();
		end = matcher.end();
		for (int i = 1; i < groupCount; i++) {
			String t = matcher.group(i);
			groups.add(t);
			int_group.put(i, t);
			int_start.put(i, matcher.start(i));
			int_end.put(i, matcher.end(i));
		}
	}

	@Override
	public String toString() {
		return start + ":" + end + ":" + group;
	}

	@Override
	public String group() {
		return group;
	}

	@Override
	public String group(int i) {
		return int_group.get(i);
	}

	@Override
	public String group(String name) {
		// return string_group.get(name);
		throw new UnsupportedOperationException();
	}

	@Override
	public int groupCount() {
		return groupCount;
	}

	@Override
	public int start() {
		return start;
	}

	@Override
	public int start(int i) {
		return int_start.get(i);
	}

	@Override
	public int start(String name) {
		// return string_start.get(name);
		throw new UnsupportedOperationException();
	}

	@Override
	public int end() {
		return end;
	}

	@Override
	public int end(int i) {
		return int_end.get(i);
	}

	@Override
	public int end(String name) {
		// return string_end.get(name);
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> groups() {
		return groups;
	}
}
