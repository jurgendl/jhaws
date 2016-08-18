package org.jhaws.common.ldap.standalone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.jhaws.common.ldap.filters.And;
import org.jhaws.common.ldap.filters.Equal;
import org.jhaws.common.ldap.filters.Or;
import org.jhaws.common.ldap.interfaces.LdapDAOCommonSuperclass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jurgen
 */
public abstract class AbstractLdapDao<T extends Serializable & Comparable<? super T>> extends LdapDAOCommonSuperclass<T> {
	private static final long serialVersionUID = 8136185571396833318L;

	private static final Logger logger = LoggerFactory.getLogger(AbstractLdapDao.class);

	/** ldap context */
	private ContextSource contextSource;

	/**
	 * Creates a new AbstractLdapDao object.
	 */
	public AbstractLdapDao() {
		super();
	}

	/**
	 * na
	 *
	 * @param attributes
	 *
	 * @return
	 */
	private final T convertAttributesToObject(final Attributes attributes) {
		T bean = this.newBean();

		for (final Object field0 : this.annotationParser.getMapping().keySet()) {
			try {
				String field = field0.toString();
				String ldap = this.annotationParser.getMapping(field);
				Object value = null;
				Class<?> fieldClass = this.annotationParser.getFieldType(field);

				if (fieldClass.equals(LdapDAOCommonSuperclass.BYTE_ARRAY_CLASS)) {
					value = attributes.get(ldap + ";binary").get(); //$NON-NLS-1$
				} else {
					Attribute attributeValue = attributes.get(ldap);
					if (attributeValue != null) {
						if (fieldClass.equals(LdapDAOCommonSuperclass.STRING_ARRAY_CLASS)) {
							NamingEnumeration<?> enumm = attributeValue.getAll();
							ArrayList<String> vals = new ArrayList<>();

							while (enumm.hasMore()) {
								vals.add(enumm.next().toString());
							}

							value = vals.toArray(new String[0]);
						} else {
							value = attributeValue.get();
						}
					}
				}

				// zet veldwaarde object via reflection met autocasting
				this.annotationParser.set(field, bean, value);
			} catch (Exception ex) {
				// NamingException,NoSuchFieldException,SecurityException,NullPointerException
				AbstractLdapDao.logger.warn("{}", ex);
			}
		}

		return bean;
	}

