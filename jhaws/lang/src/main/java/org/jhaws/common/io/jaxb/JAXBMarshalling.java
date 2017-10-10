package org.jhaws.common.io.jaxb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * @see http://www.source4code.info/2013/07/jaxb-marshal-element-missing-xmlrootelement-annotation.html
 * @see http://blog.bdoughan.com/2012/07/jaxb-and-root-elements.html
 * @see http://stackoverflow.com/questions/24519449/unable-to-marshal-type-as-an-element-because-it-is-missing-an-xmlrootelement-an
 * @see http://blog.bdoughan.com/2010/08/jaxb-namespaces.html
 */
@SuppressWarnings("rawtypes")
public class JAXBMarshalling {
    protected final JAXBContext jaxbContext;

    protected final String defaultNameSpace;

    protected String charSet = "UTF-8";

    protected boolean formatOutput = true;

    protected boolean autoClose = true;

    protected ThreadLocal<Unmarshaller> threadLocalUnmarshaller = new ThreadLocal<Unmarshaller>() {
        @Override
        protected Unmarshaller initialValue() {
            try {
                Unmarshaller u = jaxbContext.createUnmarshaller();
                return u;
            } catch (JAXBException ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    protected ThreadLocal<XMLFilter> threadLocalXMLFilter = new ThreadLocal<XMLFilter>() {
        @Override
        protected XMLFilter initialValue() {
            if (defaultNameSpace == null) return null;
            XMLFilter filter = new XMLFilterImpl() {
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    super.endElement(defaultNameSpace, localName, qName);
                }

                @Override
                public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                    super.startElement(defaultNameSpace, localName, qName, atts);
                }
            };
            SAXParserFactory spf = SAXParserFactory.newInstance();
            try {
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                filter.setParent(xr);
            } catch (ParserConfigurationException | SAXException ex) {
                throw new RuntimeException(ex);
            }
            return filter;
        }
    };

    protected ThreadLocal<UnmarshallerHandler> threadLocalUnmarshallerHandler = new ThreadLocal<UnmarshallerHandler>() {
        @Override
        protected UnmarshallerHandler initialValue() {
            if (defaultNameSpace == null) return null;
            UnmarshallerHandler unmarshallerHandler = threadLocalUnmarshaller.get().getUnmarshallerHandler();
            XMLFilter xmlFilter = threadLocalXMLFilter.get();
            xmlFilter.setContentHandler(unmarshallerHandler);
            return unmarshallerHandler;
        }
    };

    protected ThreadLocal<Marshaller> threadLocalMarshaller = new ThreadLocal<Marshaller>() {
        @Override
        protected Marshaller initialValue() {
            try {
                Marshaller m = jaxbContext.createMarshaller();
                if (formatOutput) m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                // m.setProperty(Marshaller.JAXB_FRAGMENT, true);
                // m.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,
                // "");
                // m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                return m;
            } catch (JAXBException ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    public static JAXBContext getJaxbContext(String tremaSeperatedPackages) {
        try {
            return JAXBContext.newInstance(tremaSeperatedPackages);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static JAXBContext getJaxbContext(Class[] atLeastOneClass) {
        return getJaxbContext(c -> true, atLeastOneClass);
    }

    public static JAXBContext getJaxbContext(Predicate<Class> accept, Class[] atLeastOneClass) {
        return getJaxbContext(accept, atLeastOneClass[0],
                Arrays.stream(atLeastOneClass).skip(1).collect(Collectors.toList()).toArray(new Class[atLeastOneClass.length - 1]));
    }

    public static JAXBContext getJaxbContext(Class atLeastOneClass, Class... dtoClasses) {
        return getJaxbContext(c -> true, atLeastOneClass, dtoClasses);
    }

    public static JAXBContext getJaxbContext(Predicate<Class> accept, Class atLeastOneClass, Class... dtoClasses) {
        try {
            List<Class> tmp = new ArrayList<>();
            tmp.add(atLeastOneClass);
            for (Class c : dtoClasses) {
                tmp.add(c);
            }
            Stream<Class> stream = tmp.stream();
            if (accept != null) stream = stream.filter(accept);
            return JAXBContext.newInstance(stream.toArray(size -> new Class[size]));
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<Class> getJaxbClasses(Predicate<Class> accept, Package atLeastOnePackage, Package... packages) {
        // @XmlType(factoryClass=ObjectFactory.class,
        // factoryMethod="createBean")
        List<Package> _packages = new ArrayList<>();
        _packages.add(atLeastOnePackage);
        for (Package p : packages) {
            _packages.add(p);
        }
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        AnnotationTypeFilter f1 = new AnnotationTypeFilter(XmlRootElement.class);
        AnnotationTypeFilter f2 = new AnnotationTypeFilter(XmlType.class);
        TypeFilter typeFilter = (metadataReader, metadataReaderFactory) -> f1.match(metadataReader, metadataReaderFactory)
                || f2.match(metadataReader, metadataReaderFactory);
        scanner.addIncludeFilter(typeFilter);
        Stream<Class> stream = _packages//
                .stream()//
                .map(p -> scanner.findCandidateComponents(p.getName()))//
                .map(Collection::stream)//
                .flatMap(Function.identity())//
                .map(bd -> {
                    try {
                        return Class.forName(bd.getBeanClassName());
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                });
        if (accept != null) stream = stream.filter(accept);
        return stream.collect(Collectors.toList());
    }

    public static JAXBContext getJaxbContext(Package[] atLeastOnePackage) {
        return getJaxbContext(c -> true, atLeastOnePackage);
    }

    public static JAXBContext getJaxbContext(Predicate<Class> accept, Package[] atLeastOnePackage) {
        return getJaxbContext(accept, atLeastOnePackage[0],
                Arrays.stream(atLeastOnePackage).skip(1).collect(Collectors.toList()).toArray(new Package[atLeastOnePackage.length - 1]));
    }

    public static JAXBContext getJaxbContext(Package atLeastOnePackage, Package... packages) {
        return getJaxbContext(c -> true, atLeastOnePackage, packages);
    }

    public static JAXBContext getJaxbContext(Predicate<Class> accept, Package atLeastOnePackage, Package... packages) {
        List<Class> classesToBeBound = getJaxbClasses(accept, atLeastOnePackage, packages);
        try {
            return JAXBContext.newInstance(classesToBeBound.toArray(new Class[classesToBeBound.size()]));
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JAXBMarshalling(Predicate<Class> accept, String defaultNamesSpace, Package atLeastOnePackage, Package... packages) {
        this.defaultNameSpace = defaultNamesSpace;
        jaxbContext = getJaxbContext(accept, atLeastOnePackage, packages);
    }

    public JAXBMarshalling(Predicate<Class> accept, String defaultNamesSpace, Package[] atLeastOnePackage) {
        this.defaultNameSpace = defaultNamesSpace;
        this.jaxbContext = getJaxbContext(accept, atLeastOnePackage);
    }

    public JAXBMarshalling(Predicate<Class> accept, String defaultNamesSpace, Class atLeastOneClass, Class... dtoClasses) {
        this.defaultNameSpace = defaultNamesSpace;
        this.jaxbContext = getJaxbContext(accept, atLeastOneClass, dtoClasses);
    }

    public JAXBMarshalling(Predicate<Class> accept, String defaultNamesSpace, Class[] atLeastOneClass) {
        this.defaultNameSpace = defaultNamesSpace;
        this.jaxbContext = getJaxbContext(accept, atLeastOneClass);
    }

    public JAXBMarshalling(Predicate<Class> accept, Package atLeastOnePackage, Package... packages) {
        this(accept, (String) null, atLeastOnePackage, packages);
    }

    public JAXBMarshalling(Predicate<Class> accept, Package[] atLeastOnePackage) {
        this(accept, (String) null, atLeastOnePackage);
    }

    public JAXBMarshalling(Predicate<Class> accept, Class atLeastOneClass, Class... dtoClasses) {
        this(accept, (String) null, atLeastOneClass, dtoClasses);
    }

    public JAXBMarshalling(Predicate<Class> accept, Class[] atLeastOneClass) {
        this(accept, (String) null, atLeastOneClass);
    }

    public JAXBMarshalling(String tremaSeperatedPackages) {
        this((String) null, tremaSeperatedPackages);
    }

    public JAXBMarshalling(String defaultNamesSpace, String tremaSeperatedPackages) {
        this.defaultNameSpace = defaultNamesSpace;
        this.jaxbContext = getJaxbContext(tremaSeperatedPackages);
    }

    public JAXBMarshalling(String defaultNamesSpace, Package atLeastOnePackage, Package... packages) {
        this((Predicate<Class>) null, defaultNamesSpace, atLeastOnePackage, packages);
    }

    public JAXBMarshalling(String defaultNamesSpace, Package[] atLeastOnePackage) {
        this((Predicate<Class>) null, defaultNamesSpace, atLeastOnePackage);
    }

    public JAXBMarshalling(String defaultNamesSpace, Class atLeastOneClass, Class... dtoClasses) {
        this((Predicate<Class>) null, defaultNamesSpace, atLeastOneClass, dtoClasses);
    }

    public JAXBMarshalling(String defaultNamesSpace, Class[] atLeastOneClass) {
        this((Predicate<Class>) null, defaultNamesSpace, atLeastOneClass);
    }

    public JAXBMarshalling(Package atLeastOnePackage, Package... packages) {
        this((Predicate<Class>) null, atLeastOnePackage, packages);
    }

    public JAXBMarshalling(Package[] atLeastOnePackage) {
        this((Predicate<Class>) null, atLeastOnePackage);
    }

    public JAXBMarshalling(Class atLeastOneClass, Class... dtoClasses) {
        this((Predicate<Class>) null, atLeastOneClass, dtoClasses);
    }

    public JAXBMarshalling(Class[] atLeastOneClass) {
        this((Predicate<Class>) null, atLeastOneClass);
    }

    public <DTO> void marshall(DTO dto, OutputStream out) {
        try {
            threadLocalMarshaller.get().marshal(dto, out);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                out.flush();
            } catch (NullPointerException | IOException ex) {
                //
            }
        }
    }

    public <DTO> void marshall(DTO dto, Writer out) {
        try {
            threadLocalMarshaller.get().marshal(dto, out);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                out.flush();
            } catch (NullPointerException | IOException ex) {
                //
            }
        }
    }

    public <DTO> void marshall(DTO dto, File xml) {
        try {
            marshall(dto, new BufferedOutputStream(new FileOutputStream(xml)));
        } catch (FileNotFoundException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public <DTO> void marshall(DTO dto, Path xml) {
        try {
            marshall(dto, Files.newOutputStream(xml));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public <DTO> String marshall(DTO dto) {
        return marshall(dto, charSet);
    }

    public <DTO> String marshall(DTO dto, String charset) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            marshall(dto, out);
            out.flush();
            return new String(out.toByteArray(), charset);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <DTO> DTO unmarshall(File xml) {
        return (DTO) unmarshall(Object.class, xml);
    }

    public <DTO> DTO unmarshall(Class<DTO> type, File xml) {
        try {
            return unmarshall(type, new BufferedInputStream(new FileInputStream(xml)));
        } catch (FileNotFoundException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <DTO> DTO unmarshall(Path xml) {
        return (DTO) unmarshall(Object.class, xml);
    }

    public <DTO> DTO unmarshall(Class<DTO> type, Path xml) {
        try {
            return unmarshall(type, Files.newInputStream(xml));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <DTO> DTO unmarshall(byte[] xml) {
        return (DTO) unmarshall(Object.class, xml);
    }

    public <DTO> DTO unmarshall(Class<DTO> type, byte[] xml) {
        return unmarshall(type, new ByteArrayInputStream(xml));
    }

    @SuppressWarnings("unchecked")
    public <DTO> DTO unmarshall(String xml) {
        return (DTO) unmarshall(Object.class, xml);
    }

    public <DTO> DTO unmarshall(Class<DTO> type, String xml) {
        try {
            return unmarshall(type, xml.getBytes(charSet));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <DTO> DTO unmarshall(String xml, String charset) {
        return (DTO) unmarshall(Object.class, xml, charset);
    }

    public <DTO> DTO unmarshall(Class<DTO> type, String xml, String charset) {
        try {
            return unmarshall(type, xml.getBytes(charset));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <DTO> DTO unmarshall(InputStream xml) {
        return (DTO) unmarshall(Object.class, xml);
    }

    public <DTO> DTO unmarshall(Class<DTO> type, InputStream xml) {
        return _unmarshall(type, xml);
    }

    public <DTO> DTO unmarshall(Class<DTO> type, Reader xml) {
        return _unmarshall(type, xml);
    }

    @SuppressWarnings("unchecked")
    public <DTO> DTO unmarshall(Reader xml) {
        return (DTO) unmarshall(Object.class, xml);
    }

    protected <DTO> DTO _unmarshall(Class<DTO> type, Object input) {
        if (input instanceof InputStream) {
            //
        } else if (input instanceof Reader) {
            //
        } else {
            throw new IllegalArgumentException();
        }
        try {
            Object unmarshalled;
            if (defaultNameSpace == null) {
                Unmarshaller unmarshaller = threadLocalUnmarshaller.get();
                StreamSource source = null;
                if (input instanceof InputStream) {
                    source = new StreamSource(InputStream.class.cast(input));
                } else if (input instanceof Reader) {
                    source = new StreamSource(Reader.class.cast(input));
                }
                if (type == null || Object.class.equals(type)) {
                    unmarshalled = unmarshaller.unmarshal(source);
                } else {
                    JAXBElement<DTO> unmarschalled = unmarshaller.unmarshal(source, type);
                    unmarshalled = unmarschalled.getValue();
                }
            } else {
                XMLFilter xmlFilter = threadLocalXMLFilter.get();
                UnmarshallerHandler unmarshallerHandler = threadLocalUnmarshallerHandler.get();
                InputSource source = null;
                if (input instanceof InputStream) {
                    source = new InputSource(InputStream.class.cast(input));
                } else if (input instanceof Reader) {
                    source = new InputSource(Reader.class.cast(input));
                }
                xmlFilter.parse(source);
                unmarshalled = unmarshallerHandler.getResult();
            }
            if (unmarshalled instanceof JAXBElement) {
                // xml is soap message => JAXBElement wrapper
                unmarshalled = JAXBElement.class.cast(unmarshalled).getValue();
            }
            // xml is jaxb marshalled message
            @SuppressWarnings("unchecked")
            DTO dto = (DTO) unmarshalled;
            return dto;
        } catch (JAXBException | IOException | SAXException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (autoClose) {
                try {
                    if (input instanceof InputStream) {
                        InputStream.class.cast(input).close();
                    } else if (input instanceof Reader) {
                        Reader.class.cast(input).close();
                    }
                } catch (IOException ex) {
                    //
                }
            }
        }
    }

    public String getCharSet() {
        return this.charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public boolean isFormatOutput() {
        return this.formatOutput;
    }

    public void setFormatOutput(boolean formatOutput) {
        this.formatOutput = formatOutput;
    }

    public JAXBContext getJaxbContext() {
        return this.jaxbContext;
    }

    public String getDefaultNameSpace() {
        return this.defaultNameSpace;
    }

    public boolean isAutoClose() {
        return this.autoClose;
    }

    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }
}
