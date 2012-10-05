package org.jhaws.common.ldap.spring;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.directory.SearchControls;

import org.apache.log4j.Logger;
import org.jhaws.common.ldap.interfaces.LdapDAOCommonSuperclass;
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
 * abstracte implementate voor ldap dao die <i>LdapOperations</i> (interface) oftewel <i>LdapTemplate</i> (class)
 * gebruikt.<br>
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
 * <br><br>
 * the only overidable function is newBean
 * 
 * @author Jurgen De Landsheer
 * 
 * @param <T> object type (class) waarvoor de dao subclass geschikt voor is
 */
public abstract class AbstractSpringLdapDao<T extends Serializable & Comparable<? super T>> extends LdapDAOCommonSuperclass<T> {
    /** serialVersionUID */
	private static final long serialVersionUID = 4528890293912122131L;

	/** Logger for this class */
    private static final Logger logger = Logger.getLogger(AbstractSpringLdapDao.class);

    /** LdapOperations is een interface voor LdapTemplate */
    private LdapOperations ldapOperations;

    /**
     * Creates a new AbstractSpringLdapDao object.
     */
    public AbstractSpringLdapDao() {
        super();
    }

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#findBeansByExample(T)
     */
    public final List<T> findBeansByExample(final T bean) throws IllegalArgumentException {
        logger.debug("findAll() - start"); //$NON-NLS-1$
        AndFilter filter = new AndFilter();
        OrFilter ocfilter = new OrFilter();

        for (final String objectClass : annotationParser.getObjectClass()) {
            ocfilter.or(new EqualsFilter("objectClass", objectClass)); //$NON-NLS-1$
        }

        filter.and(ocfilter);

        for (final Object key : annotationParser.getMapping().keySet()) {
            String fieldName = key.toString();
            Object value = annotationParser.get(fieldName, bean);

            if ((value != null) && value instanceof String) {
                filter.and(new LikeFilter(annotationParser.getMapping().getProperty(fieldName), (String) value));
            }
        }

        String encode = filter.encode();
        logger.info("findBeansByExample(T) - String encode=" + encode); //$NON-NLS-1$

        return findBeans(encode);
    }

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#findByPrimaryKey(java.util.Properties)
     */
    public final T findByPrimaryKey(final Properties props) {
        try {
            return lookup(buildDn(props));
        } catch (Exception ex) {
            logger.warn(ex);
            
            return null;
        }
    }
   
