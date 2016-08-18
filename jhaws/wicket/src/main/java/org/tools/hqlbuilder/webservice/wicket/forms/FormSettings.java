package org.tools.hqlbuilder.webservice.wicket.forms;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;

public class FormSettings implements Serializable {
    private static final long serialVersionUID = 3682532274799101432L;

    public static enum ShowMessages {
        no, top, bottom;
    }

    /** add required to form elements */
    protected boolean clientsideRequiredValidation = true;

    /** activate ajax on form (per field live validation, submit by ajax) */
    protected boolean ajax = false;

    protected String fieldSetLegend;

    /** show label */
    protected boolean showLabel = true;

    protected boolean showReset = true;

    protected boolean showSubmit = true;

    /** requires ajax = true */
    protected boolean liveValidation = false;

    protected boolean cancelable = false;

    protected boolean inheritId = false;

    /** css class for required fields */
    protected String requiredClass = "required";

    protected String validClass = "valid";

    protected String invalidClass = "invalid";

    protected int columns = 1;

    protected String labelWidth = "12em";

    protected boolean preferPrime = true;

    protected boolean showPlaceholder = true;

    protected FormMethod method;

    protected boolean stateless = false;

    protected Boolean autocomplete;

    protected boolean renderPocketGrid = true;

    protected ShowMessages showMessages = ShowMessages.bottom;


    public FormSettings() {
        super();
    }

    public FormSettings(FormSettings other) {
        try {
            BeanUtils.copyProperties(this, other);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(new CloneNotSupportedException(String.valueOf(ex)));
        }
    }

    @Override
    public FormSettings clone() {
        try {
            return this.getClass().cast(BeanUtils.cloneBean(this));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(new CloneNotSupportedException(String.valueOf(ex)));
        }
    }

    public boolean getAjax() {
        return this.ajax;
    }

    public Boolean getAutocomplete() {
        return this.autocomplete;
    }

    public boolean getCancelable() {
        return this.cancelable;
    }

    public boolean getClientsideRequiredValidation() {
        return this.clientsideRequiredValidation;
    }

    public int getColumns() {
        return this.columns;
    }

    public String getFieldSetLegend() {
        return this.fieldSetLegend;
    }

    public boolean getInheritId() {
        return this.inheritId;
    }

    public String getInvalidClass() {
        return this.invalidClass;
    }

    public String getLabelWidth() {
        return this.labelWidth;
    }

    public boolean getLiveValidation() {
        return this.liveValidation;
    }

    public FormMethod getMethod() {
        return this.method;
    }

    public String getRequiredClass() {
        return this.requiredClass;
    }

    public boolean getShowLabel() {
        return this.showLabel;
    }

    public String getValidClass() {
        return this.validClass;
    }

    public boolean isAjax() {
        return this.ajax;
    }

    public boolean isCancelable() {
        return this.cancelable;
    }

    public boolean isClientsideRequiredValidation() {
        return this.clientsideRequiredValidation;
    }

    public boolean isInheritId() {
        return this.inheritId;
    }

    public boolean isLiveValidation() {
        return this.liveValidation;
    }

    public boolean isPreferPrime() {
        return this.preferPrime;
    }

    public boolean isRenderPocketGrid() {
        return this.renderPocketGrid;
    }

    public boolean isShowLabel() {
        return this.showLabel;
    }

    public boolean isShowPlaceholder() {
        return this.showPlaceholder;
    }

    public boolean isShowReset() {
        return this.showReset;
    }

    public boolean isShowSubmit() {
        return this.showSubmit;
    }

    public boolean isStateless() {
        return this.stateless;
    }

    public FormSettings setAjax(boolean ajax) {
        this.ajax = ajax;
        return this;
    }

    public FormSettings setAutocomplete(Boolean autocomplete) {
        this.autocomplete = autocomplete;
        return this;
    }

    public FormSettings setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public FormSettings setClientsideRequiredValidation(boolean clientsideRequiredValidation) {
        this.clientsideRequiredValidation = clientsideRequiredValidation;
        return this;
    }

    public FormSettings setColumns(int columns) {
        this.columns = columns;
        return this;
    }

    public FormSettings setFieldSetLegend(String fieldSetLegend) {
        this.fieldSetLegend = fieldSetLegend;
        return this;
    }

    public FormSettings setInheritId(boolean inheritId) {
        this.inheritId = inheritId;
        return this;
    }

    public FormSettings setInvalidClass(String invalidClass) {
        this.invalidClass = invalidClass;
        return this;
    }

    public FormSettings setLabelWidth(String labelWidth) {
        this.labelWidth = labelWidth;
        return this;
    }

    public FormSettings setLiveValidation(boolean liveValidation) {
        this.liveValidation = liveValidation;
        return this;
    }

    public FormSettings setMethod(FormMethod method) {
        this.method = method;
        return this;
    }

    public FormSettings setPreferPrime(boolean preferPrime) {
        this.preferPrime = preferPrime;
        return this;
    }

    public FormSettings setRenderPocketGrid(boolean renderPocketGrid) {
        this.renderPocketGrid = renderPocketGrid;
        return this;
    }

    public FormSettings setRequiredClass(String requiredClass) {
        this.requiredClass = requiredClass;
        return this;
    }

    public FormSettings setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
        return this;
    }

    public FormSettings setShowPlaceholder(boolean showPlaceholder) {
        this.showPlaceholder = showPlaceholder;
        return this;
    }

    public FormSettings setShowReset(boolean showReset) {
        this.showReset = showReset;
        return this;
    }

    public FormSettings setShowSubmit(boolean showSubmit) {
        this.showSubmit = showSubmit;
        return this;
    }

    public FormSettings setStateless(boolean stateless) {
        this.stateless = stateless;
        return this;
    }

    public FormSettings setValidClass(String validClass) {
        this.validClass = validClass;
        return this;
    }

    protected boolean skipForExport(String propertyName) {
        return propertyName == null;
    }

    @Override
    public String toString() {
        try {
            StringBuilder builder = new StringBuilder("{");
            PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
            for (PropertyDescriptor desc : propertyUtils.getPropertyDescriptors(this)) {
                String name = desc.getName();
                if ("class".equals(name) || this.skipForExport(name)) {
                    continue;
                }
                Object value = propertyUtils.getSimpleProperty(this, name);
                if (value == null) {
                    continue;
                }
                if (String.class.equals(desc.getPropertyType())) {
                    if (StringUtils.isBlank(String.class.cast(value))) {
                        continue;
                    }
                    builder.append(name).append(":").append("\"").append(value).append("\"").append(",");
                } else if (Enum.class.isAssignableFrom(desc.getPropertyType())) {
                    builder.append(name).append(":").append("\"").append(value).append("\"").append(",");
                } else {
                    builder.append(name).append(":").append(value).append(",");
                }
            }
            if (builder.length() == 1) {
                return "{}";
            }
            return builder.deleteCharAt(builder.length() - 1).append("}").toString();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ShowMessages getShowMessages() {
        return this.showMessages;
    }

    public FormSettings setShowMessages(ShowMessages showMessages) {
        this.showMessages = showMessages;
        return this;
    }
}