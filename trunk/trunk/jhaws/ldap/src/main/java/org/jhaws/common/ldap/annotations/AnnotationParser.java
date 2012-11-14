package org.jhaws.common.ldap.annotations;

import org.apache.log4j.Logger;
import org.jhaws.common.ldap.interfaces.KeyValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

/**
 * collects ldap mapping information from annotated class and it's superclasses
 * 
 * @author Jurgen De Landsheer
 */
public final class AnnotationParser {
    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(AnnotationParser.class);

    /** base dn, bv ou=people, addBasePart("ou","people"), ordered */
    private final ArrayList<KeyValue> baseName = new ArrayList<KeyValue>();

    /** key, bv ugentId (key is fields name in class), ordered */
    private final ArrayList<String> keyParts = new ArrayList<String>();

    /** class type of fields (key is fields name in class), unique */
    private final Map<String, Class<?>> fieldTypes = new HashMap<String, Class<?>>();

    /** getter methods (key is fields name in class), unique */
    private final Map<String, Method> getters = new HashMap<String, Method>();

    /** setter methods (key is fields name in class), unique */
    private final Map<String, Method> setters = new HashMap<String, Method>();

    /** key=FIELD-name, value=LDAP-name (key is fields name in class), unique */
    private final Properties mapping = new Properties();

    /** objectClass in ldap */
    private final Set<String> objectClass = new HashSet<String>();

    /**
     * Creates a new AnnotationParser object.
     * 
     * @param pojoClass type pojoclass
     * 
     * @throws IllegalArgumentException "class /Class/ does not have the annotation LdapClass present"
     * @throws IllegalArgumentException "class /Class/: ldap annotation: key index incorrect"
     * @throws IllegalArgumentException "no keys found"
     * @throws IllegalArgumentException "getter/setter method getterMethodName/setterMethodName for field (field) not accessible/found"
     * @throws IllegalArgumentException "no return type found for getter method"
     */
    public AnnotationParser(final Class<?> pojoClass) {
        // creates info stack or throws IllegalArgumentException
        Stack<Class<?>> informationStack = initStep1(pojoClass);

        // sets first element for loop
        Class<?> currentClass = pojoClass;

        while (currentClass != null) {
            logger.debug("AnnotationParser(Class<?>) - Class<?> currentClass=" + currentClass); //$NON-NLS-1$
                                                                                                // currentClass = initStep2(superclass of
                                                                                                // currentClass, stack)
                                                                                                // null when currentClass not inherited or
                                                                                                // currentClass.superclass not an LdapClass

            currentClass = initStep2(currentClass, informationStack);
        }

        // traverse info stack and parse annotations
        while (!informationStack.empty()) {
            initStep3(informationStack.pop(), pojoClass);
        }

        // validates info and throw IllegalArgumentException when neccesary
        initStep4();
    }

    /**
     * gets baseName
     * 
     * @return Returns the baseName.
     */
    public ArrayList<KeyValue> getBaseNameList() {
        return this.baseName;
    }

    /**
     * geeft class type terug van field met meegegeven naam van class T
     * 
     * @param field fieldname
     * 
     * @return class type
     */
    public Class<?> getFieldType(final String field) {
        return fieldTypes.get(field);
    }

    /**
     * get class fieldname voor ldap fieldname
     * 
     * @param key class fieldname
     * 
     * @return ldap fieldname; null when not found
     * 
     * @see #getMapping()
     */
    public String getInverseMapping(final String key) {
        Properties mappingLocal = getMapping();

        for (Object o : mappingLocal.keySet()) {
            String k = o.toString();
            String v = mappingLocal.getProperty(k);

            if (v.equals(key)) {
                return k;
            }
        }

        return null;
    }

    /**
     * gets keyParts
     * 
     * @return Returns the keyParts.
     */
    public ArrayList<String> getKeyParts() {
        return this.keyParts;
    }

    /**
     * gets mapping
     * 
     * @return Returns the mapping.
     */
    public Properties getMapping() {
        return this.mapping;
    }

    /**
     * gets ldap fieldname voor class fieldname
     * 
     * @param key ldap fieldname
     * 
     * @return class fieldname; null when not found
     * 
     * @see #getMapping()
     */
    public String getMapping(final String key) {
        return getMapping().getProperty(key);
    }

