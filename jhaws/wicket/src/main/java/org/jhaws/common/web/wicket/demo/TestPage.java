package org.jhaws.common.web.wicket.demo;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.web.wicket.bootstrap.DefaultWebPage;

@SuppressWarnings("serial")
public class TestPage extends DefaultWebPage {
    @Override
    protected WebComponent addStatusBar(PageParameters parameters, MarkupContainer html, String id, String contentid) {
        Label content = new Label(contentid, "status");
        html.add(new WebMarkupContainer(id).add(content));
        return content;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(OnDomReadyHeaderItem.forScript(new FilePath(TestPage.class, "TestPage-factory.js").readAll()));
    }

    @Override
    protected void addComponents(PageParameters parameters, MarkupContainer html) {
        //
    }
}
