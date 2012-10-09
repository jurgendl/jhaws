package org.jhaws.common.ldap.interfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.log4j.Logger;
import org.jhaws.common.ldap.annotations.AnnotationParser;


/**
 * na
 *
 * @author Jurgen De Landsheer
 */
public abstract class LdapDAOCommonSuperclass<T extends Serializable & Comparable<? super T>> implements LdapDao<T> {
    /** serialVersionUID */
	private static final long serialVersionUID = -7411351913012966087L;

	/** Logger for this class */
    private static final Logger logger = Logger.getLogger(LdapDAOCommonSuperclass.class);

    /** byte array class */
    protected static final Class<?> BYTE_ARRAY_CLASS = new byte[0].getClass();
    
    /** byte array class */
    protected static final Class<?> STRING_ARRAY_CLASS = new String[0].getClass();
    
    /** pojoClass type */
    protected final Class<T> pojoClass;
    
    /** search on first level or deep */
    protected Search search = Search.SINGLE_LEVEL;
    
    /** info from annotations */
    protected final AnnotationParser annotationParser;

    /** cached {@link #getBase()} */
    protected String base = null;

    
    /**
     * Instantieer een nieuwe ALDI
     */
    @SuppressWarnings("unchecked")
	protected LdapDAOCommonSuperclass() {
        logger.debug("INIT start"); //$NON-NLS-1$
        this.pojoClass = (Class<T>) JGenerics.findImplementation(this, 0);
        logger.debug("pojoClass=" + pojoClass); //$NON-NLS-1$
        annotationParser = new AnnotationParser(pojoClass);
        logger.debug("INIT end"); //$NON-NLS-1$
    }
    
    /**
     * nieuwe instantie van bean; kan overridden worden in subclass en daarom private
     * 
     * @return T
     * 
     * @throws UnsupportedOperationException wanneer er geen default constructor is
     */
    protected T newBean() throws UnsupportedOperationException {
        try {
            return pojoClass.newInstance();
        } catch (final InstantiationException ex) {
            // wanneer er geen default constructor is
            throw new UnsupportedOperationException(
                    "default constructor not present: override Bean creation: 'private " + pojoClass.getSimpleName() //$NON-NLS-1$
                            + " newBean()' in class " + getClass().getName()); //$NON-NLS-1$
        } catch (final IllegalAccessException ex) {
            // constructor is bv private
            throw new UnsupportedOperationException(
                    "make sure the constructor is public or override Bean creation: 'private " //$NON-NLS-1$
                            + pojoClass.getSimpleName() + " newBean()' in class " + getClass().getName(), ex); //$NON-NLS-1$
        } catch (final Throwable ex) {
            // onbekende fout maar ook NullpointerException, zie naar inhoud default constructor pojo
            throw new UnsupportedOperationException("unknown exception: override Bean creation: 'private " //$NON-NLS-1$
                    + pojoClass.getSimpleName() + " newBean()' in class " + getClass().getName(), ex); //$NON-NLS-1$
        }
    }

    /**
     * creates new SearchControls based on settings
     *
     * @return SearchControls
     */
    protected final SearchControls newSearchControls() {
        SearchControls controls = new SearchControls();
        controls.setSearchScope(getSearchDepth().value());
        controls.setCountLimit(0);
        controls.setTimeLimit(0);
        controls.setReturningObjFlag(true);
        String[] atts = annotationParser.getMapping().values().toArray(new String[0]);
        controls.setReturningAttributes(atts);

        return controls;
    }
    

    /**
     * maakt base-dn string voor uivoeren queries
     * 
     * @return base-dn string
     */
    protected final String getBase() {
    	if(base==null) {
	        StringBuilder sb = new StringBuilder(annotationParser.getBaseNameList().size() * 4);
	
	        for (Iterator<KeyValue> iter = annotationParser.getBaseNameList().iterator(); iter.hasNext();) {
	            KeyValue kv = iter.next();
	            sb.append(kv.getKey());
	            sb.append("="); //$NON-NLS-1$
	            sb.append(kv.getValue());
	
	            if (iter.hasNext()) {
	                sb.append(","); //$NON-NLS-1$
	            }
	        }
	
	        base= sb.toString();
    	}
    	
    	return base;
    }

