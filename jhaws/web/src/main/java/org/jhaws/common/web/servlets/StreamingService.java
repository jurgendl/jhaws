package org.jhaws.common.web.servlets;

import java.io.IOException;

public interface StreamingService {
    public String getPath();

    public StreamingSource getStream(String relative) throws IOException;
}
