package org.jhaws.common.web.wicket.components.typeahead;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.components.EnhancedWebMarkupContainer;
import org.jhaws.common.web.wicket.jquery_typeahead.JqueryTypeAhead;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// https://styleguide.ugent.be/websites/formulieren.html#checkbox
// https://www.w3schools.com/bootstrap/bootstrap_collapse.asp
// http://www.runningcoder.org/jquerytypeahead/demo/
// https://css-tricks.com/snippets/css/a-guide-to-flexbox/
// https://www.javascripttutorial.net/es6/javascript-map/
// http://www.java2s.com/ref/javascript/javascript-map-merge.html
// https://developer.mozilla.org/en-US/docs/Web/CSS/animation?retiredLocale=nl
// https://css-tricks.com/css-animation-libraries/
// https://tachyons.io/
// https://animista.net/play/background/color-change
// https://cubic-bezier.com/#.57,.1,.66,.37
//
//
//
//
// TypeAheadConfig options = new TypeAheadConfig();
// options.setDebug(true);
// options.setField("#" + typeAheadInput.getMarkupId());
// options.setRest("/persoon/typeahead/");
// options.setExtraRestParameters(Collections.singletonMap("type", "personen"));
// options.setRestReturnPath("results");
// options.setTargetField("#" + bestaandePersoonToevoegenUid.getMarkupId() /* "persoonLijstUid" */);
// options.setEmpty("-- Geen resultaten. --");
// options.setMore("-- Meer resultaten gevonden ({{total}}), verfijn eventueel uw zoekterm. --");
@SuppressWarnings("serial")
public abstract class TypeAheadPanel extends Panel {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(TypeAheadPanel.class, "TypeAheadPanel.js").addJavaScriptResourceReferenceDependency(JqueryTypeAhead.JS);

	public static final CssResourceReference CSS = new CssResourceReference(TypeAheadPanel.class, "TypeAheadPanel.css").addCssResourceReferenceDependency(JqueryTypeAhead.CSS);

	public static final String TYPEAHEAD_FACTORY_FUNCTION = "typeaheadFactory({})";

	public static final String TYPEAHEAD_ZOEKEN_KNOP_ONCLICK = "event.preventDefault();typeaheadOpnieuwZoeken(this);";

	public static final String TYPEAHEAD_NEW_KNOP_ONCLICK = "event.preventDefault();$($(this).attr('data-on-click')).click();";

	public static final String TYPEAHEAD_TOGGLE_KNOP_ONCLICK = "event.preventDefault();toggleTypeAheadZoekenOfToevoegenMode(this);";

	public static final JavaScriptResourceReference JS_TOGGLE_NIEUW_TOEVOEGEN = new JavaScriptResourceReference(TypeAheadPanel.class, "TypeAheadRestToggleNieuwToevoegen.js").addJavaScriptResourceReferenceDependency(JS);

	@SpringBean
	private ObjectMapper objectMapper;

	private TextField<String> hiddenFieldUid;

	private TextField<String> typeAheadInput;

	private IModel<String> hiddenFieldUidModel;

	private IModel<String> typeAheadInputModel;

	private Button typeAheadZoeken;

	private AjaxSubmitLink typeAheadResultaatToevoegen;

	private TypeAheadSettings options;

	private EnhancedWebMarkupContainer typeAhead;

	private AjaxLink<String> typeAheadNieuw;

