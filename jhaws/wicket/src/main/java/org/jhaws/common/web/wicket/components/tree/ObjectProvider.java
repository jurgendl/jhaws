package org.jhaws.common.web.wicket.components.tree;

import java.io.Serializable;

public interface ObjectProvider<T> extends Serializable {
    T get();
}
