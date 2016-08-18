package org.tools.hqlbuilder.webservice.wicket.components.tree;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

public class NestedListPanel<T, N extends DataNode<T>> extends Panel {
    public class NodePanel extends ListView<N> {
        private static final long serialVersionUID = -8157862892569371968L;

        protected boolean showThisNode;

        protected boolean numbered;

        public NodePanel(String name, List<N> list, boolean showThisNode, boolean numbered) {
            super(name, list);
            this.showThisNode = showThisNode;
            this.numbered = numbered;
            WebHelper.hide(this);
        }

        @Override
        protected void populateItem(ListItem<N> listItem) {
            WebHelper.hide(listItem);
            listItem.add(new NodeListItemPanel<T, DataNode<T>>(NestedListPanel.LIST_ITEM, this.showThisNode, this.numbered, listItem.getModelObject()));
        }
    }

    private static final long serialVersionUID = -2435567670604898188L;

    public static final String LIST = "list";

    public static final String NODE_CONTAINER = "nodecontainer";

    public static final String LIST_ITEM = "li";

    protected NestedListPanel(String id, List<N> list, boolean showThisNode, boolean numbered) {
        super(id);
        setVisible(list.size() > 0);
        this.addComponents(list, showThisNode, numbered);
    }

    public NestedListPanel(String id, N root, boolean showRoot, boolean numbered) {
        this(id, Collections.singletonList(root), showRoot, numbered);
    }

    protected void addComponents(List<N> list, boolean showThisNode, final boolean numbered) {
        WebMarkupContainer ul = new WebMarkupContainer(NestedListPanel.LIST) {
            private static final long serialVersionUID = 4454743926514851597L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.setName(numbered ? "ol" : "ul");
            }
        };
        ul.setRenderBodyOnly(!showThisNode);
        this.add(ul);

        Collections.sort(list, (o1, o2) -> new CompareToBuilder().append(String.valueOf(o1), String.valueOf(o2)).toComparison());
        // (N o1, N o2) -> new CompareToBuilder().append(String.valueOf(o1), String.valueOf(o2)).toComparison());
        ul.add(this.newNodeList(list, showThisNode, numbered));
    }

    protected NestedListPanel<T, N>.NodePanel newNodeList(List<N> list, boolean showThisNode, boolean numbered) {
        return new NodePanel(NestedListPanel.NODE_CONTAINER, list, showThisNode, numbered);
    }
}
