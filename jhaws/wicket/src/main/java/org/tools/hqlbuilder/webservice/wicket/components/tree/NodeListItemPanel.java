package org.tools.hqlbuilder.webservice.wicket.components.tree;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

public class NodeListItemPanel<T, N extends DataNode<T>> extends Panel {
	private static final long serialVersionUID = -6668641188378661860L;

	public static final String LABEL = "label";

	public static final String NESTED = "nested";

	public NodeListItemPanel(String id, boolean showNode, boolean numbered, N node) {
		super(id);
		WebHelper.hide(this);
		this.addComponents(showNode, numbered, node);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addComponents(boolean showNode, boolean numbered, N node) {
		setRenderBodyOnly(true);
		setRenderBodyOnly(!showNode);
		this.add(new AttributeModifier("data-name", node.name()));
		this.add(new AttributeModifier("data-value", node.value()));
		this.add(new AttributeModifier("data-checked", node.checked()));
		this.add(new Label(NodeListItemPanel.LABEL, node.name()).setVisible(StringUtils.isNotBlank(node.name())));
		this.add(this.newNodeList(NodeListItemPanel.NESTED, showNode, numbered, (List) node.children()));
	}

	protected NestedListPanel<T, N> newNodeList(String id, boolean showNode, boolean numbered, List<N> list) {
		return new NestedListPanel<T, N>(id, list, showNode, numbered);
	}
}
