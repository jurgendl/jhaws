package org.jhaws.common.svn;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtils;
import org.jhaws.common.io.jaxb.JAXBMarshalling;
import org.jhaws.common.svn.xml.ObjectFactory;
import org.jhaws.common.svn.xml.SvnInfo;
import org.jhaws.common.svn.xml.SvnList;
import org.jhaws.common.svn.xml.SvnLog;
import org.jhaws.common.svn.xml.SvnRootBean;
import org.jhaws.common.svn.xml.SvnStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class Svn {
	private static final Logger logger = LoggerFactory.getLogger(Svn.class);

	private static JAXBMarshalling jaxbMarshalling;

	private static <T extends SvnRootBean> T get(Class<T> clazz, InputStream input) {
		byte[] data = new byte[0];
		try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			IOUtils.copy(input, output);
			IOUtils.closeQuietly(input);
			output.flush();
			IOUtils.closeQuietly(output);
			data = output.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
				T t = clazz.cast(getJaxbContext().unmarshall(clazz, in));
				try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
					getJaxbContext().marshall(t, os);
					t.toString(new String(os.toByteArray()));
				}
				return t;
			}
		} catch (IOException | RuntimeException | JAXBException ex) {
			logger.error("{}", ex);
			logger.error("{}", new String(data));
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	private static JAXBMarshalling getJaxbContext() throws JAXBException {
		if (jaxbMarshalling == null) {
			ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
					false);
			provider.addIncludeFilter(new AnnotationTypeFilter(XmlRootElement.class));
			Set<BeanDefinition> beans = provider.findCandidateComponents(ObjectFactory.class.getPackage().getName());
			ArrayList<Class<? extends SvnRootBean>> c = new ArrayList<>();
			for (BeanDefinition bd : beans) {
				try {
					c.add((Class<? extends SvnRootBean>) Class.forName(bd.getBeanClassName()));
				} catch (ClassNotFoundException ex) {
					throw new RuntimeException(ex);
				}
			}
			jaxbMarshalling = new JAXBMarshalling(c.toArray(new Class[c.size()]));
		}
		return jaxbMarshalling;
	}

	public static SvnInfo info(File fp) {
		try (InputStream in = new BufferedInputStream(new FileInputStream(fp))) {
			return Svn.get(SvnInfo.class, in);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static SvnInfo info(InputStream in) {
		return Svn.get(SvnInfo.class, in);
	}

	public static SvnList lists(File fp) {
		try (InputStream in = new BufferedInputStream(new FileInputStream(fp))) {
			return Svn.get(SvnList.class, in);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static SvnList lists(InputStream in) {
		return Svn.get(SvnList.class, in);
	}

	public static SvnLog log(File fp) {
		try (InputStream in = new BufferedInputStream(new FileInputStream(fp))) {
			return Svn.get(SvnLog.class, in);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static SvnLog log(InputStream in) {
		return Svn.get(SvnLog.class, in);
	}

	public static SvnStatus status(File fp) {
		try (InputStream in = new BufferedInputStream(new FileInputStream(fp))) {
			return Svn.get(SvnStatus.class, in);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static SvnStatus status(InputStream in) {
		return Svn.get(SvnStatus.class, in);
	}
}
