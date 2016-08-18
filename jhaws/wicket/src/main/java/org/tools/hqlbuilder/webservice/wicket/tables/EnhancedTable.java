package org.tools.hqlbuilder.webservice.wicket.tables;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.name;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.ActionsPanel;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.BooleanColumn;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.EmailColumn;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.URIColumn;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.URLColumn;
import org.tools.hqlbuilder.webservice.wicket.tables.Table.URLStringColumn;

public class EnhancedTable<T extends Serializable> extends Panel {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(Table.class);

	public static final String FORM_ID = "tableform";

	public static final String TABLE_ID = "table";

	public static final String ACTIONS_ID = "table.actions";

	protected final Table<T> table;

	public EnhancedTable(String id, List<TableColumn<T, ?>> columns, final DataProvider<T> dataProvider) {
		super(id);
		setOutputMarkupId(true);
		Form<T> form = createForm(dataProvider);
		add(form);
		this.table = createTable(columns, dataProvider, form);
		form.add(table);
	}

	protected Form<T> createForm(final DataProvider<T> dataProvider) {
		if (dataProvider.isStateless()) {
			return new StatelessForm<>(FORM_ID);
		}
		return new Form<>(FORM_ID);
	}

	protected Table<T> createTable(List<TableColumn<T, ?>> columns, final DataProvider<T> dataProvider, Form<T> form) {
		return new Table<>(form, TABLE_ID, columns, dataProvider);
	}

	public Table<T> getTable() {
		return this.table;
	}

	public static <D, P> TableColumn<D, P> newColumn(Component parent, P argument) {
		return new TableColumn<>(labelModel(parent, argument), name(argument));
	}

	public static <D> BooleanColumn<D> newBooleanColumn(Component parent, Boolean argument) {
		return new BooleanColumn<>(labelModel(parent, argument), name(argument));
	}

	public static <D> EmailColumn<D> newEmailColumn(Component parent, String argument) {
		return new EmailColumn<>(labelModel(parent, argument), name(argument));
	}

	/** {@link URL}, {@link URI} and url as {@link String} supported */
	public static <D> URLColumn<D> newURLColumn(Component parent, URL argument) {
		return new URLColumn<>(labelModel(parent, argument), name(argument));
	}

	/** {@link URL}, {@link URI} and url as {@link String} supported */
	public static <D> URIColumn<D> newURIColumn(Component parent, URI argument) {
		return new URIColumn<>(labelModel(parent, argument), name(argument));
	}

	/** {@link URL}, {@link URI} and url as {@link String} supported */
	public static <D> URLStringColumn<D> newURLStringColumn(Component parent, String argument) {
		return new URLStringColumn<>(labelModel(parent, argument), name(argument));
	}

	public static <D> TableColumn<D, Object> newTimeColumn(Component parent, Object argument) {
		return newDateOrTimeColumn(parent, argument, new DateConverter(true) {

			private static final long serialVersionUID = 1L;

			@Override
			protected DateTimeFormatter getFormat(Locale locale) {
				return DateTimeFormat.longTime().withLocale(locale);
			}

			@Override
			public String getDatePattern(Locale locale) {
				return getFormat(locale).toString();
			}
		});
	}

	public static <D> TableColumn<D, Object> newDateColumn(Component parent, Object argument) {
		return newDateOrTimeColumn(parent, argument, new DateConverter(true) {

			private static final long serialVersionUID = 1L;

			@Override
			protected DateTimeFormatter getFormat(Locale locale) {
				return DateTimeFormat.longDate().withLocale(locale);
			}

			@Override
			public String getDatePattern(Locale locale) {
				return getFormat(locale).toString();
			}
		});
	}

	public static <D> TableColumn<D, Object> newDateTimeColumn(Component parent, Object argument) {
		return newDateOrTimeColumn(parent, argument, new DateConverter(true) {

			private static final long serialVersionUID = 1L;

			@Override
			protected DateTimeFormatter getFormat(Locale locale) {
				return DateTimeFormat.longDateTime().withLocale(locale);
			}

			@Override
			public String getDatePattern(Locale locale) {
				return getFormat(locale).toString();
			}
		});
	}

	public static <D> TableColumn<D, Object> newDateOrTimeColumn(Component parent, Object argument, final DateConverter dateConverter) {
		return new TableColumn<D, Object>(labelModel(parent, argument), name(argument)) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void populateItem(Item<ICellPopulator<D>> item, String componentId, IModel<D> rowModel) {
				item.add(new DateLabel(componentId, (IModel) getDataModel(rowModel), dateConverter));
			}
		};
	}

	public static IModel<String> labelModel(final Component parent, Object argument) {
		String tmp = null;
		try {
			tmp = name(argument);
		} catch (ch.lambdaj.function.argument.ArgumentConversionException ex) {
			tmp = String.valueOf(argument);
		}
		final String property = tmp;
		IModel<String> label;
		try {
			label = new AbstractReadOnlyModel<String>() {

				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					try {
						return parent.getString(property);
					} catch (java.util.MissingResourceException ex) {
						return null;
					}

				}
			};
		} catch (MissingResourceException ex) {
			logger.error(parent.getClass().getName() + ": no translation for " + property);
			label = Model.of("[" + property + "_" + parent.getLocale() + "]");
		}
		return label;
	}

	public static <T extends Serializable> TableColumn<T, Object> getActionsColumn(Component parent, final DataProvider<T> provider) {
		return new ActionsColumn<>(labelModel(parent, ACTIONS_ID), provider);
	}

	public static class ActionsColumn<T extends Serializable> extends TableColumn<T, Object> {

		private static final long serialVersionUID = 1L;
		protected final DataProvider<T> provider;

		public ActionsColumn(IModel<String> displayModel, final DataProvider<T> provider) {
			setDisplayModel(displayModel);
			this.provider = provider;
		}

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void populateItem(Item cellItem, String componentId, IModel rowModel) {
			T object = ((T) rowModel.getObject());
			cellItem.add(new ActionsPanel<T>(componentId, object, provider) {

				private static final long serialVersionUID = 1L;

				@Override
				public void onDelete(AjaxRequestTarget target, T o) {
					provider.delete(target, o);
				}

				@Override
				public void onEdit(AjaxRequestTarget target, T o) {
					provider.edit(target, o);
				}
			});
		}
	}
}
