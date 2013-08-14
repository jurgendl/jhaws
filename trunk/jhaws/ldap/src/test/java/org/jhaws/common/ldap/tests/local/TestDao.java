package org.jhaws.common.ldap.tests.local;

import java.util.Properties;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.jhaws.common.ldap.interfaces.LdapDao;

/**
 * @author Jurgen
 */
public class TestDao extends TestCase {
    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(TestDao.class);

    /** field */
    private LdapDao<User> dao = null;

    /** field */
    private User user = null;

    /**
     * na
     */
    public void _testAll() {
        TestDao.logger.debug("_testAll() - start"); //$NON-NLS-1$

        boolean run = false;

        if (!run) {
            TestDao.logger.debug("_testAll() - end"); //$NON-NLS-1$

            return;
        }

        System.out.println("START TESTS"); //$NON-NLS-1$

        try {
            this._testCreateBean();
            this._testFindByPrimaryKeyString();
            this._testUpdateBean();
            this._testFindByPrimaryKeyProperties();
            this._testFindBeansByExample();
            this._testDeleteBean();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("STOP TESTS"); //$NON-NLS-1$
        TestDao.logger.debug("_testAll() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#createBean(java.io.Serializable)}.
     */
    public void _testCreateBean() {
        TestDao.logger.debug("_testCreateBean() - start"); //$NON-NLS-1$

        User u1 = new User();
        u1.setCn("cnn"); //$NON-NLS-1$
        u1.setDescription("descr"); //$NON-NLS-1$
        u1.setReference("refie"); //$NON-NLS-1$
        u1.setSurname("sur"); //$NON-NLS-1$
        u1.setTelephoneNumber("12112"); //$NON-NLS-1$
        Assert.assertTrue(this.dao.createBean(u1));
        TestDao.logger.debug("_testCreateBean() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#deleteBean(java.io.Serializable)}.
     */
    public void _testDeleteBean() {
        TestDao.logger.debug("_testDeleteBean() - start"); //$NON-NLS-1$

        Assert.assertTrue(this.dao.deleteBean(this.user));
        Assert.assertNull(this.dao.findByPrimaryKey("cnn")); //$NON-NLS-1$
        TestDao.logger.debug("_testDeleteBean() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#findAllBeans()}.
     */
    public void _testFindAllBeans() {
        TestDao.logger.debug("_testFindAllBeans() - start"); //$NON-NLS-1$

        Assert.assertEquals(2, this.dao.findAllBeans().size());
        TestDao.logger.debug("_testFindAllBeans() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#findBeansByExample(java.io.Serializable)}.
     */
    public void _testFindBeansByExample() {
        TestDao.logger.debug("_testFindBeansByExample() - start"); //$NON-NLS-1$

        User ex = new User();
        ex.setDescription("descr"); //$NON-NLS-1$
        Assert.assertEquals(1, this.dao.findBeansByExample(ex).size());
        TestDao.logger.debug("_testFindBeansByExample() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#findByPrimaryKey(java.util.Properties)}.
     */
    public void _testFindByPrimaryKeyProperties() {
        TestDao.logger.debug("_testFindByPrimaryKeyProperties() - start"); //$NON-NLS-1$

        Properties p = new Properties();
        p.setProperty("cn", "cnn"); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals("surname", this.dao.findByPrimaryKey(p).getSurname()); //$NON-NLS-1$
        TestDao.logger.debug("_testFindByPrimaryKeyProperties() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.interfaces.LdapDAOCommonSuperclass#findByPrimaryKey(java.lang.String)}.
     */
    public void _testFindByPrimaryKeyString() {
        TestDao.logger.debug("_testFindByPrimaryKeyString() - start"); //$NON-NLS-1$

        this.user = this.dao.findByPrimaryKey("cnn"); //$NON-NLS-1$
        Assert.assertEquals("sur", this.user.getSurname()); //$NON-NLS-1$
        TestDao.logger.debug("_testFindByPrimaryKeyString() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#updateBean(java.io.Serializable)}.
     */
    public void _testUpdateBean() {
        TestDao.logger.debug("_testUpdateBean() - start"); //$NON-NLS-1$

        this.user.setSurname("surname"); //$NON-NLS-1$
        Assert.assertTrue(this.dao.updateBean(this.user));
        TestDao.logger.debug("_testUpdateBean() - end"); //$NON-NLS-1$
    }

    /**
     * na
     */
    public void test() {
        TestDao.logger.debug("test() - start"); //$NON-NLS-1$

        this.dao = new SpringUserDaoMock();
        this._testAll();
        this.dao = new UserDaoMock();
        this._testAll();
        TestDao.logger.debug("test() - end"); //$NON-NLS-1$
    }
}
