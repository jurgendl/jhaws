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
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

public class JAXBMarshalling {
	protected final JAXBContext jaxbContext;

	protected String charSet = StringUtils.UTF8;

	protected boolean formatOutput = true;

	public static JAXBContext getJaxbContext(String tremaSeperatedPackages) {
		try {
			return JAXBContext.newInstance(tremaSeperatedPackages);
		} catch (JAXBException ex) {
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("rawtypes")
	public static JAXBContext getJaxbContext(Class[] atLeastOneClass) {
		return getJaxbContext(atLeastOneClass[0], Arrays.stream(atLeastOneClass).skip(1).collect(Collectors.toList()).toArray(new Class[atLeastOneClass.length - 1]));
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
		List<Package> tmp = new ArrayList<>();
		tmp.add(atLeastOnePackage);
		for (Package p : packages)
			tmp.add(p);
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		final AnnotationTypeFilter f1 = new AnnotationTypeFilter(XmlRootElement.class);
		TypeFilter TypeFilter = (metadataReader, metadataReaderFactory) -> f1.match(metadataReader, metadataReaderFactory);
		scanner.addIncludeFilter(TypeFilter);
		List<Class<?>> classesToBeBound = new ArrayList<>();
		for (Package p : tmp) {
			for (BeanDefinition bd : scanner.findCandidateComponents(p.getName())) {
				try {
					classesToBeBound.add(Class.forName(bd.getBeanClassName()));
				} catch (ClassNotFoundException ex) {
					throw new RuntimeException(ex);
				}
			}
		}
		return classesToBeBound;
	}

	public static JAXBContext getJaxbContext(Package[] atLeastOnePackage) {
		return getJaxbContext(atLeastOnePackage[0], Arrays.stream(atLeastOnePackage).skip(1).collect(Collectors.toList()).toArray(new Package[atLeastOnePackage.length - 1]));
	}

	public static JAXBContext getJaxbContext(Package atLeastOnePackage, Package... packages) {
		List<Class<?>> classesToBeBound = getJaxbClasses(atLeastOnePackage, packages);
		try {
			return JAXBContext.newInstance(classesToBeBound.toArray(new Class[classesToBeBound.size()]));
		} catch (JAXBException ex) {
			throw new RuntimeException(ex);
		}
	}

	public JAXBMarshalling(Package atLeastOnePackage, Package... packages) {
		jaxbContext = getJaxbContext(atLeastOnePackage, packages);
	}

	public JAXBMarshalling(Package[] atLeastOnePackage) {
		jaxbContext = getJaxbContext(atLeastOnePackage);
	}

	public JAXBMarshalling(String tremaSeperatedPackages) {
		jaxbContext = getJaxbContext(tremaSeperatedPackages);
	}

	@SuppressWarnings("rawtypes")
	public JAXBMarshalling(Class atLeastOneClass, Class... dtoClasses) {
		jaxbContext = getJaxbContext(atLeastOneClass, dtoClasses);
	}

	@SuppressWarnings("rawtypes")
	public JAXBMarshalling(Class[] atLeastOneClass) {
		jaxbContext = getJaxbContext(atLeastOneClass);
	}

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

	protected ThreadLocal<Marshaller> threadLocalMarshaller = new ThreadLocal<Marshaller>() {
		@Override
		protected Marshaller initialValue() {
			try {
				Marshaller m = jaxbContext.createMarshaller();
				if (formatOutput)
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				return m;
			} catch (JAXBException ex) {
				throw new RuntimeException(ex);
			}
		}
	};

	public <DTO> void marshall(DTO dto, OutputStream out) {
		try {
			threadLocalMarshaller.get().marshal(dto, out);
		} catch (JAXBException ex) {
			throw new RuntimeException(ex);
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

	public <DTO> DTO unmarshall(File xml) {
		try {
			return unmarshall(new BufferedInputStream(new FileInputStream(xml)));
		} catch (FileNotFoundException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public <DTO> DTO unmarshall(Path xml) {
		try {
			return unmarshall(Files.newInputStream(FilePath.getPath(xml)));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public <DTO> DTO unmarshall(byte[] xml) {
		return unmarshall(new ByteArrayInputStream(xml));
	}

	public <DTO> DTO unmarshall(String xml) {
		try {
			return unmarshall(xml.getBytes(charSet));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public <DTO> DTO unmarshall(String xml, String charset) {
		try {
			return unmarshall(xml.getBytes(charset));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public <DTO> DTO unmarshall(InputStream xml) {
		try {
			Object unmarshalled = threadLocalUnmarshaller.get().unmarshal(xml);
			if (unmarshalled instanceof JAXBElement) {
				// xml is soap message => JAXBElement wrapper
				unmarshalled = JAXBElement.class.cast(unmarshalled).getValue();
			}
			// xml is jaxb marshalled message
			@SuppressWarnings("unchecked")
			DTO dto = (DTO) unmarshalled;
			return dto;
		} catch (JAXBException ex) {
			throw new RuntimeException(ex);
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
}