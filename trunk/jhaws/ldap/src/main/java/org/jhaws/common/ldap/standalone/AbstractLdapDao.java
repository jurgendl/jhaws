package org.jhaws.common.ldap.standalone;

import org.apache.log4j.Logger;
import org.jhaws.common.ldap.filters.And;
import org.jhaws.common.ldap.filters.Equal;
import org.jhaws.common.ldap.filters.Or;
import org.jhaws.common.ldap.interfaces.LdapDAOCommonSuperclass;


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

/**
 * na
 *
 * @author Jurgen De Landsheer
 */
public abstract class AbstractLdapDao<T extends Serializable & Comparable<? super T>> extends LdapDAOCommonSuperclass<T> {
    /** serialVersionUID */
	private static final long serialVersionUID = 8136185571396833318L;

	/** Logger for this class */
    private static final Logger logger = Logger.getLogger(AbstractLdapDao.class);

    /** ldap context */
    private ContextSource contextSource;
    
    /**
     * Creates a new AbstractLdapDao object.
     */
    public AbstractLdapDao() {
        super();
    }

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#createBean(java.io.Serializable)
     */
    public boolean createBean(T object) {
        try {
            contextSource.getContext().bind(buildDn(object), null, convertObjectToAttributes(object));
            
            return true;
        } catch (Exception ex) {
            //NamingException
            logger.warn(ex);
        }
        
        return false;
    }

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#deleteBean(java.io.Serializable)
     */
    public boolean deleteBean(T object) {
        try {
            contextSource.getContext().unbind(buildDn(object));
            
            return true;
        } catch (Exception ex) {
            //NamingException
            logger.warn(ex);
        }
        
        return false;
    }

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#findAllBeans()
     */
    public List<T> findAllBeans() throws IllegalArgumentException {
        Or query = new Or();
        
        for(String oc : annotationParser.getObjectClass()) {
            query.addFilters(new Equal("objectClass",oc)); //$NON-NLS-1$
        }
        
        return findBeans(query.toString());
    }

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#findBeansByExample(java.io.Serializable)
     */
    public List<T> findBeansByExample(T bean) throws IllegalArgumentException {
        Properties mapping = annotationParser.getMapping();
        And filter = new And();
        Or ocfilter = new Or();

        for (final String objectClass : annotationParser.getObjectClass()) {
            ocfilter.addFilters(new Equal("objectClass", objectClass)); //$NON-NLS-1$
        }

        filter.addFilters(ocfilter);

        for (final Object key : mapping.keySet()) {
            String fieldName = key.toString();
            Object value = annotationParser.get(fieldName, bean);

            if ((value != null) && value instanceof String) {
                filter.addFilters(new Equal(mapping.getProperty(fieldName), (String) value));
            }
        }

        String filterString = filter.toString();
        logger.info("findBeansByExample(T) - String filterString=" + filterString); //$NON-NLS-1$

        return findBeans(filterString);
    }

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#findByPrimaryKey(java.util.Properties)
     */
    public T findByPrimaryKey(Properties props) throws IllegalArgumentException {
        try {
            return convertAttributesToObject(((LdapContext) contextSource.getContext().lookup(buildDn(props))).getAttributes(""));  //$NON-NLS-1$
        } catch (Exception ex) {
            // InvalidNameException,NamingException
            logger.warn(ex);
        }
        
        return null;
    }

    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#updateBean(java.io.Serializable)
     */
    public boolean updateBean(T object) {
        try {
            contextSource.getContext().rebind(buildDn(object), null, convertObjectToAttributes(object));
            
            return true;
        } catch (Exception ex) {
            //NamingException
            logger.warn(ex);
        }
        
        return false;
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

        for (final Object field0 : annotationParser.getMapping().keySet()) {
            try {
                String field = field0.toString();
                String ldap = annotationParser.getMapping(field);
                Class<?> fieldClass = annotationParser.getFieldType(field);
                Object value = annotationParser.get(field, bean);
                Attribute attribute = null;
                
                if (fieldClass.equals(BYTE_ARRAY_CLASS)) {
                    attribute = new BasicAttribute(ldap+";binary", value); //$NON-NLS-1$
                } else {
                    if (fieldClass.equals(STRING_ARRAY_CLASS)) {
                        if("objectClass".equals(ldap)) { //$NON-NLS-1$
                            value = annotationParser.getObjectClass();
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
                //NullPointerException,SecurityException,NoSuchFieldException
                logger.warn(ex);
            }
        }
        
        logger.debug(attributes);
        
        return attributes;
    }
    
    /**
     * na
     *
     * @param attributes
     * 
     * @return
     */
    private final T convertAttributesToObject(final Attributes attributes) {
        T bean = newBean();
        
        for (final Object field0 : annotationParser.getMapping().keySet()) {
            try {
                String field = field0.toString();
                String ldap = annotationParser.getMapping(field);
                Object value = null;
                Class<?> fieldClass = annotationParser.getFieldType(field);
                
                if (fieldClass.equals(BYTE_ARRAY_CLASS)) {
                    value = attributes.get(ldap + ";binary").get(); //$NON-NLS-1$
                } else {
                    if (fieldClass.equals(STRING_ARRAY_CLASS)) {
                        NamingEnumeration<?> enumm = attributes.get(ldap).getAll();
                        ArrayList<String> vals = new ArrayList<String>();
                        
                        while(enumm.hasMore()) {
                            vals.add( enumm.next().toString() );
                        }
                        
                        value = vals.toArray(new String[0]);
                    } else {
                        value = attributes.get(ldap).get();
                    }
                }

                // zet veldwaarde object via reflection met autocasting
                annotationParser.set(field, bean, value);
            } catch (Exception ex) {
                //NamingException,NoSuchFieldException,SecurityException,NullPointerException
                logger.warn(ex);
            }
        }
        
        return bean;
    }

    /**
     * 
     * @see org.jhaws.common.ldap.interfaces.LdapDAOCommonSuperclass#search(java.lang.String, javax.naming.directory.SearchControls)
     */
    @Override
    protected final List<T> search(final String query, final SearchControls controls) {
        String _base = getBase();
        return search(_base, query, controls);
    }

	protected final List<T> search(String base, final String query,
			final SearchControls controls) {
		logger.debug("search(String, SearchControls) - start");  //$NON-NLS-1$
		logger.debug("search(String, SearchControls) - base=" + base + ", query=" + query); //$NON-NLS-1$ //$NON-NLS-2$

        List<T> list = new ArrayList<T>();

        try {
            NamingEnumeration<SearchResult> namingEnum = contextSource.getContext().search(base, query, controls);

            while (namingEnum.hasMore()) {
                SearchResult res = namingEnum.next();
                list.add(convertAttributesToObject(res.getAttributes()));
            }
        } catch (Exception ex) {
            //NamingException
            logger.warn(ex);
        }

        logger.debug("search(String, SearchControls) - end");  //$NON-NLS-1$

        return list;
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
     * sets contextSource
     *
     * @param contextSource The contextSource to set.
     */
    public final void setContextSource(ContextSource contextSource) {
        this.contextSource = contextSource;
    }
}
