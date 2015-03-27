package org.jhaws.common.ldap.spring;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.directory.SearchControls;

import org.jhaws.common.ldap.interfaces.LdapDAOCommonSuperclass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.UncategorizedLdapException;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapOperations;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;

/**
 * abstracte implementate voor ldap dao die <i>LdapOperations</i> (interface)
 * oftewel <i>LdapTemplate</i> (class) gebruikt.<br>
 * <br>
 * <br>
 * <u><b>config via annotations:</b></u><br>
 * <br>
 *
 * <pre>
 *              @LdapClass(dn = {  @LdapKeyValue(key = &quot;ou&quot;, value = &quot;people&quot;) } )
 *              public class Persoon
 *                     @LdapField(&quot;mail&quot;)
 *                     private String email;
 *                     @LdapField(&quot;uid&quot;)
 *                     private String login;
 *                     @LdapField(&quot;sn&quot;)
 *                     private String naam;
 *                     @LdapKey(value = &quot;ugentID&quot;, index = 0)
 *                     private String ugentId;
 *                     @LdapField(&quot;givenName&quot;)
 *                     private String voornaam;
 * </pre>
 *
 * <br>
 * <br>
 * the only overidable function is newBean
 *
 * @author Jurgen
 *
 * @param <T>
 *            object type (class) waarvoor de dao subclass geschikt voor is
 */
public abstract class AbstractSpringLdapDao<T extends Serializable & Comparable<? super T>> extends LdapDAOCommonSuperclass<T> {
	private static final long serialVersionUID = 4528890293912122131L;

	private static final Logger logger = LoggerFactory.getLogger(AbstractSpringLdapDao.class);

	/** LdapOperations is een interface voor LdapTemplate */
	private LdapOperations ldapOperations;

	private final ContextMapper<T> contextMapper = new ContextMapper<T>() {
		@SuppressWarnings("unchecked")
		@Override
		public T mapFromContext(Object ctx) {
			return (T) AbstractSpringLdapDao.this.mapFromContextImp(DirContextOperations.class.cast(ctx));
		}
	};

