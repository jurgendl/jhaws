package org.jhaws.common.ldap.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

/**
 * interface voor ldap dao
 * 
 * @author Jurgen
 */
public interface LdapDao<T extends Serializable & Comparable<? super T>> extends Serializable {
    /**
     * maakt een object aan (save). voor beveiliging moet een subclass een functie aanmaken en deze aanspreken
     * 
     * @param object bean object
     * 
     * @return success?
     */
    public abstract boolean createBean(final T object);

    /**
     * verwijderd een object aan (save). voor beveiliging moet een subclass een functie aanmaken en deze aanspreken
     * 
     * @param object bean object
     * 
     * @return success?
     */
    public abstract boolean deleteBean(final T object);

    /**
     * zoekt alle beans in base-dn
     * 
     * @return List van T objecten (mogelijks gelimiteerd aantal door ldap server)
     * 
     * @throws IllegalArgumentException objectClass not set
     */
    public abstract List<T> findAllBeans() throws IllegalArgumentException;

    /**
     * zoekt alle objecten dien voldoen aan een and relatie van like 'waarda'/equals 'waarde' van elke ingevulde object property die een string is en
     * niet null
     * 
     * @param bean voorbeeld; bean met ingevulde waardes
     * 
     * @return List van T objecten (mogelijks gelimiteerd aantal door ldap server)
     * 
     * @throws IllegalArgumentException objectClass not set
     */
    public abstract List<T> findBeansByExample(final T bean) throws IllegalArgumentException;

    /**
     * zoek op primary key
     * 
     * @param props waarden van 'class fieldname'='value'
     * 
     * @return bean nieuw object wanneer gevonden
     * 
     * @throws IllegalArgumentException key bestaat uit meer dan 1 deel, je kan deze functie dan niet gebruiken
     */
    public abstract T findByPrimaryKey(final Properties props) throws IllegalArgumentException;

    /**
     * zie findByPrimaryKey(final Properties props), werkt enkel wanneer de key bestaat uit 1 deel
     * 
     * @param value waarde
     * 
     * @return T
     * 
     * @throws IllegalArgumentException key bestaat uit meer dan 1 deel, je kan deze functie dan niet gebruiken
     */
    public abstract T findByPrimaryKey(final String value) throws IllegalArgumentException;

    /**
     * gets search level
     * 
     * @return Search
     */
    public abstract Search getSearchDepth();

    /**
     * sets search level
     * 
     * @param search Search
     */
    public abstract void setSearchDepth(Search search);

    /**
     * past een bestaand ldap object aan (update). voor beveiliging moet een subclass een functie aanmaken en deze aanspreken
     * 
     * @param object bean object
     * 
     * @return success?
     */
    public abstract boolean updateBean(final T object);
}