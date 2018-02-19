package org.jhaws.common.net.client;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TestBody other = (TestBody) obj;
        if (body == null) {
            if (other.body != null) return false;
        } else if (!body.equals(other.body)) return false;
        return true;
    }
}
