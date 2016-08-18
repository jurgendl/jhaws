package org.tools.hqlbuilder.webservice.wicket.tables;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationIncrementLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.sort.AjaxFallbackOrderByLink;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.string.Strings;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.jquery.ui.tablesorter.TableSorter;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.WeLoveIcons;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;
import org.tools.hqlbuilder.webservice.wicket.components.CheckBoxPanel;
import org.tools.hqlbuilder.webservice.wicket.components.LinkPanel;
import org.tools.hqlbuilder.webservice.wicket.components.LinkPanel.LinkType;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

/**
 * @see https ://www.packtpub.com/article/apache-wicket-displaying-data-using-datatable
 * @see http://wicketinaction.com/2008/10/building-a-listeditor-form-component/
 */
public class Table<T extends Serializable> extends AjaxFallbackDefaultDataTable<T, String> {
    protected static abstract class ActionsPanel<T extends Serializable> extends Panel {
        private static final long serialVersionUID = -5249593513368522879L;

        public ActionsPanel(String id, final T object, DataProvider<T> dataProvider) {
            super(id);
            this.setOutputMarkupId(true);
            @SuppressWarnings("unchecked")
            final Form<T> form = (Form<T>) this.getParent();
            if (dataProvider.canEdit()) {
                AjaxFallbackButton editLink = new AjaxFallbackButton(Table.ACTIONS_EDIT_ID, form) {
                    private static final long serialVersionUID = 2401036651703118413L;

                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> f) {
                        ActionsPanel.this.onEdit(target, object);
                    }
                };
                this.add(editLink);
            } else {
                this.add(new WebMarkupContainer(Table.ACTIONS_EDIT_ID).setVisible(false));
            }
            if (dataProvider.canDelete()) {
                AjaxFallbackButton deleteLink = new AjaxFallbackButton(Table.ACTIONS_DELETE_ID, form) {
                    private static final long serialVersionUID = 8838151595047275051L;

                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> f) {
                        ActionsPanel.this.onDelete(target, object);
                    }
                };
                this.add(deleteLink);
            } else {
                this.add(new WebMarkupContainer(Table.ACTIONS_DELETE_ID).setVisible(false));
            }
        }

        /**
         * add table to AjaxRequestTarget and make service call to delete object
         */
        public abstract void onDelete(AjaxRequestTarget target, T object);

        /**
         * add table to AjaxRequestTarget
         */
        public abstract void onEdit(AjaxRequestTarget target, T object);
    }

    /** see AjaxFallbackOrderByBorder, to change sorting */
    protected abstract class AjaxFallbackOrderBy extends Border {
        private static final long serialVersionUID = 212283337538504257L;

        public AjaxFallbackOrderBy(final String id, final String sortProperty, final ISortStateLocator<String> stateLocator,
                final AjaxFallbackOrderByLink.ICssProvider<String> cssProvider, final IAjaxCallListener ajaxCallListener) {
            super(id);
            AjaxFallbackOrderByLink<String> link = new AjaxFallbackOrderByLink<String>("orderByLink", sortProperty, stateLocator, cssProvider,
                    ajaxCallListener) {
                private static final long serialVersionUID = -6094915237038098719L;

                @Override
                protected SortOrder nextSortOrder(final SortOrder order) {
                    switch (order) {
                        default: // <null>
                        case NONE:
                            return SortOrder.ASCENDING;
                        case ASCENDING:
                            return SortOrder.DESCENDING;
                        case DESCENDING:
                            return SortOrder.NONE;
                    }
                }

                @Override
                public void onClick(final AjaxRequestTarget target) {
                    AjaxFallbackOrderBy.this.onAjaxClick(target);
                }

                @Override
                protected void onSortChanged() {
                    AjaxFallbackOrderBy.this.onSortChanged();
                }
            };
            this.addToBorder(link);
            this.add(new AjaxFallbackOrderByLink.CssModifier<String>(link, cssProvider));
            link.add(this.getBodyContainer());
        }

        public AjaxFallbackOrderBy(final String id, final String sortProperty, final ISortStateLocator<String> stateLocator,
                final IAjaxCallListener ajaxCallListener) {
            this(id, sortProperty, stateLocator, new AjaxFallbackOrderByLink.DefaultCssProvider<String>(), ajaxCallListener);
        }

        protected abstract void onAjaxClick(final AjaxRequestTarget target);

        /**
         * This method is a hook for subclasses to perform an action after sort has changed
         */
        protected abstract void onSortChanged();
    }

    public class BottomToolbar extends AbstractToolbar {
        private static final long serialVersionUID = -8277730819874510969L;

        public BottomToolbar(final DataTable<T, String> table, final DataProvider<T> dataProvider) {
            super(table);

            WebMarkupContainer td = new WebMarkupContainer("td");
            this.add(td);

            td.add(AttributeModifier.replace("colspan", new AbstractReadOnlyModel<String>() {
                private static final long serialVersionUID = 5599883778610261348L;

                @Override
                public String getObject() {
                    return String.valueOf(table.getColumns().size());
                }
            }));
            Label norecordsfoundLabel = new Label("norecordsfound", new ResourceModel("norecordsfound"));
            norecordsfoundLabel.setVisible(this.getTable().getRowCount() == 0);
            td.add(norecordsfoundLabel);

            // ugly hack, parent='tableform' is set during form.add(table) which
            // happens later and we need the form here
            // possible fix 1: put this in a form (form within form)
            // possible fix 2: adjust AjaxFallbackButton to fetch form at the
            // moment of execution later so it is not needed during contruction
            Form<?> form = ((DelegateDataProvider<T>) Table.this.getDataprovider()).getForm();
            Table.this.addLink = new AjaxFallbackButton(Table.ACTIONS_ADD_ID, form) {
                private static final long serialVersionUID = -8033338314334624474L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> f) {
                    dataProvider.add(target);
                }
            };
            td.add(Table.this.addLink);

            RepeatingView extraActions = new RepeatingView("extraActions");
            td.add(extraActions);
        }
    }

    protected static abstract class CssAttributeBehavior extends Behavior {
        private static final long serialVersionUID = 1159801773515376493L;

        protected abstract String getCssClass();

        /**
         * @see Behavior#onComponentTag(Component, ComponentTag)
         */
        @Override
        public void onComponentTag(final Component component, final ComponentTag tag) {
            String className = this.getCssClass();
            if (!Strings.isEmpty(className)) {
                tag.append("class", className, " ");
            }
        }
    }

    protected class DefaultDataGridView extends DataGridView<T> {
        private static final long serialVersionUID = -5612396859737033644L;

        public DefaultDataGridView(String id, List<? extends IColumn<T, String>> columns, IDataProvider<T> dataProvider) {
            super(id, columns, dataProvider);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        protected Item newCellItem(final String id, final int index, final IModel model) {
            Item item = Table.this.newCellItem(id, index, model);
            // final TableColumn<T> column = (TableColumn<T>) Table.this.getColumns().get(index);
            // if (column instanceof IStyledColumn) {
            // item.add(new CssAttributeBehavior() {
            // private static final long serialVersionUID = -8376202471270737937L;
            //
            // @Override
            // protected String getCssClass() {
            // return ((IStyledColumn<T, String>) column).getCssClass();
            // }
            // });
            // }
            return item;
        }

        @Override
        protected Item<T> newRowItem(final String id, final int index, final IModel<T> model) {
            return Table.this.newRowItem(id, index, model);
        }
    }

    /* ugly hack */
    private static class DelegateDataProvider<T extends Serializable> implements DataProvider<T> {
        private static final long serialVersionUID = -3517388036834640180L;

        private final Form<?> form;

        private final DataProvider<T> delegate;

        public DelegateDataProvider(Form<?> form, DataProvider<T> delegate) {
            this.delegate = delegate;
            this.form = form;
        }

        @Override
        public void add(AjaxRequestTarget target) {
            this.delegate.add(target);
        }

        @Override
        public boolean canAdd() {
            return this.delegate.canAdd();
        }

        @Override
        public boolean canDelete() {
            return this.delegate.canDelete();
        }

        @Override
        public boolean canEdit() {
            return this.delegate.canEdit();
        }

        @Override
        public void delete(AjaxRequestTarget target, T object) {
            this.delegate.delete(target, object);
        }

        @Override
        public void detach() {
            this.delegate.detach();
        }

        @Override
        public void edit(AjaxRequestTarget target, T object) {
            this.delegate.edit(target, object);
        }

        @Override
        public String getAjaxRefreshMethod() {
            return this.delegate.getAjaxRefreshMethod();
        }

        @Override
        public int getAjaxRefreshSeconds() {
            return this.delegate.getAjaxRefreshSeconds();
        }

        @Override
        public String getAjaxRefreshUrl() {
            return this.delegate.getAjaxRefreshUrl();
        }

        public Form<?> getForm() {
            return this.form;
        }

        @Override
        public Serializable getId(IModel<T> model) {
            return this.delegate.getId(model);
        }

        @Override
        public String getIdProperty() {
            return this.delegate.getIdProperty();
        }

        @Override
        public int getRowsPerPage() {
            return this.delegate.getRowsPerPage();
        }

        @Override
        public ISortState<String> getSortState() {
            return this.delegate.getSortState();
        }

        @Override
        public boolean isStateless() {
            return this.delegate.isStateless();
        }

        @Override
        public Iterator<? extends T> iterator(long first, long count) {
            return this.delegate.iterator(first, count);
        }

        @Override
        public IModel<T> model(T object) {
            return this.delegate.model(object);
        }

        @Override
        public long size() {
            return this.delegate.size();
        }
    }

    public static class BooleanColumn<D> extends TableColumn<D, Boolean> {
        private static final long serialVersionUID = 4634739390630581195L;

        public BooleanColumn(IModel<String> displayModel, String propertyExpression) {
            this.setDisplayModel(displayModel);
            this.setPropertyExpression(propertyExpression);
        }

        @Override
        public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
            IModel<Boolean> dataModel = this.getDataModel(rowModel);
            CheckBoxPanel checkBoxPanel = new CheckBoxPanel(componentId, dataModel, null);
            checkBoxPanel.getField().setEnabled(false);
            checkBoxPanel.getField().add(new CssClassNameAppender(PrimeUI.puicheckbox));
            item.add(checkBoxPanel);
        }
    }

    public static class EmailColumn<D> extends TableColumn<D, String> {
        private static final long serialVersionUID = 4634739390630581195L;

        public EmailColumn(IModel<String> displayModel, String propertyExpression) {
            this.setDisplayModel(displayModel);
            this.setPropertyExpression(propertyExpression);
        }

        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
            IModel<String> dataModel = this.getDataModel(rowModel);
            IModel dataModelUncast = dataModel;
            IModel<String> dataModelCast = dataModelUncast;
            item.add(new LinkPanel(componentId, dataModelUncast, dataModelCast, LinkType.email));
        }
    }

    public class HeadersToolbar extends AjaxFallbackHeadersToolbar<String> {
        private static final long serialVersionUID = -8737070685949753385L;

        public HeadersToolbar(DataTable<?, String> table, ISortStateLocator<String> stateLocator) {
            super(table, stateLocator);
        }

        @Override
        protected WebMarkupContainer newSortableHeader(String borderId, String property, ISortStateLocator<String> locator) {
            return new AjaxFallbackOrderBy(borderId, property, locator, this.getAjaxCallListener()) {
                private static final long serialVersionUID = -7436736813608388408L;

                @Override
                protected void onAjaxClick(final AjaxRequestTarget target) {
                    target.add(HeadersToolbar.this.getTable());
                }

                @Override
                protected void onSortChanged() {
                    HeadersToolbar.this.getTable().setCurrentPage(0);
                }
            };
        }
    }

    public class PagingNavigator extends AjaxPagingNavigator {
        private static final long serialVersionUID = 1844950934466502565L;

        public PagingNavigator(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
            super(id, pageable, labelProvider);
        }

        protected void modifyButtonLinkActiveBehavior(final AbstractLink link) {
            link.add(new CssClassNameAppender(Table.this.CSS_ACTIVE_STYLE) {
                private static final long serialVersionUID = -4390935870504634276L;

                @Override
                public boolean isEnabled(Component component) {
                    return super.isEnabled(component) && !link.isEnabled();
                }
            });
            link.add(new CssClassNameRemover(Table.this.CSS_ACTIVE_STYLE) {
                private static final long serialVersionUID = -6666736063912700264L;

                @Override
                public boolean isEnabled(Component component) {
                    return super.isEnabled(component) && link.isEnabled();
                }
            });
        }

        protected void modifyButtonLinkDisableBehavior(final AbstractLink link) {
            link.add(new CssClassNameAppender(Table.this.CSS_DISABLED_STYLE) {
                private static final long serialVersionUID = -8703931679380496079L;

                @Override
                public boolean isEnabled(Component component) {
                    return super.isEnabled(component) && !link.isEnabled();
                }
            });
            link.add(new CssClassNameRemover(Table.this.CSS_DISABLED_STYLE) {
                private static final long serialVersionUID = -7077515156924411650L;

                @Override
                public boolean isEnabled(Component component) {
                    return super.isEnabled(component) && link.isEnabled();
                }
            });
        }

        @Override
        protected PagingNavigation newNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
            return new AjaxPagingNavigation(id, pageable, labelProvider) {
                private static final long serialVersionUID = -6446431226749147484L;

                @Override
                protected Link<?> newPagingNavigationLink(String id0, IPageable pageable0, long pageIndex) {
                    AjaxPagingNavigationLink link = new AjaxPagingNavigationLink(id0, pageable0, pageIndex);
                    if (pageable0.getCurrentPage() == pageIndex) {
                        PagingNavigator.this.modifyButtonLinkActiveBehavior(link);
                    } else {
                        PagingNavigator.this.modifyButtonLinkDisableBehavior(link);
                    }
                    return link;
                }
            };
        }

        @Override
        protected AbstractLink newPagingNavigationIncrementLink(String id, IPageable pageable, int increment) {
            AjaxPagingNavigationIncrementLink link = new AjaxPagingNavigationIncrementLink(id, pageable, increment);
            this.modifyButtonLinkDisableBehavior(link);
            return link;
        }

        @Override
        protected AbstractLink newPagingNavigationLink(String id, IPageable pageable, int pageNumber) {
            AjaxPagingNavigationLink link = new AjaxPagingNavigationLink(id, pageable, pageNumber);
            this.modifyButtonLinkDisableBehavior(link);
            return link;
        }
    }

    public class TopToolbar extends AjaxNavigationToolbar {
        private static final long serialVersionUID = 7871654433608259728L;

        public TopToolbar(final DataTable<T, String> table,  final DataProvider<T> dataProvider) {
            super(table);
        }

        @Override
        protected PagingNavigator newPagingNavigator(final String navigatorId, final DataTable<?, ?> table) {
            return new PagingNavigator(navigatorId, table, null);
        }
    }

    public static class URLColumn<D> extends TableColumn<D, URL> {
        private static final long serialVersionUID = -2998876473654238089L;

        public URLColumn(IModel<String> displayModel, String propertyExpression) {
            this.setDisplayModel(displayModel);
            this.setPropertyExpression(propertyExpression);
        }

        @Override
        public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
            item.add(new LinkPanel(componentId, this.getDataModel(rowModel), this.getDisplayModel(), LinkType.url));
        }
    }

    public static class URIColumn<D> extends TableColumn<D, URI> {
        private static final long serialVersionUID = -2998876473654238089L;

        public URIColumn(IModel<String> displayModel, String propertyExpression) {
            this.setDisplayModel(displayModel);
            this.setPropertyExpression(propertyExpression);
        }

        @Override
        public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
            item.add(new LinkPanel(componentId, this.getDataModel(rowModel), this.getDisplayModel(), LinkType.url));
        }
    }

    public static class URLStringColumn<D> extends TableColumn<D, String> {
        private static final long serialVersionUID = -2998876473654238089L;

        public URLStringColumn(IModel<String> displayModel, String propertyExpression) {
            this.setDisplayModel(displayModel);
            this.setPropertyExpression(propertyExpression);
        }

        @Override
        public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
            IModel<String> dataModel = this.getDataModel(rowModel);
            item.add(new LinkPanel(componentId, dataModel, this.getDisplayModel(), LinkType.url));
        }
    }

    private static final long serialVersionUID = -997730195881970840L;

    public static JavaScriptResourceReference JS_AJAX_UPDATE = new JavaScriptResourceReference(Table.class, "TableAjaxRefresh.js")
            .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static final String ACTIONS_DELETE_ID = "delete";

    public static final String ACTIONS_EDIT_ID = "edit";

    public static final String ACTIONS_ADD_ID = "add";

    protected String CSS_DISABLED_STYLE = "ui-state-active";

    protected String CSS_ACTIVE_STYLE = "ui-state-active";

    public static final String CSS_EVEN = "ui-widget-content pui-datatable-even ui-datatable-even";

    public static final String CSS_ODD = "ui-widget-content pui-datatable-odd ui-datatable-odd";

    protected String cssEven = Table.CSS_EVEN;

    protected String cssOdd = Table.CSS_ODD;

    protected AjaxFallbackButton addLink;

    protected BottomToolbar bottomToolbar;

    protected DataProvider<T> dataProvider;

    /** sorter-library id javascript object for this table; set during creation */
    protected String tableSortAjaxId;

    /** id javascript object for all tables; set before creation */
    protected String tableRefreshAjaxId = "tableAjaxRefresh";

    public Table(Form<?> form, String id, List<TableColumn<T, ?>> columns, final DataProvider<T> dataProvider) {
        super(id, columns, new DelegateDataProvider<T>(form, dataProvider), dataProvider.getRowsPerPage());
        this.dataProvider = dataProvider;
        this.addLink.setVisible(dataProvider.canAdd());
        this.setOutputMarkupId(true);
    }

    @Override
    public void addBottomToolbar(AbstractToolbar toolbar) {
        this.bottomToolbar = new BottomToolbar(this, this.getDataprovider());
        super.addBottomToolbar(this.bottomToolbar);
    }

    @Override
    public void addTopToolbar(AbstractToolbar toolbar) {
        if (toolbar instanceof AjaxNavigationToolbar) {
            // moved navigation to bottom
            super.addBottomToolbar(new TopToolbar(this, this.getDataprovider()));
        } else if (toolbar instanceof AjaxFallbackHeadersToolbar) {
            super.addTopToolbar(new HeadersToolbar(this, this.getDataprovider()));
        }
    }

    public BottomToolbar getBottomToolbar() {
        return this.bottomToolbar;
    }

    public String getCssEven() {
        return this.cssEven;
    }

    public String getCssOdd() {
        return this.cssOdd;
    }

    public DataProvider<T> getDataprovider() {
        return (DataProvider<T>) this.getDataProvider();
    }

    public String getTableRefreshAjaxId() {
        return this.tableRefreshAjaxId;
    }

    public String getTableSortAjaxId() {
        return this.tableSortAjaxId;
    }

    protected String getTableSortConfig() {
        String debug = "debug:true";
        String sortReset = "sortReset:true";
        String headers = "headers:{'." + TableColumn.UNSORTABLE + "':{sorter:false},'." + TableColumn.SERVER_SORTABLE + "':{sorter:false}}";
        String widgets = "widgets:[" + this.getTableSortConfigWidgets() + "],widgetOptions:{" + this.getTableSortConfigWidgetsConfig() + "}";
        String key = "sortMultiSortKey:'ctrlKey'";
        String css = "cssAsc:'wicket_orderDown',cssDesc:'wicket_orderUp'"; // TODO get wicket_orderDown/wicket_orderUp from somewhere else
        return debug + "," + headers + "," + widgets + "," + sortReset + "," + key + "," + css;
    }

    protected String getTableSortConfigWidgets() {
        // 'filter',
        return "'zebra'";
    }

    protected String getTableSortConfigWidgetsConfig() {
        // filter_useParsedData:true,filter_searchDelay:300,filter_ignoreCase:true,filter_columnFilters:false,filter_saveFilters:true,filter_reset:'.tablesortfilterreset',
        return "zebra:['" + this.getCssOdd() + "','" + this.getCssEven() + "']";
    }

    @Override
    protected DataGridView<T> newDataGridView(String id, List<? extends IColumn<T, String>> columns, IDataProvider<T> _dataProvider) {
        return new DefaultDataGridView(id, columns, _dataProvider);
    }

    @Override
    protected Item<T> newRowItem(final String id, final int index, final IModel<T> model) {
        return new Item<T>(id, index, model) {
            private static final long serialVersionUID = 4130240092094444434L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("class", ((this.getIndex() % 2) == 0) ? Table.this.cssEven : Table.this.cssOdd);
                Serializable dataId = Table.this.dataProvider.getId(model);
                String dataIdString;
                if (dataId instanceof String && StringUtils.isBlank(String.class.cast(dataId))) {
                    dataIdString = String.valueOf(index);
                } else {
                    dataIdString = String.valueOf(dataId);
                }
                tag.put("data-id", dataIdString);
            }
        };
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!this.isEnabledInHierarchy()) {
            return;
        }
        this.renderHeadIcons(response);
        this.renderHeadClientUpdate(response);
        this.renderHeadClientSorting(response);
    }

    /** client column sorting */
    @SuppressWarnings("unchecked")
    public void renderHeadClientSorting(IHeaderResponse response) {
        for (IColumn<T, String> column : this.getColumns()) {
            if (((TableColumn<T, ?>) column).getSorting() == Side.client) {
                response.render(JavaScriptHeaderItem.forReference(TableSorter.TABLE_SORTER_WIDGETS_JS));
                this.tableSortAjaxId = "sortable_table_" + this.getMarkupId();
                response.render(OnDomReadyHeaderItem.forScript("var " + this.tableSortAjaxId + " = $('#" + this.getMarkupId() + "').tablesorter({"
                        + this.getTableSortConfig() + "});"));
                break;
            }
        }
    }

    /** client update table via ajax */
    public void renderHeadClientUpdate(IHeaderResponse response) {
        if (StringUtils.isNotBlank(this.dataProvider.getIdProperty()) && StringUtils.isNotBlank(this.dataProvider.getAjaxRefreshUrl())) {
            response.render(JavaScriptHeaderItem.forReference(Table.JS_AJAX_UPDATE));

            response.render(JavaScriptHeaderItem.forScript(";var " + this.tableRefreshAjaxId + "=Array.prototype.map;", "js_"
                    + this.tableRefreshAjaxId));
            {
                Map<String, Map<String, Object>> propertyConfigs = new LinkedHashMap<>();
                int i = 0;
                for (IColumn<T, String> column : this.getColumns()) {
                    if (column.getClass().equals(TableColumn.class)) {
                        @SuppressWarnings("unchecked")
                        TableColumn<T, ?> propertyColumn = (TableColumn<T, ?>) column;
                        if (StringUtils.isNotBlank(propertyColumn.getPropertyExpression())) {
                            Map<String, Object> propertyConfig = new HashMap<>();
                            propertyConfigs.put(propertyColumn.getPropertyExpression(), propertyConfig);
                            propertyConfig.put("idx", i);
                            if (propertyColumn.isDataTag()) {
                                propertyConfig.put("data", propertyColumn.isDataTag());
                            }
                        }
                    }
                    i++;
                }
                String tableMarkupId = this.getMarkupId();
                Map<String, Object> configMap = new HashMap<String, Object>();
                configMap.put("refresh", this.dataProvider.getAjaxRefreshSeconds());
                configMap.put("type", this.dataProvider.getAjaxRefreshMethod());
                configMap.put("url", this.dataProvider.getAjaxRefreshUrl());
                configMap.put("oidProperty", this.dataProvider.getIdProperty());
                configMap.put("config", propertyConfigs);
                response.render(JavaScriptHeaderItem.forScript(
                        this.tableRefreshAjaxId + "['" + tableMarkupId + "'] = " + new JSONObject(configMap).toString() + ";", "js_"
                                + this.tableRefreshAjaxId + "_" + tableMarkupId));
            }
        }
    }

    public void renderHeadIcons(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(WeLoveIcons.WE_LOVE_ICONS_CSS));
    }

    public void setCssEven(String cssEven) {
        this.cssEven = cssEven;
    }

    public void setCssOdd(String cssOdd) {
        this.cssOdd = cssOdd;
    }

    public void setTableRefreshAjaxId(String tableRefreshAjaxId) {
        this.tableRefreshAjaxId = tableRefreshAjaxId;
    }
}
