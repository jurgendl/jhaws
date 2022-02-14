package org.jhaws.common.net.resteasy;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.ext.MessageBodyReader;

import org.apache.commons.io.IOUtils;

// @Consumes("*/*")
public class StreamingOutputMessageBodyReader implements MessageBodyReader<StreamingOutput> {
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public StreamingOutput readFrom(Class<StreamingOutput> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        return output -> IOUtils.copy(entityStream, output);
    }
}