    /**
     * @see org.springframework.ldap.ContextMapper#mapFromContext(java.lang.Object)
     */
    private final Object mapFromContextImp(final DirContextOperations ctx) {
        logger.debug("DirContextOperations=" + ctx); //$NON-NLS-1$

        T bean = newBean();

        for (final Object field0 : annotationParser.getMapping().keySet()) {
            try {
                String field = field0.toString();
                String ldap = annotationParser.getMapping(field);
                Object value;

                /*
                 * als mapping als array is gezet (bv objectClass) dan moet de waarde worden opgevraagt met
                 * getStringAttributes kijkt naar de class van het veld, is het byte[] dan wordt automatisch ';binary'
                 * achter de ldap veldnaam gezet om de binaire waarde op te halen
                 */
                Class<?> fieldClass = annotationParser.getFieldType(field);
                logger.debug("fieldClass=" + fieldClass); //$NON-NLS-1$

                if (fieldClass.equals(BYTE_ARRAY_CLASS)) {
                    value = ctx.getObjectAttribute(ldap + ";binary"); //$NON-NLS-1$
                } else {
                    if (fieldClass.equals(STRING_ARRAY_CLASS)) {
                        value = ctx.getStringAttributes(ldap);
                    } else {
                        value = ctx.getObjectAttribute(ldap);
                    }
                }

                logger.debug("mapFromContext(" + ctx.getClass().getSimpleName() + ") - field=" + field + ", ldap=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + ldap + ", value=" + value); //$NON-NLS-1$

                // zet veldwaarde object via reflection met autocasting
                annotationParser.set(field, bean, value);
            } catch (final Exception ex) {
                logger.error(ex, ex);
            }
        }

        return bean;
    }

    /**
     * intern gebruik
     * 
     * @param dn DistinguishedName
     * 
     * @return T
     * 
     * @throws IllegalArgumentException login gegevens mogelijks verkeerd
     */
    private final T lookup(final Name dn) {
        logger.debug("lookup(Name) - dn=" + dn); //$NON-NLS-1$
        
        try {
            return getPojoClass().cast(ldapOperations.lookup(dn, new ContextMapper() {
                public Object mapFromContext(Object ctx) {
                    return mapFromContextImp((DirContextOperations) ctx);
                }
            }));
        } catch (final UncategorizedLdapException ule) {
            if (ule.getCause().getClass().equals(InvalidNameException.class)) {
                throw new IllegalArgumentException("login gegevens mogelijks verkeerd", ule); //$NON-NLS-1$
            } 
            
            throw ule;            
        }
    }

    private final ContextMapper contextMapper = new ContextMapper() {
        public Object mapFromContext(Object ctx) {
            return mapFromContextImp((DirContextOperations) ctx);
        }
    };
    
    /**
     * 
     * @see org.jhaws.common.ldap.interfaces.LdapDAOCommonSuperclass#search(java.lang.String, javax.naming.directory.SearchControls)
     */
    @Override
    protected final List<T> search(final String query, final SearchControls controls) {
        String _base = getBase();
        return search(_base, query, controls);
    }

	@SuppressWarnings("unchecked")
	protected final List<T> search(String base, final String query,
			final SearchControls controls) {
		logger.debug("search(String, SearchControls) - start");  //$NON-NLS-1$
		logger.debug("search(String, SearchControls) - base=" + base + ", query=" + query); //$NON-NLS-1$ //$NON-NLS-2$
        
        List<T> list = null;

        try {
            list = ldapOperations.search(base, query, controls, contextMapper);
        } catch (final UncategorizedLdapException ule) {
            if (ule.getCause().getClass().equals(InvalidNameException.class)) {
                throw new IllegalArgumentException("login gegevens zijn mogelijk verkeerd", ule); //$NON-NLS-1$
            }
            
            throw ule;
        }

       logger.debug("search(String, SearchControls) - end");  //$NON-NLS-1$
        
        return list;
	}

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#createBean(T)
     */
    public final boolean createBean(final T object) {
        try {
            Name dn = buildDn(object);
            getLdapOperations().bind(dn, getContextToBind(dn, object), null);

            return true;
        } catch (final Exception ex) {
            logger.warn(ex);

            return false;
        }
    }

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#findAllBeans()
     */
    public final List<T> findAllBeans() throws IllegalArgumentException {
        OrFilter filter = new OrFilter();

        for (final String objectClass : annotationParser.getObjectClass()) {
            filter.or(new EqualsFilter("objectClass", objectClass)); //$NON-NLS-1$
        }

        return findBeans(filter.encode());
    }

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#deleteBean(T)
     */
    public final boolean deleteBean(final T object) {
        try {
            getLdapOperations().unbind(buildDn(object));

            return true;
        } catch (final Exception ex) {
            logger.warn(ex);

            return false;
        }
    }

    /**
     * create DirContextOperations object from exiting object in ldap
     * 
     * @param dn dn
     * @param object bean object
     * 
     * @return DirContextOperations
     */
    private final DirContextOperations getContextToBind(final Name dn, final T object) {
        DirContextAdapter adapter = new DirContextAdapter();

        for (final Object field0 : annotationParser.getMapping().keySet()) {
            try {
                String field = field0.toString();
                String ldap = annotationParser.getMapping(field);
                Object value = annotationParser.get(field, object);
                Class<?> fieldType = annotationParser.getFieldType(field);
                logger.debug("getContextToBind(" + getPojoClass().getSimpleName() + ") - field=" + field + ", ldap=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + ldap + ", value=" + value); //$NON-NLS-1$

                if (value != null) {
                    if (fieldType.equals(BYTE_ARRAY_CLASS)) {
                        adapter.setAttributeValue(ldap, value);
                    } else {
                        if (fieldType.equals(STRING_ARRAY_CLASS)) {
                            if("objectClass".equals(ldap)) { //$NON-NLS-1$
                                value = annotationParser.getObjectClass();
                            }
                            
                            adapter.setAttributeValues(ldap, (String[]) value);
                        } else {
                            adapter.setAttributeValue(ldap, value);
                        }
                    }
                }
            } catch (final Exception ex) {
                logger.warn(ex, ex);
            }
        }

        adapter.setAttributeValues("objectClass", annotationParser.getObjectClass()); //$NON-NLS-1$

        return adapter;
    }

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#updateBean(T)
     */
    public final boolean updateBean(final T object) {
        try {
            Name dn = buildDn(object);
            getLdapOperations().rebind(dn, getContextToBind(dn, object), null);            

            return true;
        } catch (final Exception ex) {
            logger.warn(ex);

            return false;
        }
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
     * sets ldapOperations
     *
     * @param ldapOperations The ldapOperations to set.
     */
    public final void setLdapOperations(LdapOperations ldapOperations) {
        this.ldapOperations = ldapOperations;
    }
}