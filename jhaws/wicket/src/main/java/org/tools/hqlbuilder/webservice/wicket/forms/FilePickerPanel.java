package org.tools.hqlbuilder.webservice.wicket.forms;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.tools.hqlbuilder.webservice.jquery.ui.filestyle.FileStyle;
import org.tools.hqlbuilder.webservice.jquery.ui.validation.JQueryValidation;
import org.tools.hqlbuilder.webservice.wicket.components.AJAXDownload;

/**
 * based on markusslima bootstrap-filestyle version 1.1.0
 *
 * @see http://markusslima.github.io/bootstrap-filestyle/
 * @see http://www.surrealcms.com/blog/whipping-file-inputs-into-shape-with-bootstrap-3
 */
public class FilePickerPanel<P> extends FormRowPanel<P, List<FileUpload>, FileUploadField, FilePickerSettings> implements FormSubmitInterceptor {
    private static final long serialVersionUID = -6943635423428119032L;

    public static final String REMOVE_FILE_ID = "removeFile";

    public static final String DOWNLOAD_FILE_ID = "downloadFile";

    public static final String FILENAME_LABEL_ID = "filenameLabel";

    public static final String FILE_COMPONENT_CONTAINER_ID = "fileComponentContainer";

    public static final String FILE_INPUT_CONTAINER_ID = "fileInputContainer";

    public static final String FILE_PRESENT_CONTAINER_ID = "filePresentContainer";

    public static final String BUTTON_TEXT_ID = "buttonText";

    public static final String MIMETYPE_INVALID_MSG = "mimetype.invalid";

    // protected FileItemFactory fileItemFactory;

    protected IModel<?> parentModel;

    protected FilePickerHook hook;

    @SuppressWarnings("unchecked")
    public FilePickerPanel(IModel<?> parentModel, P propertyPath, FormSettings formSettings, FilePickerSettings componentSettings, FilePickerHook hook) {
        super(propertyPath, new ListModel<FileUpload>(), formSettings, componentSettings);
        this.hook = hook;
        this.parentModel = parentModel;
        @SuppressWarnings("rawtypes")
        Class tmpc = new ArrayList<FileUpload>().getClass();
        Class<List<FileUpload>> pt = tmpc;
        setPropertyType(pt);
    }

    @Override
    protected FormRowPanel<P, List<FileUpload>, FileUploadField, FilePickerSettings> addComponents() {
        this.add(getLabel());

        WebMarkupContainer fileComponentContainer = new WebMarkupContainer(FILE_COMPONENT_CONTAINER_ID);
        fileComponentContainer.setOutputMarkupId(true);
        add(fileComponentContainer);

        WebMarkupContainer fileInputContainer = new WebMarkupContainer(FILE_INPUT_CONTAINER_ID) {
            private static final long serialVersionUID = -5403623924747451558L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                if (hook.getCurrentFilenames() == null || hook.getCurrentFilenames().isEmpty()) {
                    tag.getAttributes().remove("style");
                } else {
                    tag.getAttributes().put("style", "display:none");
                }
            }
        };
        fileComponentContainer.add(fileInputContainer);
        fileInputContainer.setOutputMarkupId(true);
        fileInputContainer.add(getComponent());

