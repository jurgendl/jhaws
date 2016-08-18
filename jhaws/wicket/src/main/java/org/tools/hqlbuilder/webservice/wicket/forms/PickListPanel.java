package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://www.primefaces.org/primeui/picklist.html
 */
public class PickListPanel<T extends Serializable> extends FormRowPanel<Collection<T>, Collection<T>, ListMultipleChoice<T>, ListSettings> {
    private static final long serialVersionUID = 7874409832671834238L;

    public static final String SOURCEID = "source";

    public static final String PICKLISTID = "picklist";

    public static final String TARGETID = "target";

    protected IModel<List<T>> choices;

    protected IChoiceRenderer<T> renderer;

    protected WebMarkupContainer pickList;

    protected ListMultipleChoice<T> source;

    protected PickListPanel(IModel<?> model, Collection<T> propertyPath, FormSettings formSettings, ListSettings componentSettings,
            IModel<List<T>> choices, final IChoiceRenderer<T> renderer) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = new IChoiceRenderer<T>() {
            private static final long serialVersionUID = 9002126185670506302L;

            @Override
            public String toString() {
                return "IChoiceRenderer<T>";
            }

            @Override
            public Object getDisplayValue(T object) {
                Object tmp = object;
                if (renderer != null) {
                    tmp = renderer.getDisplayValue(object);
                }
                return String.valueOf(tmp);
            }

            @Override
            public String getIdValue(T object, int index) {
                Object displayValue = getDisplayValue(object);
                return String.valueOf(displayValue);
            }
        };
    }

    @Override
    protected ListMultipleChoice<T> createComponent(final IModel<Collection<T>> selection, Class<Collection<T>> valueType) {
        IModel<List<T>> selectionListModel = new IModel<List<T>>() {
            private static final long serialVersionUID = 7030770200558777598L;

            @Override
            public String toString() {
                return "IModel<List<T>> selectionListModel";
            }

            @Override
            public void detach() {
                selection.detach();
            }

            @Override
            public List<T> getObject() {
                return new ArrayList<T>(selection.getObject());
            }

            @Override
            public void setObject(List<T> object) {
                IModel<Collection<T>> _selection = selection;
                Collection<T> selectionObject = _selection.getObject();
                selectionObject.clear();
                selectionObject.addAll(object);
            }
        };
        ListMultipleChoice<T> target = new ListMultipleChoice<T>(TARGETID, selectionListModel, selectionListModel, renderer) {
            private static final long serialVersionUID = -4637460687004256289L;

            @Override
            public String toString() {
                return "ListMultipleChoice<T>";
            }

            @Override
            protected List<T> convertChoiceIdsToChoices(String[] ids) {
                ArrayList<T> selectedValues = new ArrayList<T>();

                // If one or more ids is selected
                if (ids != null && ids.length > 0 && !Strings.isEmpty(ids[0])) {
                    // Get values that could be selected
                    final Map<String, T> choiceIds2choiceValues = createChoicesIdsMap();

                    // Loop through selected indices
                    for (String id : ids) {
                        if (choiceIds2choiceValues.containsKey(id)) {
                            selectedValues.add(choiceIds2choiceValues.get(id));
                        }
                    }
                }
                return selectedValues;
            }

            private Map<String, T> createChoicesIdsMap() {
                final List<? extends T> _choices = getChoices();
                List<T> otherOptions = choices.getObject();
                final Map<String, T> choiceIds2choiceValues = new HashMap<String, T>(_choices.size() + otherOptions.size(), 1);
                for (int index = 0; index < _choices.size(); index++) {
                    // Get next choice
                    final T choice = _choices.get(index);
                    choiceIds2choiceValues.put(getChoiceRenderer().getIdValue(choice, index), choice);
                }
                for (int index = 0; index < otherOptions.size(); index++) {
                    // Get next choice
                    final T choice = otherOptions.get(index);
                    choiceIds2choiceValues.put(getChoiceRenderer().getIdValue(choice, index), choice);
                }
                return choiceIds2choiceValues;
            }
        };
        return target;
    }

    @Override
    protected FormRowPanel<Collection<T>, Collection<T>, ListMultipleChoice<T>, ListSettings> addComponents() {
        this.add(getLabel());
        this.add(getPickList().add(getSource()).add(getComponent()));
        this.add(getRequiredMarker());
        this.add(getFeedback());
        return this;
    }

    public WebMarkupContainer getPickList() {
        if (pickList == null) {
            pickList = new WebMarkupContainer(PICKLISTID);
            pickList.setOutputMarkupId(true);
            pickList.add(new CssClassNameAppender(PrimeUI.puipicklist));
        }
        return pickList;
    }

    public ListMultipleChoice<T> getSource() {
        if (source == null) {
            source = new ListMultipleChoice<T>(SOURCEID, choices, choices, renderer);
        }
        return source;
    }

    public ListMultipleChoice<T> getTarget() {
        return super.getComponent();
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
    }

    @Override
    public IModel<Collection<T>> getValueModel() {
        if (valueModel == null) {
            String property = getPropertyName();
            valueModel = property == null ? null : new PropertyModel<Collection<T>>(getDefaultModel(), property);
        }
        return valueModel;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Class<Collection<T>> getPropertyType() {
        return (Class) Collection.class;
    }

    public IChoiceRenderer<T> getRenderer() {
        return this.renderer;
    }
}
