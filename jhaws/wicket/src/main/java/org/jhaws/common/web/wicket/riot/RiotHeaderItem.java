package org.jhaws.common.web.wicket.riot;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.CharSequenceResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.jhaws.common.io.FilePath;

@SuppressWarnings("serial")
public class RiotHeaderItem extends JavaScriptReferenceHeaderItem {
    protected String inlineContent;

    public RiotHeaderItem(Class<?> scope, String name, boolean inline) {
        super(new ResourceReference(scope, name) {
            @Override
            public IResource getResource() {
                return new ByteArrayResource("text/plain", new FilePath(scope, name).readAllBytes(), name);
            }
        }, null, name, false, "utf-8", null);
        if (inline) inlineContent = new FilePath(scope, name).readAll();
    }

    public RiotHeaderItem(Class<?> scope, String name, byte[] content, boolean inline) {
        super(new ResourceReference(scope, name) {
            @Override
            public IResource getResource() {
                return new ByteArrayResource("text/plain", content, name);
            }
        }, null, name, false, "utf-8", null);
        if (inline) try {
            this.inlineContent = new String(content, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public RiotHeaderItem(Class<?> scope, String name, String content, boolean inline) {
        super(new ResourceReference(scope, name) {
            @Override
            public IResource getResource() {
                return new CharSequenceResource("text/plain", content, name);
            }
        }, null, name, false, "utf-8", null);
        if (inline) this.inlineContent = content;
    }

    @Override
    public void render(Response r) {
        if (StringUtils.isNotBlank(inlineContent)) {
            r.write("<script type=\"riot/tag\">" + inlineContent.replace("<script>", "").replace("</script>", "") + "</script>\n");
        } else {
            r.write("<script type=\"riot/tag\" data-src=\""
                    + RequestCycle.get().urlFor(new ResourceReferenceRequestHandler(getReference())).toString() + "\"></script>\n");
        }
    }
}
