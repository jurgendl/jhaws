package org.jhaws.common.net.client.tests;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestBody {
    private String body;

    public TestBody() {
        super();
    }

    public TestBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "TestBody [body=" + body + "]";
    }
}
