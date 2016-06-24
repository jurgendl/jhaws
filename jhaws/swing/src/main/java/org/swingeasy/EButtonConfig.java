package org.swingeasy;

import javax.swing.Action;
import javax.swing.Icon;

/**
 * @author Jurgen
 */
public class EButtonConfig extends EComponentConfig<EButtonConfig> {
	protected EButtonCustomizer buttonCustomizer;

	protected Action action;

	protected Icon icon;

	protected String text;

	public EButtonConfig() {
		super();
	}

	public EButtonConfig(Action a) {
		action = a;
	}

	public EButtonConfig(EButtonCustomizer ebc) {
		buttonCustomizer = ebc;
	}

	public EButtonConfig(EButtonCustomizer ebc, Action a) {
		buttonCustomizer = ebc;
		action = a;
	}

	public EButtonConfig(EButtonCustomizer ebc, Icon icon) {
		buttonCustomizer = ebc;
		this.icon = icon;
	}

	public EButtonConfig(EButtonCustomizer ebc, String text) {
		buttonCustomizer = ebc;
		this.text = text;
	}

	public EButtonConfig(EButtonCustomizer ebc, String text, Icon icon) {
		buttonCustomizer = ebc;
		this.text = text;
		this.icon = icon;
	}

	public EButtonConfig(Icon icon) {
		this.icon = icon;
	}

	public EButtonConfig(String text) {
		this.text = text;
	}

	public EButtonConfig(String text, Icon icon) {
		this.text = text;
		this.icon = icon;
	}

	public Action getAction() {
		return action;
	}

	public EButtonCustomizer getButtonCustomizer() {
		return buttonCustomizer;
	}

	public Icon getIcon() {
		return icon;
	}

	public String getText() {
		return text;
	}

	public EButtonConfig setAction(Action action) {
		lockCheck();
		this.action = action;
		return this;
	}

	public EButtonConfig setButtonCustomizer(EButtonCustomizer buttonCustomizer) {
		lockCheck();
		this.buttonCustomizer = buttonCustomizer;
		return this;
	}

	public EButtonConfig setIcon(Icon icon) {
		lockCheck();
		this.icon = icon;
		return this;
	}

	public EButtonConfig setText(String text) {
		lockCheck();
		this.text = text;
		return this;
	}
}
