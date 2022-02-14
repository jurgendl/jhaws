package org.jhaws.common.web.wicket.components;

import java.nio.file.Path;

import org.apache.wicket.request.resource.IResource.Attributes;
import org.jhaws.common.io.FilePath;

@SuppressWarnings("serial")
public class PathImageDataProvider implements ImageDataProvider {
    private FilePath root;
    private FilePath path;

    public PathImageDataProvider(Path root, Path path) {
        this.root = new FilePath(root);
        this.path = new FilePath(path);
    }

    @Override
    public byte[] getImageData(Attributes attributes) {
        return path.readAllBytes();
    }

    @Override
    public String getName() {
        return root.relativize(path).toString().replace('/', '_').replace('\\', '_');
    }

    @Override
    public String getFormat() {
        return "image/" + path.getExtension();
    }

}
