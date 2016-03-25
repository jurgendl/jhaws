package org.swingeasy;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jurgen
 */
public class CommonTests {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void test() {
        ETable eTable = new ETable();
        eTable = new ETable(new ETableConfig());
        eTable.stsi().setEnabled(false);
        Assert.assertEquals(false, eTable.isEnabled());

        EList eList = new EList();
        eList = new EList(new EListConfig());
        eList.stsi().setEnabled(false);
        Assert.assertEquals(false, eList.isEnabled());

        EComboBox eComboBox = new EComboBox();
        eComboBox = new EComboBox(new EComboBoxConfig());
        eComboBox.stsi().setEnabled(false);
        Assert.assertEquals(false, eComboBox.isEnabled());

        ECheckBoxTree eCheckBoxTree = new ECheckBoxTree(new ECheckBoxTreeNode("test")); //$NON-NLS-1$
        eCheckBoxTree = new ECheckBoxTree(new ECheckBoxTreeConfig(), new ECheckBoxTreeNode("test")); //$NON-NLS-1$
        eCheckBoxTree.stsi().setEnabled(false);
        Assert.assertEquals(false, eCheckBoxTree.isEnabled());
    }
}