        WebMarkupContainer filePresentContainer = new WebMarkupContainer(FILE_PRESENT_CONTAINER_ID) {
            private static final long serialVersionUID = -3719538448746978418L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                if (hook.getCurrentFilenames() == null || hook.getCurrentFilenames().isEmpty()) {
                    tag.getAttributes().put("style", "display:none");
                } else {
                    tag.getAttributes().remove("style");
                }
            }
        };
        fileComponentContainer.add(filePresentContainer);
        filePresentContainer.setOutputMarkupId(true);
        filePresentContainer.add(getFilePresentLabel());
        filePresentContainer.add(getDownloadFile()); // FIXME can only download first file
        filePresentContainer.add(getRemoveFile()); // FIXME can only remove all files

        this.add(getRequiredMarker());
        this.add(getFeedback());

        return this;
    }

    protected Component getFilePresentLabel() {
        Label filenameLabel = new Label(FILENAME_LABEL_ID, new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = -1394872283988076378L;

            @Override
            public String getObject() {
                return getInitialValue();
            }
        });
        return filenameLabel;
    }

    protected Component getDownloadFile() {
        final AJAXDownload download = new AJAXDownload() {
            private static final long serialVersionUID = 3386413702948444161L;

            @Override
            protected IResourceStream getResourceStream() {
                return hook.download(hook.getCurrentFilenames().iterator().next());
            }

            @Override
            protected String getFileName() {
                return hook.getCurrentFilenames().iterator().next();
            }
        };
        AjaxFallbackLink<String> button = new AjaxFallbackLink<String>(DOWNLOAD_FILE_ID) {
            private static final long serialVersionUID = -4513016499912698499L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                download.initiate(target);
            }
        };
        button.add(download);
        return button;
    }

    protected Component getRemoveFile() {
        AjaxFallbackLink<String> button = new AjaxFallbackLink<String>(REMOVE_FILE_ID) {
            private static final long serialVersionUID = -5973307610040597469L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                hook.clear(hook.getCurrentFilenames());
                target.add(FilePickerPanel.this.get(FILE_COMPONENT_CONTAINER_ID));
                target.appendJavaScript("$('#" + getComponent().getMarkupId() + "').filestyle();");
            }
        };
        return button;
    }

    @Override
    protected FileUploadField createComponent(IModel<List<FileUpload>> model, Class<List<FileUpload>> valueType) {
        FileUploadField comp = new FileUploadField(VALUE, model) {
            private static final long serialVersionUID = 506304026413763103L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                FilePickerSettings filePickerSettings = getFilePickerSettings();
                tag(tag, "accept", filePickerSettings.getMimeType());
                tag(tag, "data-input", filePickerSettings.getShowInput());
                tag(tag, "data-icon", filePickerSettings.getShowIcon());
                tag(tag, "data-buttonText", FilePickerPanel.this.getString(BUTTON_TEXT_ID));
                tag(tag, "data-buttonName", null);
                tag(tag, "data-iconName", null);
                tag(tag, "data-disabled", null);
                tag(tag, "data-buttonBefore", null);
                tag(tag, "multiple", Boolean.TRUE.equals(filePickerSettings.getMultiple()) ? "multiple" : null);
            }
        };

        comp.add(new IValidator<List<FileUpload>>() {
            private static final long serialVersionUID = -3197676261666493500L;

            @Override
            public void validate(IValidatable<List<FileUpload>> validatable) {
                FilePickerSettings settings = getFilePickerSettings();
                if (settings.getMimeType() != null && validatable.getValue() != null) {
                    for (FileUpload fileUpload : validatable.getValue()) {
                        if (!fileUpload.getContentType().equals(settings.getMimeType())) {
                            ValidationError error = new ValidationError(this);
                            error.addKey(MIMETYPE_INVALID_MSG);
                            error.setMessage(MIMETYPE_INVALID_MSG);
                            validatable.error(error);
                            break;
                        }
                    }
                }
            }
        });

        return comp;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        renderHeadConsistentLook(response);
        renderHeadClientSideValidation(response);
    }

    protected void renderHeadConsistentLook(IHeaderResponse response) {
        if (!getFilePickerSettings().isConsistentLook()) {
            return;
        }

        response.render(JavaScriptHeaderItem.forReference(FileStyle.FILESTYLE_JS));
    }

    protected void renderHeadClientSideValidation(IHeaderResponse response) {
        if (!getFilePickerSettings().isClientSideTypeCheck()) {
            return;
        }

        // also imports JQueryValidation.VALIDATION_JS, JQueryForm.FORM_JS and JqueryUI
        response.render(JavaScriptHeaderItem.forReference(JQueryValidation.VALIDATION_ADDITIONAL_JS));

        // localisation
        response.render(JavaScriptHeaderItem.forReference(JQueryValidation.VALIDATION_LOCALIZATION_JS));

        FilePickerSettings filePickerSettings = getFilePickerSettings();
        if (filePickerSettings.getMimeType() != null) {
            String formId = getComponent().getForm().getMarkupId();
            StringBuilder initScript = new StringBuilder();
            initScript.append("$(\"#" + formId + "\").validate();").append("\n");
            initScript.append("$(\"#" + getComponent().getMarkupId() + "\").rules('add', { accept: \"" + filePickerSettings.getMimeType() + "\" })")
            .append("\n");
            response.render(OnLoadHeaderItem.forScript(initScript));
        }
    }

    // public FileItemFactory getFileItemFactory() {
    // if (fileItemFactory == null) {
    // fileItemFactory = new DiskFileItemFactory(new FileCleaner());
    // }
    // return this.fileItemFactory;
    // }
    //
    // public void setFileItemFactory(FileItemFactory fileItemFactory) {
    // this.fileItemFactory = fileItemFactory;
    // }

    @Override
    public void onBeforeSubmit() {
        Collection<FileUpload> fileUploads = getComponent().getFileUploads();
        if (fileUploads != null) {
            hook.write(fileUploads);
        }
    }

    protected String getInitialValue() {
        Collection<String> currentFilenames = hook.getCurrentFilenames();
        if (currentFilenames == null || currentFilenames.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String currentFilename : currentFilenames) {
            sb.append(currentFilename).append(", ");
        }
        return sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1).toString();
    }

    @Override
    public void onAfterSubmit(Serializable submitReturnValue) {
        // nothing to do
    }

    public FilePickerSettings getFilePickerSettings() {
        return FilePickerSettings.class.cast(getComponentSettings());
    }
}
