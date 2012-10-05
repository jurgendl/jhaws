package org.jhaws.common.ldap.tests.local;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jhaws.common.ldap.annotations.LdapClass;
import org.jhaws.common.ldap.annotations.LdapField;
import org.jhaws.common.ldap.annotations.LdapKey;
import org.jhaws.common.ldap.annotations.LdapKeyValue;


import java.io.Serializable;


/**
 * na
 *
 * @author Jurgen De Landsheer
 */
@LdapClass(objectClass = "person", dn = @LdapKeyValue(key = "ou", value = "users") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

)
public class User implements Serializable, Comparable<User> {
    /** serialVersionUID */
    private static final long serialVersionUID = -1064423243267474243L;

    /** field */
    @LdapKey
    private String cn;

    /** field */
    @LdapField
    private String description;

    /** field */
    @LdapField(value = "seealso")
    private String reference;

    /** field */
    @LdapField(value = "sn")
    private String surname;

    /** field */
    @LdapField(value = "telephonenumber")
    private String telephoneNumber;

    /** field */
    @LdapField
    private String[] objectClass;

    /**
     * Setter voor cn
     *
     * @param cn The cn to set.
     */
    public final void setCn(String cn) {
        this.cn = cn;
    }

    /**
     * Getter voor cn
     *
     * @return Returns the cn.
     */
    public final String getCn() {
        return this.cn;
    }

    /**
     * Setter voor description
     *
     * @param description The description to set.
     */
    public final void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter voor description
     *
     * @return Returns the description.
     */
    public final String getDescription() {
        return this.description;
    }

    /**
     * Setter voor objectClass
     *
     * @param objectClass The objectClass to set.
     */
    public final void setObjectClass(String[] objectClass) {
        this.objectClass = objectClass;
    }

    /**
     * Getter voor objectClass
     *
     * @return Returns the objectClass.
     */
    public final String[] getObjectClass() {
        return this.objectClass;
    }

    /**
     * Setter voor reference
     *
     * @param seealso The reference to set.
     */
    public final void setReference(String seealso) {
        this.reference = seealso;
    }

    /**
     * Getter voor reference
     *
     * @return Returns the reference.
     */
    public final String getReference() {
        return this.reference;
    }

    /**
     * Setter voor surname
     *
     * @param sn The surname to set.
     */
    public final void setSurname(String sn) {
        this.surname = sn;
    }

    /**
     * Getter voor surname
     *
     * @return Returns the surname.
     */
    public final String getSurname() {
        return this.surname;
    }

    /**
     * Setter voor telephoneNumber
     *
     * @param telephonenumber The telephoneNumber to set.
     */
    public final void setTelephoneNumber(String telephonenumber) {
        this.telephoneNumber = telephonenumber;
    }

    /**
     * Getter voor telephoneNumber
     *
     * @return Returns the telephoneNumber.
     */
    public final String getTelephoneNumber() {
        return this.telephoneNumber;
    }

    /**
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(User o) {
        return 0;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("description", this.description).append("surname", this.surname).append("telephoneNumber", this.telephoneNumber).append("reference", this.reference).append("cn", this.cn).append("objectClass", this.objectClass).toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
    }

    /**
     * 
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }

        User rhs = (User) object;

        return new EqualsBuilder().appendSuper(super.equals(object)).append(this.cn, rhs.cn).append(this.description, rhs.description).append(this.telephoneNumber, rhs.telephoneNumber).append(this.surname, rhs.surname).append(this.objectClass, rhs.objectClass).append(this.reference, rhs.reference).isEquals();
    }

    /**
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1529639275, 107903843).appendSuper(super.hashCode()).append(this.cn).append(this.description).append(this.telephoneNumber).append(this.surname).append(this.objectClass).append(this.reference).toHashCode();
    }
}
