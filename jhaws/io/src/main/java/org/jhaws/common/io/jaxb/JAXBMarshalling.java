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
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.StringUtils;
import org.jhaws.common.lang.functions.EFunction;
import org.springframework.beans.factory.config.BeanDefinition;
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
public class JAXBMarshalling {
    protected final JAXBContext jaxbContext;

    protected final String defaultNameSpace;

    protected String charSet = StringUtils.UTF8;

    protected boolean formatOutput = true;

    protected ThreadLocal<Unmarshaller> threadLocalUnmarshaller = new ThreadLocal<Unmarshaller>() {
        @Override
        protected Unmarshaller initialValue() {
            try {
                return jaxbContext.createUnmarshaller();
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

    @SuppressWarnings("rawtypes")
    public static JAXBContext getJaxbContext(Class[] atLeastOneClass) {
        return getJaxbContext(atLeastOneClass[0],
                Arrays.stream(atLeastOneClass).skip(1).collect(Collectors.toList()).toArray(new Class[atLeastOneClass.length - 1]));
    }

    @SuppressWarnings("rawtypes")
    public static JAXBContext getJaxbContext(Class atLeastOneClass, Class... dtoClasses) {
        try {
            List<Class<?>> tmp = new ArrayList<>();
            tmp.add(atLeastOneClass);
            for (Class<?> c : dtoClasses)
                tmp.add(c);
            return JAXBContext.newInstance(tmp.toArray(new Class[tmp.size()]));
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<Class<?>> getJaxbClasses(Package atLeastOnePackage, Package... packages) {
        // @XmlType(factoryClass=ObjectFactory.class, factoryMethod="createBean")
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
        return _packages//
                .stream()//
                .map(p -> scanner.findCandidateComponents(p.getName()))//
                .map(Collection::stream)//
                .flatMap(Function.identity())//
                .map((EFunction<BeanDefinition, Class<?>>) bd -> Class.forName(bd.getBeanClassName()))//
                .collect(Collectors.toList())//
        ;
    }

    public static JAXBContext getJaxbContext(Package[] atLeastOnePackage) {
        return getJaxbContext(atLeastOnePackage[0],
                Arrays.stream(atLeastOnePackage).skip(1).collect(Collectors.toList()).toArray(new Package[atLeastOnePackage.length - 1]));
    }

    public static JAXBContext getJaxbContext(Package atLeastOnePackage, Package... packages) {
        List<Class<?>> classesToBeBound = getJaxbClasses(atLeastOnePackage, packages);
        try {
            return JAXBContext.newInstance(classesToBeBound.toArray(new Class[classesToBeBound.size()]));
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JAXBMarshalling(String defaultNamesSpace, Package atLeastOnePackage, Package... packages) {
        this.defaultNameSpace = defaultNamesSpace;
        jaxbContext = getJaxbContext(atLeastOnePackage, packages);
    }

    public JAXBMarshalling(String defaultNamesSpace, Package[] atLeastOnePackage) {
        this.defaultNameSpace = defaultNamesSpace;
        this.jaxbContext = getJaxbContext(atLeastOnePackage);
    }

    public JAXBMarshalling(String defaultNamesSpace, String tremaSeperatedPackages) {
        this.defaultNameSpace = defaultNamesSpace;
        this.jaxbContext = getJaxbContext(tremaSeperatedPackages);
    }

    @SuppressWarnings("rawtypes")
    public JAXBMarshalling(String defaultNamesSpace, Class atLeastOneClass, Class... dtoClasses) {
        this.defaultNameSpace = defaultNamesSpace;
        this.jaxbContext = getJaxbContext(atLeastOneClass, dtoClasses);
    }

    @SuppressWarnings("rawtypes")
    public JAXBMarshalling(String defaultNamesSpace, Class[] atLeastOneClass) {
        this.defaultNameSpace = defaultNamesSpace;
        this.jaxbContext = getJaxbContext(atLeastOneClass);
    }

    public JAXBMarshalling(Package atLeastOnePackage, Package... packages) {
        this((String) null, atLeastOnePackage, packages);
    }

    public JAXBMarshalling(Package[] atLeastOnePackage) {
        this((String) null, atLeastOnePackage);
    }

    public JAXBMarshalling(String tremaSeperatedPackages) {
        this((String) null, tremaSeperatedPackages);
    }

    @SuppressWarnings("rawtypes")
    public JAXBMarshalling(Class atLeastOneClass, Class... dtoClasses) {
        this((String) null, atLeastOneClass, dtoClasses);
    }

    @SuppressWarnings("rawtypes")
    public JAXBMarshalling(Class[] atLeastOneClass) {
        this((String) null, atLeastOneClass);
    }

    public <DTO> void marshall(DTO dto, OutputStream out) {
        try {
            threadLocalMarshaller.get().marshal(dto, out);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                out.close();
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
            marshall(dto, Files.newOutputStream(FilePath.getPath(xml)));
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

    public <DTO> DTO unmarshall(Class<DTO> type, File xml) {
        try {
            return unmarshall(type, new BufferedInputStream(new FileInputStream(xml)));
        } catch (FileNotFoundException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public <DTO> DTO unmarshall(Class<DTO> type, Path xml) {
        try {
            return unmarshall(type, Files.newInputStream(FilePath.getPath(xml)));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public <DTO> DTO unmarshall(Class<DTO> type, byte[] xml) {
        return unmarshall(type, new ByteArrayInputStream(xml));
    }

    public <DTO> DTO unmarshall(Class<DTO> type, String xml) {
        try {
            return unmarshall(type, xml.getBytes(charSet));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <DTO> DTO unmarshall(Class<DTO> type, String xml, String charset) {
        try {
            return unmarshall(type, xml.getBytes(charset));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <DTO> DTO unmarshall(Class<DTO> type, InputStream xml) {
        try {
            Object unmarshalled;
            if (defaultNameSpace == null) {
                Unmarshaller unmarshaller = threadLocalUnmarshaller.get();
                unmarshalled = unmarshaller.unmarshal(new StreamSource(xml), type).getValue();
            } else {
                XMLFilter xmlFilter = threadLocalXMLFilter.get();
                UnmarshallerHandler unmarshallerHandler = threadLocalUnmarshallerHandler.get();
                InputSource is = new InputSource(xml);
                xmlFilter.parse(is);
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
            try {
                xml.close();
            } catch (NullPointerException | IOException ex) {
                //
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
}
