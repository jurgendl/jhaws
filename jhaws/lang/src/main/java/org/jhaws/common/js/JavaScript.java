package org.jhaws.common.js;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JavaScript {
	public static final ScriptEngineManager factory = new ScriptEngineManager();
	public static final ScriptEngine engine = factory.getEngineByName("JavaScript");

	@SuppressWarnings("unchecked")
	public static <T> T run(String script) {
		try {
			return ((T) engine.eval(script));
		} catch (ScriptException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static <T> T run(Class<T> cast, String script) {
		return cast.cast(JavaScript.<T>run(script));
	}
}
