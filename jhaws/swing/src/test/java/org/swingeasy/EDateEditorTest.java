package org.swingeasy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EDateEditorTest {
    @Test
    public void test() {
        EDateEditor e = new EDateEditor ();
        ELabel label = e.getLabel();
        Assertions.assertNotNull(label);
        System.out.println(label.getText());
        Assertions.assertNotNull(label.getText());
    }
}