    /**
     * gets objectClass
     * 
     * @return Returns the objectClass.
     */
    public String[] getObjectClass() {
        if (objectClass.size() == 0) {
            IllegalArgumentException ex = new IllegalArgumentException("no objectClass set"); //$NON-NLS-1$
            logger.error(ex, ex);
            throw ex;
        }

        return this.objectClass.toArray(new String[0]);
    }

    /**
     * invoke getter via class field name
     * 
     * @param field class field name
     * @param bean object
     * 
     * @return value
     * 
     * @throws IllegalArgumentException "getter not invokable"
     */
    public Object get(String field, Object bean) {
        try {
            return getters.get(field).invoke(bean, new Object[0]);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("getter not invokable"); //$NON-NLS-1$
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("getter not invokable"); //$NON-NLS-1$
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException("getter not invokable"); //$NON-NLS-1$
        }
    }

    /**
     * invoke getter via class field name
     * 
     * @param ldapfield class field name
     * @param bean object
     * 
     * @return value
     */
    public Object lget(final String ldapfield, final Object bean) {
        return get(getInverseMapping(ldapfield), bean);
    }

    /**
     * invoke setter via ldap field name
     * 
     * @param ldapfield ldap field name
     * @param bean object
     * @param value value
     */
    public void lset(final String ldapfield, final Object bean, final Object value) {
        set(getInverseMapping(ldapfield), bean, value);
    }

    /**
     * invoke setter via ldap field name
     * 
     * @param field ldap field name
     * @param bean object
     * @param value value
     * 
     * @throws IllegalArgumentException "setter not invokable"
     */
    public void set(final String field, final Object bean, final Object value) {
        try {
            setters.get(field).invoke(bean, new Object[] { value });
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("setter not invokable"); //$NON-NLS-1$
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("setter not invokable"); //$NON-NLS-1$
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException("setter not invokable"); //$NON-NLS-1$
        }
    }