	/**
	 * Creates a new AbstractSpringLdapDao object.
	 */
	public AbstractSpringLdapDao() {
		super();
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#createBean(T)
	 */
	@Override
	public final boolean createBean(final T object) {
		try {
			Name dn = this.buildDn(object);
			this.getLdapOperations().bind(dn, this.getContextToBind(dn, object), null);

			return true;
		} catch (final Exception ex) {
			AbstractSpringLdapDao.logger.warn("{}", ex);

			return false;
		}
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#deleteBean(T)
	 */
	@Override
	public final boolean deleteBean(final T object) {
		try {
			this.getLdapOperations().unbind(this.buildDn(object));

			return true;
		} catch (final Exception ex) {
			AbstractSpringLdapDao.logger.warn("{}", ex);

			return false;
		}
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#findAllBeans()
	 */
	@Override
	public final List<T> findAllBeans() throws IllegalArgumentException {
		OrFilter filter = new OrFilter();

		for (final String objectClass : this.annotationParser.getObjectClass()) {
			filter.or(new EqualsFilter("objectClass", objectClass)); //$NON-NLS-1$
		}

		return this.findBeans(filter.encode());
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#findBeansByExample(T)
	 */
	@Override
	public final List<T> findBeansByExample(final T bean) throws IllegalArgumentException {
		AbstractSpringLdapDao.logger.debug("findAll() - start"); //$NON-NLS-1$
		AndFilter filter = new AndFilter();
		OrFilter ocfilter = new OrFilter();

		for (final String objectClass : this.annotationParser.getObjectClass()) {
			ocfilter.or(new EqualsFilter("objectClass", objectClass)); //$NON-NLS-1$
		}

		filter.and(ocfilter);

		for (final Object key : this.annotationParser.getMapping().keySet()) {
			String fieldName = key.toString();
			Object value = this.annotationParser.get(fieldName, bean);

			if ((value != null) && (value instanceof String)) {
				filter.and(new LikeFilter(this.annotationParser.getMapping().getProperty(fieldName), (String) value));
			}
		}

		String encode = filter.encode();
		AbstractSpringLdapDao.logger.info("findBeansByExample(T) - String encode=" + encode); //$NON-NLS-1$

		return this.findBeans(encode);
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#findByPrimaryKey(java.util.Properties)
	 */
	@Override
	public final T findByPrimaryKey(final Properties props) {
		try {
			return this.lookup(this.buildDn(props));
		} catch (Exception ex) {
			AbstractSpringLdapDao.logger.warn("{}", ex);

			return null;
		}
	}

	/**
	 * create DirContextOperations object from exiting object in ldap
	 *
	 * @param dn
	 *            dn
	 * @param object
	 *            bean object
	 *
	 * @return DirContextOperations
	 */
	private final DirContextOperations getContextToBind(final Name dn, final T object) {
		DirContextAdapter adapter = new DirContextAdapter();

		for (final Object field0 : this.annotationParser.getMapping().keySet()) {
			try {
				String field = field0.toString();
				String ldap = this.annotationParser.getMapping(field);
				Object value = this.annotationParser.get(field, object);
				Class<?> fieldType = this.annotationParser.getFieldType(field);
				AbstractSpringLdapDao.logger.debug("getContextToBind(" + this.getPojoClass().getSimpleName() + ") - field=" + field + ", ldap=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ ldap + ", value=" + value); //$NON-NLS-1$

				if (value != null) {
					if (fieldType.equals(LdapDAOCommonSuperclass.BYTE_ARRAY_CLASS)) {
						adapter.setAttributeValue(ldap, value);
					} else {
						if (fieldType.equals(LdapDAOCommonSuperclass.STRING_ARRAY_CLASS)) {
							if ("objectClass".equals(ldap)) { //$NON-NLS-1$
								value = this.annotationParser.getObjectClass();
							}

							adapter.setAttributeValues(ldap, (String[]) value);
						} else {
							adapter.setAttributeValue(ldap, value);
						}
					}
				}
			} catch (final Exception ex) {
				AbstractSpringLdapDao.logger.warn("{}", ex);
			}
		}

		adapter.setAttributeValues("objectClass", this.annotationParser.getObjectClass()); //$NON-NLS-1$

		return adapter;
	}

	/**
	 * gets ldapOperations
	 *
	 * @return Returns the ldapOperations.
	 */
	public final LdapOperations getLdapOperations() {
		return this.ldapOperations;
	}

	/**
	 * intern gebruik
	 *
	 * @param dn
	 *            DistinguishedName
	 *
	 * @return T
	 *
	 * @throws IllegalArgumentException
	 *             login gegevens mogelijks verkeerd
	 */
	private final T lookup(final Name dn) {
		AbstractSpringLdapDao.logger.debug("lookup(Name) - dn=" + dn); //$NON-NLS-1$

		try {
			Object lookup = this.ldapOperations.lookup(dn, new ContextMapper<Object>() {
				@Override
				public Object mapFromContext(Object ctx) {
					return AbstractSpringLdapDao.this.mapFromContextImp((DirContextOperations) ctx);
				}
			});
			return this.getPojoClass().cast(lookup);
		} catch (final UncategorizedLdapException ule) {
			if (ule.getCause().getClass().equals(InvalidNameException.class)) {
				throw new IllegalArgumentException("login gegevens mogelijks verkeerd", ule); //$NON-NLS-1$
			}

			throw ule;
		}
	}

	/**
	 * @see org.springframework.ldap.ContextMapper#mapFromContext(java.lang.Object)
	 */
	private final Object mapFromContextImp(final DirContextOperations ctx) {
		AbstractSpringLdapDao.logger.debug("DirContextOperations=" + ctx); //$NON-NLS-1$

		T bean = this.newBean();

		for (final Object field0 : this.annotationParser.getMapping().keySet()) {
			try {
				String field = field0.toString();
				String ldap = this.annotationParser.getMapping(field);
				Object value;

				/*
				 * als mapping als array is gezet (bv objectClass) dan moet de
				 * waarde worden opgevraagt met getStringAttributes kijkt naar
				 * de class van het veld, is het byte[] dan wordt automatisch
				 * ';binary' achter de ldap veldnaam gezet om de binaire waarde
				 * op te halen
				 */
				Class<?> fieldClass = this.annotationParser.getFieldType(field);
				AbstractSpringLdapDao.logger.debug("fieldClass=" + fieldClass); //$NON-NLS-1$

				if (fieldClass.equals(LdapDAOCommonSuperclass.BYTE_ARRAY_CLASS)) {
					value = ctx.getObjectAttribute(ldap + ";binary"); //$NON-NLS-1$
				} else {
					if (fieldClass.equals(LdapDAOCommonSuperclass.STRING_ARRAY_CLASS)) {
						value = ctx.getStringAttributes(ldap);
					} else {
						value = ctx.getObjectAttribute(ldap);
					}
				}

				AbstractSpringLdapDao.logger.debug("mapFromContext(" + ctx.getClass().getSimpleName() + ") - field=" + field + ", ldap=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ ldap + ", value=" + value); //$NON-NLS-1$

				// zet veldwaarde object via reflection met autocasting
				this.annotationParser.set(field, bean, value);
			} catch (final Exception ex) {
				AbstractSpringLdapDao.logger.error("{}", ex);
			}
		}

		return bean;
	}

	/**
	 *
	 * @see org.jhaws.common.ldap.interfaces.LdapDAOCommonSuperclass#search(java.lang.String,
	 *      javax.naming.directory.SearchControls)
	 */
	@Override
	protected final List<T> search(final String query, final SearchControls controls) {
		String _base = this.getBase();
		return this.search(_base, query, controls);
	}

	protected final List<T> search(String _base, final String query, final SearchControls controls) {
		AbstractSpringLdapDao.logger.debug("search(String, SearchControls) - start"); //$NON-NLS-1$
		AbstractSpringLdapDao.logger.debug("search(String, SearchControls) - base=" + _base + ", query=" + query); //$NON-NLS-1$ //$NON-NLS-2$

		List<T> list = null;

		try {
			list = this.ldapOperations.search(_base, query, controls, this.contextMapper);
		} catch (final UncategorizedLdapException ule) {
			if (ule.getCause().getClass().equals(InvalidNameException.class)) {
				throw new IllegalArgumentException("login gegevens zijn mogelijk verkeerd", ule); //$NON-NLS-1$
			}

			throw ule;
		}

		AbstractSpringLdapDao.logger.debug("search(String, SearchControls) - end"); //$NON-NLS-1$

		return list;
	}

	/**
	 * sets ldapOperations
	 *
	 * @param ldapOperations
	 *            The ldapOperations to set.
	 */
	public final void setLdapOperations(LdapOperations ldapOperations) {
		this.ldapOperations = ldapOperations;
	}

	/**
	 * @see org.jhaws.common.ldap.interfaces.LdapDao#updateBean(T)
	 */
	@Override
	public final boolean updateBean(final T object) {
		try {
			Name dn = this.buildDn(object);
			this.getLdapOperations().rebind(dn, this.getContextToBind(dn, object), null);

			return true;
		} catch (final Exception ex) {
			AbstractSpringLdapDao.logger.warn("{}", ex);

			return false;
		}
	}
}