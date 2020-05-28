package org.tools.hqlbuilder.webservice.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jhaws.common.web.wicket.bootstrap.DefaultWebPage;
import org.jhaws.common.web.wicket.tables.bootstrap.EnhancedTable;
import org.jhaws.common.web.wicket.tables.bootstrap.TableColumn;
import org.jhaws.common.web.wicket.tables.common.DefaultDataProvider;
import org.jhaws.common.web.wicket.tables.common.Side;
import org.tools.hqlbuilder.webservice.bootstrap4.popoverx.PopoverXPanel;

import ch.lambdaj.Lambda;

@SuppressWarnings("serial")
public class PaginationTestPage extends DefaultWebPage {
    public static class Record implements Serializable {
        private String name;

        public Record() {
            super();
        }

        public Record(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Override
    protected void addComponents(PageParameters parameters, MarkupContainer html) {
        // html.add(new PopoverXPanel("popover-x-test"));

        List<Record> records = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            records.add(new Record("" + i));
        }

        Record proxy = Lambda.on(Record.class);
        DefaultDataProvider<Record> dataProvider = new DefaultDataProvider<Record>() {
            @Override
            public Iterator<Record> select(long first, long count, Map<String, SortOrder> sorting) {
                return records.iterator();
            }

            @Override
            public long size() {
                return records.size();
            }
        };
        dataProvider.setStateless(true);
        List<TableColumn<Record, ?>> columns = new ArrayList<>();
        columns.add(EnhancedTable.<Record, String> newColumn(this, proxy.getName()).setSorting(Side.client));
        dataProvider.setAdd(false);
        dataProvider.setEdit(false);
        dataProvider.setRowsPerPage(10);
        EnhancedTable<Record> searchresultsTable = new EnhancedTable<>("table", columns, dataProvider);
        html.add(searchresultsTable);

        html.add(new PopoverXPanel("pagination-popover") {
            @Override
            protected Model<Boolean> createHeaderVisibibleModel() {
                return Model.of(Boolean.FALSE);
            }

            @Override
            protected Component createTrigger(String id) {
                Fragment fragment = new Fragment(id, POPOVER_X_TRIGGER_FRAGMENT, PaginationTestPage.this);
                Button triggeredBy = new Button(id) {
                    @Override
                    protected void onComponentTag(ComponentTag tag) {
                        super.onComponentTag(tag);
                        tag.getAttributes().put("data-target", getDataTargetAttribute());
                    }
                };
                fragment.add(triggeredBy);
                return fragment;
            }

            @Override
            protected Component createContent(String id) {
                WebMarkupContainer content = new WebMarkupContainer(id);
                content.add(new Fragment("content", "popover-form-content-fragment", PaginationTestPage.this));
                return content;
            }

            @Override
            protected Component createFooter(String id) {
                WebMarkupContainer footer = new WebMarkupContainer(id);
                footer.add(new Fragment("footer", "popover-footer-content-fragment", PaginationTestPage.this));
                return footer;
            }
        });
    }
}
