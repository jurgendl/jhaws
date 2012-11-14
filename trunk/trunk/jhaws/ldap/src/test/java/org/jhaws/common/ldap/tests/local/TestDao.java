package org.jhaws.common.ldap.tests.local;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.jhaws.common.ldap.interfaces.LdapDao;

import java.util.Properties;

/**
 * na
 * 
 * @author Jurgen De Landsheer
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
        logger.debug("_testAll() - start"); //$NON-NLS-1$

        boolean run = false;

        if (!run) {
            logger.debug("_testAll() - end"); //$NON-NLS-1$

            return;
        }

        System.out.println("START TESTS"); //$NON-NLS-1$

        try {
            _testCreateBean();
            _testFindByPrimaryKeyString();
            _testUpdateBean();
            _testFindByPrimaryKeyProperties();
            _testFindBeansByExample();
            _testDeleteBean();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("STOP TESTS"); //$NON-NLS-1$
        logger.debug("_testAll() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#createBean(java.io.Serializable)}.
     */
    public void _testCreateBean() {
        logger.debug("_testCreateBean() - start"); //$NON-NLS-1$

        User u1 = new User();
        u1.setCn("cnn"); //$NON-NLS-1$
        u1.setDescription("descr"); //$NON-NLS-1$
        u1.setReference("refie"); //$NON-NLS-1$
        u1.setSurname("sur"); //$NON-NLS-1$
        u1.setTelephoneNumber("12112"); //$NON-NLS-1$
        assertTrue(dao.createBean(u1));
        logger.debug("_testCreateBean() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#deleteBean(java.io.Serializable)}.
     */
    public void _testDeleteBean() {
        logger.debug("_testDeleteBean() - start"); //$NON-NLS-1$

        assertTrue(dao.deleteBean(user));
        assertNull(dao.findByPrimaryKey("cnn")); //$NON-NLS-1$
        logger.debug("_testDeleteBean() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#findAllBeans()}.
     */
    public void _testFindAllBeans() {
        logger.debug("_testFindAllBeans() - start"); //$NON-NLS-1$

        assertEquals(2, dao.findAllBeans().size());
        logger.debug("_testFindAllBeans() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#findBeansByExample(java.io.Serializable)}.
     */
    public void _testFindBeansByExample() {
        logger.debug("_testFindBeansByExample() - start"); //$NON-NLS-1$

        User ex = new User();
        ex.setDescription("descr"); //$NON-NLS-1$
        assertEquals(1, dao.findBeansByExample(ex).size());
        logger.debug("_testFindBeansByExample() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#findByPrimaryKey(java.util.Properties)}.
     */
    public void _testFindByPrimaryKeyProperties() {
        logger.debug("_testFindByPrimaryKeyProperties() - start"); //$NON-NLS-1$

        Properties p = new Properties();
        p.setProperty("cn", "cnn"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("surname", dao.findByPrimaryKey(p).getSurname()); //$NON-NLS-1$
        logger.debug("_testFindByPrimaryKeyProperties() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.interfaces.LdapDAOCommonSuperclass#findByPrimaryKey(java.lang.String)}.
     */
    public void _testFindByPrimaryKeyString() {
        logger.debug("_testFindByPrimaryKeyString() - start"); //$NON-NLS-1$

        user = dao.findByPrimaryKey("cnn"); //$NON-NLS-1$
        assertEquals("sur", user.getSurname()); //$NON-NLS-1$
        logger.debug("_testFindByPrimaryKeyString() - end"); //$NON-NLS-1$
    }

    /**
     * Main method for {@link org.jhaws.common.ldap.spring.AbstractSpringLdapDao#updateBean(java.io.Serializable)}.
     */
    public void _testUpdateBean() {
        logger.debug("_testUpdateBean() - start"); //$NON-NLS-1$

        user.setSurname("surname"); //$NON-NLS-1$
        assertTrue(dao.updateBean(user));
        logger.debug("_testUpdateBean() - end"); //$NON-NLS-1$
    }

    /**
     * na
     */
    public void test() {
        logger.debug("test() - start"); //$NON-NLS-1$

        this.dao = new SpringUserDaoMock();
        _testAll();
        this.dao = new UserDaoMock();
        _testAll();
        logger.debug("test() - end"); //$NON-NLS-1$
    }
}
