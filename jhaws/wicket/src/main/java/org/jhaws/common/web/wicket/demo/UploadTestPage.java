package org.jhaws.common.web.wicket.demo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Bytes;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.CollectionUtils8;
import org.jhaws.common.web.wicket.bootstrap.DefaultWebPage;

@SuppressWarnings("serial")
public class UploadTestPage extends DefaultWebPage {
    private static final String TMP_UPLOAD = "c:/java/tmp";

    public UploadTestPage() {
        super();
    }

    public UploadTestPage(PageParameters parameters) {
        super(parameters);
    }

    public static class UploadDTO implements Serializable {
        List<UploadItem> existing = new ArrayList<>();

        public UploadDTO(List<UploadItem> existing) {
            super();
            this.existing = existing;
        }

        public UploadDTO() {
            super();
        }

        public List<UploadItem> getExisting() {
            return existing;
        }

        public void setExisting(List<UploadItem> existing) {
            this.existing = existing;
        }
    }

    public static class UploadItem implements Serializable {
        String path;
        Boolean delete; // markeer om te verwijderen

        public UploadItem() {
            super();
        }

        public UploadItem(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Boolean getDelete() {
            return delete;
        }

        public void setDelete(Boolean delete) {
            this.delete = delete;
        }

        @Override
        public String toString() {
            return (Boolean.TRUE.equals(delete) ? "-" : "") + path;
        }
    }

    IModel<UploadDTO> formModel;

    @Override
    protected void addComponents(PageParameters parameters, MarkupContainer html) {
        Form<Void> hoofdform = new Form<>("hoofdform"); // om form in form te testen
        add(hoofdform);
        formModel = Model.of(new UploadDTO(new FilePath(TMP_UPLOAD).listFiles().stream().filter(FilePath::isFile).map(FilePath::getFileNameString).map(UploadItem::new).collect(Collectors.toList())));
        FileUploadField fileupload = new FileUploadField("fileupload");
        Form<UploadDTO> uploadform = new Form<UploadDTO>("uploadform", formModel) { // echte upload form
            @Override
            protected void onSubmit() {
                UploadDTO uploadDTO = getModel().getObject();
                CollectionUtils8.streamDetached(uploadDTO.getExisting()).forEach(i -> {
                    if (Boolean.TRUE.equals(i.getDelete())) {
                        System.out.println("-" + i.getPath()); // logging

                        FilePath newFile = new FilePath(TMP_UPLOAD).child(i.getPath());
                        newFile.delete(); // service toegang

                        // update model
                        uploadDTO.getExisting().remove(i);
                    }
                });
                List<FileUpload> uploadedFiles = fileupload.getFileUploads();
                if (uploadedFiles != null) {
                    uploadedFiles.forEach(uploadedFile -> {
                        try {
                            System.out.println("+" + uploadedFile.getClientFileName()); // logging

                            FilePath newFile = new FilePath(TMP_UPLOAD).child(uploadedFile.getClientFileName());
                            newFile.write(uploadedFile.getInputStream());// service toegang

                            // update model
                            uploadDTO.getExisting().add(new UploadItem(uploadedFile.getClientFileName()));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
        };
        uploadform.setFileMaxSize(Bytes.kilobytes(1024l * 10l));// random gekozen om te testen
        uploadform.setMultiPart(true); // denk ik niet meer nodig in deze wicket versie
        hoofdform.add(uploadform);
        WebMarkupContainer fileuploadLabel = new WebMarkupContainer("fileuploadLabel") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("for", fileupload.getMarkupId());
            }
        };
        uploadform.add(fileupload);
        uploadform.add(fileuploadLabel);

        // alle bestaande files (zitten al in formmodel DTO als lijst van UploadItem)
        uploadform.add(new ListView<UploadItem>("existing", new IModel<List<UploadItem>>() {
            @Override
            public void detach() {
                //
            }

            @Override
            public List<UploadItem> getObject() {
                return uploadform.getModel().getObject().getExisting();
            }

            @Override
            public void setObject(List<UploadItem> object) {
                uploadform.getModel().getObject().setExisting(object);
            }
        }) {
            @Override
            protected void populateItem(ListItem<UploadItem> item) {
                CheckBox itemdelete = new CheckBox("itemdelete", new PropertyModel<Boolean>(item.getModel(), "delete"));
                itemdelete.setOutputMarkupId(true);
                item.add(itemdelete);
                Label itemlabel = new Label("itemlabel", Model.of(item.getModelObject().getPath()));
                itemlabel.setOutputMarkupId(true);
                item.add(itemlabel);
            }
        });
        hoofdform.add(new SubmitLink("submit"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
    }
}