	public TypeAheadPanel(String id, TypeAheadSettings options) {
		super(id);

		this.options = options;

		setOutputMarkupId(true);

		typeAhead = new EnhancedWebMarkupContainer("typeAhead") {
			@Override
			public void renderHead(IHeaderResponse response) {
				try {
					String cfgToString = objectMapper.writeValueAsString(options);
					TypeAheadSettingsJs jsConfig = objectMapper.readValue(cfgToString, TypeAheadSettingsJs.class);
					jsConfig.setField("#" + typeAheadInput.getMarkupId());
					jsConfig.setTargetField("#" + hiddenFieldUid.getMarkupId());
					jsConfig.setTriggerOnSelect("#" + typeAheadResultaatToevoegen.getMarkupId());
					jsConfig.setTriggerOnNew("#" + typeAheadNieuw.getMarkupId());
					cfgToString = objectMapper.writeValueAsString(jsConfig);
					System.out.println(cfgToString);
					response.render(OnDomReadyHeaderItem.forScript(";" + TYPEAHEAD_FACTORY_FUNCTION.replace("{}", cfgToString) + ";"));
				} catch (JsonProcessingException ex) {
					throw new RuntimeException(ex);
				}
			}
		};
		typeAhead.setOutputMarkupId(true);
		add(typeAhead);

		hiddenFieldUidModel = Model.of();
		hiddenFieldUid = new TextField<>("hiddenFieldUid", hiddenFieldUidModel);
		hiddenFieldUid.setOutputMarkupId(true);
		typeAhead.add(hiddenFieldUid);

		// is onzichtbaar in html maar niet wicket:setvisible(false) want dan staat die er totaal niet op
		// wordt getriggert bij onclick van een zoekresultaat
		// krijgt dan identifier (uid/id) terug van hidden field
		// trigger te overidden methode "typeAheadResultaatToevoegen(AjaxRequestTarget,uuid)"
		typeAheadResultaatToevoegen = new AjaxSubmitLink("typeAheadResultaatToevoegen") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				super.onSubmit(target);
				typeAheadResultaatToevoegen(target, getUniqueIdentifier());
			}
		};
		typeAheadResultaatToevoegen.setOutputMarkupId(true);
		typeAhead.add(typeAheadResultaatToevoegen);

		// is onzichtbaar in html maar niet wicket:setvisible(false) want dan staat die er totaal niet op
		// wordt getriggert bij onclick van "nieuw" balkeje onderaan
		// trigger te overidden methode "typeAheadNieuw(AjaxRequestTarget)"
		typeAheadNieuw = new AjaxLink<>("typeAheadNieuw") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				typeAheadNieuw(target);
			}
		};
		typeAheadNieuw.setOutputMarkupId(true);
		typeAhead.add(typeAheadNieuw);

		typeAheadInputModel = Model.of();
		typeAheadInput = new TextField<>("typeAheadInput", typeAheadInputModel);
		typeAheadInput.setOutputMarkupId(true);
		typeAhead.add(typeAheadInput);

		typeAheadZoeken = new Button("typeAheadZoeken");
		// deze javascript call zit ook in
		// be.ugent.gismo.web.wicket.CSPHelper.setupCustomWeb(WebApplication)
		typeAheadZoeken.add(new AttributeModifier("onclick", TYPEAHEAD_ZOEKEN_KNOP_ONCLICK));
		typeAheadZoeken.setOutputMarkupId(true);
		typeAhead.add(typeAheadZoeken);
	}

	abstract protected void typeAheadNieuw(AjaxRequestTarget target);

	abstract protected void typeAheadResultaatToevoegen(AjaxRequestTarget target, String uniqueIdentifier);

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(CssHeaderItem.forReference(JqueryTypeAhead.CSS));
		response.render(CssHeaderItem.forReference(TypeAheadPanel.CSS));
		response.render(JavaScriptHeaderItem.forReference(JqueryTypeAhead.JS));
		response.render(JavaScriptHeaderItem.forReference(TypeAheadPanel.JS));
		renderToggleNieuwToevoegen(response);
	}

	protected void renderToggleNieuwToevoegen(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(TypeAheadPanel.JS_TOGGLE_NIEUW_TOEVOEGEN));
	}

	public void reset() {
		typeAheadInputModel.setObject(null);
		hiddenFieldUidModel.setObject(null);
	}

	public String getUniqueIdentifier() {
		return hiddenFieldUidModel.getObject();
	}

	public TextField<String> getTypeAheadInput() {
		return this.typeAheadInput;
	}

	public IModel<String> getTypeAheadInputModel() {
		return this.typeAheadInputModel;
	}

	public TextField<String> getHiddenFieldUid() {
		return this.hiddenFieldUid;
	}

	public IModel<String> getHiddenFieldUidModel() {
		return this.hiddenFieldUidModel;
	}

	public Button getTypeAheadZoeken() {
		return this.typeAheadZoeken;
	}

	public AjaxSubmitLink getTypeAheadResultaatToevoegen() {
		return this.typeAheadResultaatToevoegen;
	}

	public TypeAheadSettings getOptions() {
		return this.options;
	}

	public EnhancedWebMarkupContainer getTypeAhead() {
		return this.typeAhead;
	}

	public AjaxLink<String> getTypeAheadNieuw() {
		return this.typeAheadNieuw;
	}
}