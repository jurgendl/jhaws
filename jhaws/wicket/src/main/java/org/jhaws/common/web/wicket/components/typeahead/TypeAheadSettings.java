package org.jhaws.common.web.wicket.components.typeahead;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings({ "serial", "hiding" })
public class TypeAheadSettings implements Serializable {
	/** javascript debug voor typeahead */
	Boolean debug;

	/** rest path voor call */
	String rest;

	/** extra rest url query key=value voor rest call */
	Map<String, Object> extraRestParameters;

	/** in het object zit de collectie van waarden op dit pad, mag null zijn */
	String restReturnPath;

	/** i18n: empty, mag null zijn */
	String emptyText;

	/** i18n: more, mag null zijn */
	String moreText;

	/** i18n: add, mag null zijn */
	String newText;

	/** leegt bij verlaten zoeken */
	Boolean onBlur;

	/** er laten uit zien als knop of geween tekst */
	Boolean newAsButton;

	int max = 10;

	String hasSuccessClass;

	Boolean resetOnTriggerOnSelect;

	String placeholder;

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public TypeAheadSettings placeholder(String placeholder) {
		this.placeholder = placeholder;
		return this;
	}

	public Boolean getResetOnTriggerOnSelect() {
		return resetOnTriggerOnSelect;
	}

	public void setResetOnTriggerOnSelect(Boolean restOnTriggerOnSelect) {
		this.resetOnTriggerOnSelect = restOnTriggerOnSelect;
	}

	public TypeAheadSettings resetOnTriggerOnSelect(Boolean restOnTriggerOnSelect) {
		this.resetOnTriggerOnSelect = restOnTriggerOnSelect;
		return this;
	}

	public String getHasSuccessClass() {
		return hasSuccessClass;
	}

	public void setHasSuccessClass(String hasSuccessClass) {
		this.hasSuccessClass = hasSuccessClass;
	}

	public TypeAheadSettings hasSuccessClass(String hasSuccessClass) {
		this.hasSuccessClass = hasSuccessClass;
		return this;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public TypeAheadSettings max(int max) {
		this.max = max;
		return this;
	}

	public Boolean getDebug() {
		return this.debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public String getRest() {
		return this.rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}

	public Map<String, Object> getExtraRestParameters() {
		return this.extraRestParameters;
	}

	public void setExtraRestParameters(Map<String, Object> extraRestParameters) {
		this.extraRestParameters = extraRestParameters;
	}

	public String getRestReturnPath() {
		return this.restReturnPath;
	}

	public void setRestReturnPath(String restReturnPath) {
		this.restReturnPath = restReturnPath;
	}

	public TypeAheadSettings debug(Boolean debug) {
		this.debug = debug;
		return this;
	}

	public TypeAheadSettings rest(String rest) {
		this.rest = rest;
		return this;
	}

	public TypeAheadSettings extraRestParameters(Map<String, Object> extraRestParameters) {
		this.extraRestParameters = extraRestParameters;
		return this;
	}

	public TypeAheadSettings restReturnPath(String restReturnPath) {
		this.restReturnPath = restReturnPath;
		return this;
	}

	public String getNewText() {
		return this.newText;
	}

	public void setNewText(String newText) {
		this.newText = newText;
	}

	public String getEmptyText() {
		return this.emptyText;
	}

	public void setEmptyText(String emptyText) {
		this.emptyText = emptyText;
	}

	public String getMoreText() {
		return this.moreText;
	}

	public void setMoreText(String moreText) {
		this.moreText = moreText;
	}

	public TypeAheadSettings newText(String newText) {
		this.newText = newText;
		return this;
	}

	public TypeAheadSettings emptyText(String emptyText) {
		this.emptyText = emptyText;
		return this;
	}

	public TypeAheadSettings moreText(String moreText) {
		this.moreText = moreText;
		return this;
	}

	public Boolean getOnBlur() {
		return this.onBlur;
	}

	public void setOnBlur(Boolean onBlur) {
		this.onBlur = onBlur;
	}

	public TypeAheadSettings onBlur(Boolean onBlur) {
		this.onBlur = onBlur;
		return this;
	}

	public Boolean getNewAsButton() {
		return this.newAsButton;
	}

	public void setNewAsButton(Boolean newAsButton) {
		this.newAsButton = newAsButton;
	}

	public TypeAheadSettings newAsButton(Boolean newAsButton) {
		this.newAsButton = newAsButton;
		return this;
	}
}