    /**
     * see inside for info
     * 
     * @param field NA
     * @param ldapName NA
     * @param superPojoClass NA
     * 
     * @throws IllegalArgumentException "getter/setter method getterMethodName/setterMethodName for field (field) not accessible/found"; "no return
     *             type found for getter method"
     */
    private void addMapping(final String field, final String ldapName, final Class<?> superPojoClass) {
        String getterMethodName = "get" + Character.toUpperCase(field.charAt(0)) + field.substring(1); //$NON-NLS-1$
        logger.debug("addMapping(String, String, Class<?>) - String getterMethodName=" + getterMethodName); //$NON-NLS-1$

        Method getterMethod = null;

        try {
            getterMethod = superPojoClass.getMethod(getterMethodName, new Class[0]);
        } catch (SecurityException ex) {
            logger.error(ex, ex);
            throw new IllegalArgumentException("getter method " + getterMethodName + " for field " + field + " not accessible"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        } catch (NoSuchMethodException ex) {
            logger.error(ex, ex);
            throw new IllegalArgumentException("getter method " + getterMethodName + " for field " + field + " not found"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        Class<?> returns = getterMethod.getReturnType();
        logger.debug("addMapping(String, String, Class<?>) - Class<?> returns=" + returns); //$NON-NLS-1$

        if (returns == null) {
            throw new IllegalArgumentException("no return type found for getter method"); //$NON-NLS-1$
        }

        String setterMethodName = "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1); //$NON-NLS-1$
        logger.debug("addMapping(String, String, Class<?>) - String setterMethodName=" + setterMethodName); //$NON-NLS-1$

        Method setterMethod = null;

        try {
            setterMethod = superPojoClass.getMethod(setterMethodName, new Class[] { returns });
        } catch (SecurityException ex) {
            logger.error(ex, ex);
            throw new IllegalArgumentException("setter method " + setterMethodName + " for field " + field + " not accessible"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        } catch (NoSuchMethodException ex) {
            logger.error(ex, ex);
            throw new IllegalArgumentException("setter method " + setterMethodName + " for field " + field + " not found"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        this.mapping.setProperty(field, ldapName);
        this.getters.put(field, getterMethod);
        this.fieldTypes.put(field, returns);
        this.setters.put(field, setterMethod);
    }

    /**
     * see inside for info
     * 
     * @param pojoClass NA
     * 
     * @return NA
     */
    private Stack<Class<?>> initStep1(final Class<?> pojoClass) {
        logger.debug("initStep1(Class<?>) - start"); //$NON-NLS-1$
        logger.debug("initStep1(Class<?>) - Class<?> pojoClass=" + pojoClass); //$NON-NLS-1$

        // gets LdapClass annotation for current class
        LdapClass classAnnotation = pojoClass.getAnnotation(LdapClass.class);
        logger.debug("initStep1(Class<?>) - LdapClass classAnnotation=" + classAnnotation); //$NON-NLS-1$

        // annotation not found; throw exception
        if (classAnnotation == null) {
            IllegalArgumentException ex = new IllegalArgumentException(
                    "class " + pojoClass.getName() + " does not have the annotation LdapClass present"); //$NON-NLS-1$ //$NON-NLS-2$
            logger.error(ex, ex);
            throw ex;
        }

        // create new info stack and adds pojoclass, then returns is
        Stack<Class<?>> informationStack = new Stack<Class<?>>();
        informationStack.push(pojoClass);

        logger.debug("initStep1(Class<?>) - end"); //$NON-NLS-1$

        return informationStack;
    }

    /**
     * see inside for info
     * 
     * @param currentInspectedClass NA
     * @param informationStack NA
     * 
     * @return NA
     */
    private Class<?> initStep2(final Class<?> currentInspectedClass, final Stack<Class<?>> informationStack) {
        logger.debug("initStep2(Class<?>, Stack<Class<?>>) - start"); //$NON-NLS-1$
        logger.debug("initStep2(Class<?>, Stack<Class<?>>) - currentInspectedClass=" + currentInspectedClass); //$NON-NLS-1$

        // gets LdapClass annotation for current class
        LdapClass classAnnotation = currentInspectedClass.getAnnotation(LdapClass.class);
        boolean inherit = classAnnotation.inherited();
        logger.debug("initStep2(Class<?>, Stack<Class<?>>) - boolean inherit=" + inherit); //$NON-NLS-1$

        // this current class is not inherited: return null to stop loop
        if (!inherit) {
            logger.debug("initStep2(Class<?>, Stack<Class<?>>) - end"); //$NON-NLS-1$

            return null;
        }

        // gets superclass (warning: override existing currentInspectedClass)
        Class<?> nextCurrentInspectedClass = currentInspectedClass.getSuperclass();
        // gets superclass LdapClass annotation (warning: override existing classAnnotation)
        classAnnotation = nextCurrentInspectedClass.getAnnotation(LdapClass.class);
        logger.debug("initStep2(Class<?>, Stack<Class<?>>) - currentInspectedClass=" + currentInspectedClass + ", classAnnotation=" + classAnnotation); //$NON-NLS-1$ //$NON-NLS-2$

        // the new current class is not an LdapClass: return null to stop loop
        if (classAnnotation == null) {
            logger.debug("initStep2(Class<?>, Stack<Class<?>>) - end"); //$NON-NLS-1$

            return null;
        }

        // all ok, add to info stack
        informationStack.push(nextCurrentInspectedClass);

        logger.debug("initStep2(Class<?>, Stack<Class<?>>) - end"); //$NON-NLS-1$

        // returns sub class for loop
        return nextCurrentInspectedClass;
    }

    /**
     * see inside for info
     * 
     * @param currentClass NA
     * @param pojoClass NA
     */
    private void initStep3(final Class<?> currentClass, final Class<?> pojoClass) {
        logger.debug("initStep3(Class<?>) - start"); //$NON-NLS-1$
        logger.debug("initStep3(Class<?>) - Class<?> currentClass=" + currentClass); //$NON-NLS-1$

        LdapClass ldapClassAnnotation = currentClass.getAnnotation(LdapClass.class);

        for (String objectClassLocal : ldapClassAnnotation.objectClass()) {
            logger.debug("initStep3(Class<?>) - Set<String> objectClass=" + objectClassLocal); //$NON-NLS-1$
            this.objectClass.add(objectClassLocal);
        }

        for (LdapKeyValue dn : ldapClassAnnotation.dn()) {
            if (!dn.key().equals("")) { //$NON-NLS-1$

                KeyValue kv = new KeyValue(dn.key(), dn.value());
                logger.debug("initStep3(Class<?>) - KeyValue kv=" + kv); //$NON-NLS-1$
                this.baseName.add(kv);
            }
        }

        SortedMap<Integer, String> sortedKeys = new TreeMap<Integer, String>();
        boolean auto = ldapClassAnnotation.autoFields();
        logger.debug("initStep3(Class<?>) - boolean auto=" + auto); //$NON-NLS-1$

        for (Field field : currentClass.getDeclaredFields()) {
            String fieldName = field.getName();
            logger.debug("initStep3(Class<?>) - String fieldName=" + fieldName); //$NON-NLS-1$

            // skip serialVersionUID field
            if ("serialVersionUID".equals(fieldName)) { //$NON-NLS-1$
                logger.debug("initStep3(Class<?>) - continue"); //$NON-NLS-1$

                continue;
            }

            // check if it's a key first, then continue
            LdapKey ldapKey = field.getAnnotation(LdapKey.class);
            logger.debug("initStep3(Class<?>) - LdapKey ldapKey=" + ldapKey); //$NON-NLS-1$

            if (null != ldapKey) {
                int index = ldapKey.index();
                String ldapKeyName = ldapKey.value().equals("") ? fieldName : ldapKey.value(); //$NON-NLS-1$
                sortedKeys.put(new Integer(index), fieldName);
                addMapping(fieldName, ldapKeyName, pojoClass);
                logger.debug("initStep3(Class<?>) - add to mapping - int index=" + index + " fieldName=" + fieldName + ", ldapKeyName=" + ldapKeyName); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                logger.debug("initStep3(Class<?>) - continue"); //$NON-NLS-1$

                continue;
            }

            // check for LdapField or auto
            LdapField ldapField = field.getAnnotation(LdapField.class);

            // ldapField == null
            // --> auto == true
            // --> fieldName
            // --> auto == false
            // --> null
            // ldapField != null
            // --> ldapField.value().equals("") == true
            // --> fieldName
            // --> ldapField.value().equals("") == false
            // --> ldapField.value()
            String ldapName = (ldapField == null) ? (auto ? fieldName : null) : (ldapField.value().equals("") ? fieldName : ldapField.value()); //$NON-NLS-1$
            logger.debug("initStep3(Class<?>) - String ldapName=" + ldapName); //$NON-NLS-1$

            if (null != ldapName) {
                // add to field mapping
                addMapping(fieldName, ldapName, pojoClass);
                logger.debug("initStep3(Class<?>) - add to mapping - fieldName=" + fieldName + ", ldapName=" + ldapName); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        int last = -1;

        // builds key
        for (final Entry<Integer, String> sortedEntry : sortedKeys.entrySet()) {
            int index = sortedEntry.getKey().intValue();
            logger.debug("initStep3(Class<?>) - last=" + last + ", index=" + index); //$NON-NLS-1$ //$NON-NLS-2$

            if (last >= index) {
                IllegalArgumentException ex = new IllegalArgumentException(
                        "class " + currentClass.getName() + ": ldap annotation: key index incorrect"); //$NON-NLS-1$ //$NON-NLS-2$
                logger.error(ex, ex);
                throw ex;
            }

            String value = sortedEntry.getValue();
            logger.debug("initStep3(Class<?>) - String value=" + value); //$NON-NLS-1$
            this.keyParts.add(value);
            last = sortedEntry.getKey().intValue();
        }

        logger.debug("initStep3(Class<?>) - end"); //$NON-NLS-1$
    }

    /**
     * see inside for info
     */
    private void initStep4() {
        logger.debug("initStep4() - start"); //$NON-NLS-1$

        if (getKeyParts().size() == 0) {
            IllegalArgumentException ex = new IllegalArgumentException("no keys found"); //$NON-NLS-1$
            logger.error(ex, ex);
            throw ex;
        }

        if (objectClass.size() == 0) {
            logger.warn("no objectClass set, findAll/CRUD operations can't be used"); //$NON-NLS-1$
        }

        logger.debug("initStep4() - end"); //$NON-NLS-1$
    }
}
