package org.jhaws.common.web.wicket.components.typeahead;

/** interne config, bevat ook velden die door typeaheadrest gezet worden maar niet door developer */
@SuppressWarnings({ "serial" })
class TypeAheadSettingsJs extends TypeAheadSettings {
	/** hidden uid/id field markupid, wordt gezet door typeaheadrest zelf */
	String targetField;

	/** component input field markupid, wordt gezet door typeaheadrest zelf */
	String field;

	/** button markup id die getriggerd moet worden bij selecteren van waarde, wordt gezet door typeaheadrest zelf */
	String triggerOnSelect;

	/** button markup id die getriggerd moet worden bij nieuw aanklikken, wordt gezet door typeaheadrest zelf */
	String triggerOnNew;

	public String getTargetField() {
		return this.targetField;
	}

	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}

	public String getField() {
		return this.field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTriggerOnNew() {
		return this.triggerOnNew;
	}

	public void setTriggerOnNew(String triggerOnNew) {
		this.triggerOnNew = triggerOnNew;
	}

	public String getTriggerOnSelect() {
		return this.triggerOnSelect;
	}

	public void setTriggerOnSelect(String triggerOnSelect) {
		this.triggerOnSelect = triggerOnSelect;
	}
}