package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.Collection;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.util.resource.IResourceStream;

public interface FilePickerHook extends Serializable {
    public void write(Collection<FileUpload> files);

    public Collection<String> getCurrentFilenames();

    public IResourceStream download(String currentFilename);

    public void clear(Collection<String> filenames);
}
