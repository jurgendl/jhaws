package org.jhaws.common.elasticsearch.common;

import java.io.Serializable;

import org.apache.commons.lang3.builder.CompareToBuilder;

@SuppressWarnings("serial")
public class ElasticDocument implements Comparable<ElasticDocument>, Serializable {
    @Id
    protected String id;

    @Version
    protected Long version;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "#" + this.id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ElasticDocument other = (ElasticDocument) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public int compareTo(ElasticDocument o) {
        return new CompareToBuilder().append(this.id, o.id).toComparison();
    }
}
