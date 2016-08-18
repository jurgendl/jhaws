package org.tools.hqlbuilder.webservice.wicket.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.IExportableColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class TableColumn<T, D> implements IStyledColumn<T, String>, IExportableColumn<T, String, D> {
    private static final long serialVersionUID = -7609015288627705664L;

    public static final String SERVER_SORTABLE = "serversortable";

    public static final String CLIENT_SORTABLE = "clientsortable";

    public static final String UNSORTABLE = "unsortable";

    protected IModel<String> displayModel;

    protected Side filtering = Side.none;

    protected Side sorting = Side.none;

    protected String propertyExpression;

    protected boolean dataTag = false;

    protected boolean escapeModelStrings = true;

    public TableColumn() {
        super();
    }

    public TableColumn(IModel<String> displayModel, String propertyExpression) {
        this();
        this.setDisplayModel(displayModel);
        this.setPropertyExpression(propertyExpression);
    }

    /**
     * @see org.apache.wicket.model.IDetachable#detach()
     */
    @Override
    public void detach() {
        if (this.displayModel != null) {
            this.displayModel.detach();
        }
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn#getCssClass()
     */
    @Override
    public String getCssClass() {
        if (this.sorting == Side.client) {
            return TableColumn.CLIENT_SORTABLE;
        }
        if (this.sorting == Side.server) {
            return TableColumn.SERVER_SORTABLE;
        }
        return TableColumn.UNSORTABLE;
    }

    /**
     * Factory method for generating a model that will generated the displayed value. Typically the model is a property model using the
     * {@link #propertyExpression} specified in the constructor.
     */
    @Override
    public IModel<D> getDataModel(IModel<T> rowModel) {
        PropertyModel<D> propertyModel = new PropertyModel<D>(rowModel, this.propertyExpression);
        return propertyModel;
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.table.export.IExportableColumn#getDisplayModel()
     */
    @Override
    public IModel<String> getDisplayModel() {
        return this.displayModel;
    }

    public Side getFiltering() {
        return this.filtering;
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn#getHeader(java.lang.String)
     */
    @Override
    public Component getHeader(final String componentId) {
        Label label = new Label(componentId, this.getDisplayModel());
        return label;
    }

    /**
     * @return wicket property expression
     */
    public String getPropertyExpression() {
        return this.propertyExpression;
    }

    public Side getSorting() {
        return this.sorting;
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn#getSortProperty()
     */
    @Override
    public String getSortProperty() {
        return this.sorting == Side.server ? this.propertyExpression : null;
    }

    public boolean isDataTag() {
        return this.dataTag;
    }

    public boolean isEscapeModelStrings() {
        return this.escapeModelStrings;
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn#isSortable()
     */
    @Override
    public boolean isSortable() {
        return this.getSortProperty() != null;
    }

    /**
     * Implementation of populateItem which adds a label to the cell whose model is the provided property expression evaluated against rowModelObject
     *
     * @see ICellPopulator#populateItem(Item, String, IModel)
     */
    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        IModel<D> dataModel = this.getDataModel(rowModel);
        Label label = new Label(componentId, dataModel);
        label.setEscapeModelStrings(this.escapeModelStrings);
        item.add(label);
        if (this.dataTag) {
            item.add(new AttributeModifier("data-" + this.propertyExpression, String.valueOf(dataModel.getObject())));
        }
    }

    public TableColumn<T, D> setDataTag(boolean dataTag) {
        this.dataTag = dataTag;
        return this;
    }

    public void setDisplayModel(IModel<String> displayModel) {
        this.displayModel = displayModel;
    }

    public TableColumn<T, D> setEscapeModelStrings(boolean escapeModelStrings) {
        this.escapeModelStrings = escapeModelStrings;
        return this;
    }

    public TableColumn<T, D> setFiltering(Side filtering) {
        this.filtering = filtering;
        return this;
    }

    public void setPropertyExpression(String propertyExpression) {
        this.propertyExpression = propertyExpression;
    }

    public TableColumn<T, D> setSorting(Side sorting) {
        this.sorting = sorting;
        return this;
    }
}