    /**
     * 
     * @see org.jhaws.common.ldap.interfaces.LdapDao#getSearchDepth()
     */
    public final Search getSearchDepth() {
        return this.search;
    }

    /**
     * 
     * @see org.jhaws.common.ldap.interfaces.LdapDao#setSearchDepth(org.jhaws.common.ldap.interfaces.Search)
     */
    public final void setSearchDepth(Search search) {
        this.search = search;
    }

    /**
     * gets pojoClass (no init)
     *
     * @return Returns the pojoClass.
     */
    protected final Class<T> getPojoClass() {
        return this.pojoClass;
    }    
    
    /**
     * @see org.jhaws.common.ldap.interfaces.LdapDao#findByPrimaryKey(java.lang.String)
     */
    public final T findByPrimaryKey(final String value) throws IllegalArgumentException {
        if (annotationParser.getKeyParts().size() > 1) {
            throw new IllegalArgumentException("key has more than 1 part, do not use this function"); //$NON-NLS-1$
        }

        Properties props = new Properties();
        props.setProperty(annotationParser.getKeyParts().get(0), value);

        return findByPrimaryKey(props);
    }
    
    /**
     * zoekt met query string (gebruik handmatig opgestelde string of classes zoals AndFilter etc)
     * 
     * @param query querystring
     * 
     * @return List van T objecten (mogelijks gelimiteerd aantal door ldap server); nooit null maar lege lijst
     */
    protected final List<T> findBeans(final String query) {
        List<T> toReturn = search(query, newSearchControls()); // voert search uit
            
        if(toReturn == null) {
            // geeft een lege lijst terug wanneer niets gevonden
            toReturn = new ArrayList<T>();
        }
        
        return toReturn;
    }
    
    /**
     * intern gebruik
     * 
     * @param query String
     * @param controls SearchControls
     * 
     * @return List T
     * 
     * @throws IllegalArgumentException login gegevens mogelijks verkeerd
     */
    protected abstract List<T> search(String query, SearchControls controls);
    
    /**
     * maakt distinguished name van bestaand object (maw de base-dn/key)
     * 
     * @param object bean object
     * 
     * @return distinguished Name
     * 
     * @throws InvalidNameException 
     */
    protected final Name buildDn(final Properties keyprops) throws InvalidNameException  {
        // maakt een referentie op naam aan via de de baseDn en key
        LdapName dn = new LdapName(""); //$NON-NLS-1$

        // deel 1 : base DN (baseDn kan leeg zijn)
        for (final KeyValue dnpart : annotationParser.getBaseNameList()) {
            dn.add(new Rdn(dnpart.getKey()+"="+dnpart.getValue())); //$NON-NLS-1$
        }

        // deel 2 : key (keyparts kan niet leeg zijn)
        for (final String dnpart : annotationParser.getKeyParts()) {
            String ldap = annotationParser.getMapping(dnpart);
            //String value = (String) get(object, dnpart);
            String value = keyprops.getProperty(dnpart);
            logger.debug("buildDn(" + getPojoClass().getSimpleName() + ") - ldap=" + ldap + ", value=" + value); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            dn.add(new Rdn(ldap+"="+value)); //$NON-NLS-1$
        }

        return dn;
    }
    
    /**
     * maakt distinguished name van bestaand object (maw de base-dn/key)
     * 
     * @param object bean object
     * 
     * @return distinguished Name
     * 
     * @throws InvalidNameException 
     */
    protected final Name buildDn(final T bean) throws InvalidNameException   {
        // maakt een referentie op naam aan via de de baseDn en key
        LdapName dn = new LdapName(""); //$NON-NLS-1$

        // deel 1 : base DN (baseDn kan leeg zijn)
        for (final KeyValue dnpart : annotationParser.getBaseNameList()) {
            dn.add(new Rdn(dnpart.getKey()+"="+dnpart.getValue())); //$NON-NLS-1$
        }

        // deel 2 : key (keyparts kan niet leeg zijn)
        for (final String dnpart : annotationParser.getKeyParts()) {
            String ldap = annotationParser.getMapping(dnpart);
            String value = (String) annotationParser.get(dnpart, bean);
            logger.debug("buildDn(" + getPojoClass().getSimpleName() + ") - ldap=" + ldap + ", value=" + value); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            dn.add(new Rdn(ldap+"="+value)); //$NON-NLS-1$
        }

        return dn;
    }
}
