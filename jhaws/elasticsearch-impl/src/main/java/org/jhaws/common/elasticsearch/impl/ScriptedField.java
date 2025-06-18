package org.jhaws.common.elasticsearch.impl;


import java.util.HashMap;
import java.util.Map;

public class ScriptedField {
    private String name;

    private String script;

    private Map<String, Object> parameters = new HashMap<>();

    public ScriptedField() {
        super();
    }

    public ScriptedField(String name, String script) {
        this.name = name;
        this.script = script;
    }

    public ScriptedField parameter(String name, Object value) {
        parameters.put(name, value);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
