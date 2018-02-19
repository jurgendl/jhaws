package org.tools.hqlbuilder.webservice.wicket.actions;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface OnAction extends Serializable {
    public void doAction(AjaxRequestTarget target);
}