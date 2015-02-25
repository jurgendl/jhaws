package org.jhaws.common.io;

import org.junit.Assert;
import org.junit.Test;

public class FilePathTest {
    @Test
    public void test() {
        FilePath thisDir = new FilePath();
        FilePath p = new FilePath("dummyfile");
        Assert.assertNull(p.getParent());
        p = FilePath.class.cast(p.toAbsolutePath());
        Assert.assertEquals(thisDir, p.getParent());
    }
}
