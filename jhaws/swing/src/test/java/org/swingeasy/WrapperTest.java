package org.swingeasy;

import org.junit.Assert;
import org.junit.Test;

public class WrapperTest {
    @Test
    public void test1() {
        EComboBox<String> comp = new EComboBox<String>(new EComboBoxConfig());
        EComboBox<String> safe = comp.stsi();

        Assert.assertEquals(safe, comp.stsi());
        Assert.assertEquals(safe, comp.STSI());
        Assert.assertEquals(safe, comp.getSimpleThreadSafeInterface());
        Assert.assertEquals(safe, safe.stsi());
        Assert.assertEquals(safe, safe.STSI());
        Assert.assertEquals(safe, safe.getSimpleThreadSafeInterface());

        Assert.assertEquals(comp, comp.getOriginal());
        Assert.assertEquals(comp, safe.getOriginal());
        Assert.assertEquals(comp, comp.getOriginal().getOriginal());
        Assert.assertEquals(comp, safe.getOriginal().getOriginal());
    }

    @Test
    public void test2() {
        new ECheckBoxTree<String>(new ECheckBoxTreeNode<String>(null)).stsi().setVisible(true);
        new EComboBox<String>(new EComboBoxConfig()).stsi().setVisible(true);
        new EList<String>(new EListConfig()).stsi().setVisible(true);
        new ETable<String>(new ETableConfig()).stsi().setVisible(true);
        new ETree<String>(new ETreeNode<String>(null)).stsi().setVisible(true);
        new ETreeTable<String>(new ETreeTableConfig(), new ETreeTableHeaders<String>("test")).stsi().setVisible(true);
    }
}
