package org.jhaws.common.lang;

import java.util.List;

public interface Match {
	public String group();

	public String group(int group);

	public String group(String name);

	public int groupCount();

	public int start();

	public int start(int group);

	public int start(String name);

	public int end();

	public int end(int group);

	public int end(String name);

	public List<String> groups();
}