	/**
	 * na
	 *
	 * @param object
	 *
	 * @return
	 */
	private final Attributes convertObjectToAttributes(T bean) {
		Attributes attributes = new BasicAttributes();

		for (final Object field0 : this.annotationParser.getMapping().keySet()) {
			try {
				String field = field0.toString();
				String ldap = this.annotationParser.getMapping(field);
				Class<?> fieldClass = this.annotationParser.getFieldType(field);
				Object value = this.annotationParser.get(field, bean);
				Attribute attribute = null;

				if (fieldClass.equals(LdapDAOCommonSuperclass.BYTE_ARRAY_CLASS)) {
					attribute = new BasicAttribute(ldap + ";binary", value); //$NON-NLS-1$
				} else {
					if (fieldClass.equals(LdapDAOCommonSuperclass.STRING_ARRAY_CLASS)) {
						if ("objectClass".equals(ldap)) { //$NON-NLS-1$
							value = this.annotationParser.getObjectClass();
						}

						attribute = new BasicAttribute(ldap);

						for (String s : (String[]) value) {
							attribute.add(s);
						}
					} else {
						attribute = new BasicAttribute(ldap, value);
					}
				}

				attributes.put(attribute);
			} catch (Exception ex) {
				// NullPointerException,SecurityException,NoSuchFieldException
				AbstractLdapDao.logger.warn("{}", ex);
			}
		}

		AbstractLdapDao.logger.debug("{}", attributes);

		return attributes;
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#createBean(java.io.Serializable)
	 */
	@Override
	public boolean createBean(T object) {
		try {
			this.contextSource.getContext().bind(this.buildDn(object), null, this.convertObjectToAttributes(object));

			return true;
		} catch (Exception ex) {
			// NamingException
			AbstractLdapDao.logger.warn("{}", ex);
		}

		return false;
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#deleteBean(java.io.Serializable)
	 */
	@Override
	public boolean deleteBean(T object) {
		try {
			this.contextSource.getContext().unbind(this.buildDn(object));

			return true;
		} catch (Exception ex) {
			// NamingException
			AbstractLdapDao.logger.warn("{}", ex);
		}

		return false;
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#findAllBeans()
	 */
	@Override
	public List<T> findAllBeans() throws IllegalArgumentException {
		Or query = new Or();

		for (String oc : this.annotationParser.getObjectClass()) {
			query.addFilters(new Equal("objectClass", oc)); //$NON-NLS-1$
		}

		return this.findBeans(query.toString());
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#findBeansByExample(java.io.Serializable)
	 */
	@Override
	public List<T> findBeansByExample(T bean) throws IllegalArgumentException {
		Properties mapping = this.annotationParser.getMapping();
		And filter = new And();
		Or ocfilter = new Or();

		for (final String objectClass : this.annotationParser.getObjectClass()) {
			ocfilter.addFilters(new Equal("objectClass", objectClass)); //$NON-NLS-1$
		}

		filter.addFilters(ocfilter);

		for (final Object key : mapping.keySet()) {
			String fieldName = key.toString();
			Object value = this.annotationParser.get(fieldName, bean);

			if ((value != null) && (value instanceof String)) {
				filter.addFilters(new Equal(mapping.getProperty(fieldName), (String) value));
			}
		}

		String filterString = filter.toString();
		AbstractLdapDao.logger.info("findBeansByExample(T) - String filterString=" + filterString); //$NON-NLS-1$

		return this.findBeans(filterString);
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#findByPrimaryKey(java.util.Properties)
	 */
	@Override
	public T findByPrimaryKey(Properties props) throws IllegalArgumentException {
		try {
			LdapContext lookup = (LdapContext) this.contextSource.getContext().lookup(this.buildDn(props));
			String[] atts = this.annotationParser.getMapping().values().toArray(new String[0]);
			Attributes attributes = lookup.getAttributes("", atts);//$NON-NLS-1$
			return this.convertAttributesToObject(attributes);
		} catch (Exception ex) {
			// InvalidNameException,NamingException
			AbstractLdapDao.logger.warn("{}", ex);
		}

		return null;
	}

	/**
	 * gets contextSource
	 *
	 * @return Returns the contextSource.
	 */
	public final ContextSource getContextSource() {
		return this.contextSource;
	}

	/**
	 *
	 * @see org.jhaws.common.ldap.interfaces.LdapDAOCommonSuperclass#search(java.lang.String, javax.naming.directory.SearchControls)
	 */
	@Override
	protected final List<T> search(final String query, final SearchControls controls) {
		String _base = this.getBase();
		return this.search(_base, query, controls);
	}

	protected final List<T> search(String _base, final String query, final SearchControls controls) {
		AbstractLdapDao.logger.debug("search(String, SearchControls) - start"); //$NON-NLS-1$
		AbstractLdapDao.logger.debug("search(String, SearchControls) - base=" + _base + ", query=" + query); //$NON-NLS-1$ //$NON-NLS-2$

		List<T> list = new ArrayList<>();

		try {
			NamingEnumeration<SearchResult> namingEnum = this.contextSource.getContext().search(_base, query, controls);

			while (namingEnum.hasMore()) {
				SearchResult res = namingEnum.next();
				list.add(this.convertAttributesToObject(res.getAttributes()));
			}
		} catch (Exception ex) {
			// NamingException
			AbstractLdapDao.logger.warn("{}", ex);
		}

		AbstractLdapDao.logger.debug("search(String, SearchControls) - end"); //$NON-NLS-1$

		return list;
	}

	/**
	 * sets contextSource
	 *
	 * @param contextSource
	 *            The contextSource to set.
	 */
	public final void setContextSource(ContextSource contextSource) {
		this.contextSource = contextSource;
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#updateBean(java.io.Serializable)
	 */
	@Override
	public boolean updateBean(T object) {
		try {
			this.contextSource.getContext().rebind(this.buildDn(object), null, this.convertObjectToAttributes(object));

			return true;
		} catch (Exception ex) {
			// NamingException
			AbstractLdapDao.logger.warn("{}", ex);
		}

		return false;
	}
}
