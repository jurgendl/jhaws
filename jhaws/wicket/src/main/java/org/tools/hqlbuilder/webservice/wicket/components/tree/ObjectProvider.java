package org.tools.hqlbuilder.webservice.wicket.components.tree;

import java.io.Serializable;

public interface ObjectProvider<T> extends Serializable {
    T get();
}
