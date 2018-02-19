package org.jhaws.common.net.client;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;

@Provider
// @Consumes
public class MatrixTestBeanMessageBodyReader implements MessageBodyReader<MatrixTestBeanI> {
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public MatrixTestBeanI readFrom(Class<MatrixTestBeanI> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        System.out.println("READING");
        final BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.setNamingPolicy(new NamingPolicy() {
            @Override
            public String getClassName(final String prefix, final String source, final Object key, final Predicate names) {
                return type.getName();
            }
        });
        Map<String, List<String>> properties = httpHeaders.keySet().stream().collect(Collectors.toMap(Function.identity(), i -> httpHeaders.get(i)));
        BeanGenerator.addProperties(beanGenerator, properties);
        return type.cast(beanGenerator.createClass());
    }
}
