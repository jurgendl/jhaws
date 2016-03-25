package org.jhaws.common.svn.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class Svn {
	private static final Logger logger = LoggerFactory.getLogger(Svn.class);

	public static class RootBeanImpl {
		private transient String toString;

		@Override
		public String toString() {
			return toString;
		}

		public void toString(String string) {
			toString = string;
		}
	}

	private static JAXBContext jaxbContext;

	private static Unmarshaller jaxbUnmarshaller;

	private static Marshaller jaxbMarshaller;

	private static <T extends RootBeanImpl> T get(Class<T> clazz, InputStream input) {
		byte[] data = new byte[0];
		try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			IOUtils.copy(input, output);
			IOUtils.closeQuietly(input);
			output.flush();
			IOUtils.closeQuietly(output);
			data = output.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
				T t = clazz.cast(Svn.getJaxbUnmarshaller().unmarshal(in));
				try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
					Svn.getJaxbMarshaller().marshal(t, os);
					t.toString(new String(os.toByteArray()));
				}
				return t;
			}
		} catch (IOException | JAXBException | RuntimeException ex) {
			logger.error("{}", ex);
			logger.error("{}", new String(data));
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	private static JAXBContext getJaxbContext() throws JAXBException {
		if (Svn.jaxbContext == null) {
			ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
			provider.addIncludeFilter(new AnnotationTypeFilter(XmlRootElement.class));
			Set<BeanDefinition> beans = provider.findCandidateComponents(Svn.class.getPackage().getName());
			ArrayList<Class<? extends RootBeanImpl>> c = new ArrayList<Class<? extends RootBeanImpl>>();
			for (BeanDefinition bd : beans) {
				try {
					c.add((Class<? extends RootBeanImpl>) Class.forName(bd.getBeanClassName()));
				} catch (ClassNotFoundException ex) {
					throw new RuntimeException(ex);
				}
			}
			Svn.jaxbContext = JAXBContext.newInstance(c.toArray(new Class[c.size()]));
		}
		return Svn.jaxbContext;
	}

	private static Marshaller getJaxbMarshaller() throws JAXBException {
		if (Svn.jaxbMarshaller == null) {
			Svn.jaxbMarshaller = Svn.getJaxbContext().createMarshaller();
			Svn.jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		}
		return Svn.jaxbMarshaller;
	}

	private static Unmarshaller getJaxbUnmarshaller() throws JAXBException {
		if (Svn.jaxbUnmarshaller == null) {
			Svn.jaxbUnmarshaller = Svn.getJaxbContext().createUnmarshaller();
		}
		return Svn.jaxbUnmarshaller;
	}

	public static SvnInfo info(File fp) {
		try (InputStream in = new BufferedInputStream(new FileInputStream(fp))) {
			return Svn.get(SvnInfo.class, in);
		} catch (IOException ex) {
			return null;
		}
	}

	public static SvnInfo info(InputStream in) {
		return Svn.get(SvnInfo.class, in);
	}

	public static SvnList lists(File fp) {
		try (InputStream in = new BufferedInputStream(new FileInputStream(fp))) {
			return Svn.get(SvnList.class, in);
		} catch (IOException ex) {
			return null;
		}
	}

	public static SvnList lists(InputStream in) {
		return Svn.get(SvnList.class, in);
	}

	public static SvnLog log(File fp) {
		try (InputStream in = new BufferedInputStream(new FileInputStream(fp))) {
			return Svn.get(SvnLog.class, in);
		} catch (IOException ex) {
			return null;
		}
	}

	public static SvnLog log(InputStream in) {
		return Svn.get(SvnLog.class, in);
	}

	public static SvnStatus status(File fp) {
		try (InputStream in = new BufferedInputStream(new FileInputStream(fp))) {
			return Svn.get(SvnStatus.class, in);
		} catch (IOException ex) {
			return null;
		}
	}

	public static SvnStatus status(InputStream in) {
		return Svn.get(SvnStatus.class, in);
	}
}
