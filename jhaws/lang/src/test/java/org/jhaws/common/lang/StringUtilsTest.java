package org.jhaws.common.lang;

import org.junit.Test;

public class StringUtilsTest {
    // @Test
    // public void regexTest() {
    // {
    // String r1 = " +";
    // Assert.assertEquals(" ".replaceAll(r1, " "), " ");
    // Assert.assertEquals("a ".replaceAll(r1, " "), "a ");
    // Assert.assertEquals(" a".replaceAll(r1, " "), " a");
    // Assert.assertEquals(" a ".replaceAll(r1, " "), " a ");
    // }
    // {
    // String r2 = StringUtils.regexNotLookBehind("^( +)", " +");
    // Assert.assertEquals(" ".replaceAll(r2, " "), " ");
    // Assert.assertEquals("a ".replaceAll(r2, " "), "a ");
    // Assert.assertEquals(" a".replaceAll(r2, " "), " a");
    // Assert.assertEquals(" a ".replaceAll(r2, " "), " a ");
    // }
    // }

    @Test
    public void s6666666666666() {
        String string = "       x    y  z     ";
        char D = 172;
        string = StringUtils.replaceLeading(string, ' ', D);
        string = string.replaceFirst(" ++$", "").replaceAll(" ++", " ");
        string = StringUtils.replaceLeading(string, D, ' ');
        System.out.println(string + "---");
    }
}
