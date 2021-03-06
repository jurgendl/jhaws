package org.jhaws.common.lang;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

public class DateTime8Test {
    @Test
    public void test() {
        try {
            LocalDateTime t = LocalDateTime.now();
            Thread.sleep(500);
            Duration duration = Duration.between(t, t.plusHours(2).plusMinutes(35).plusSeconds(18).plusNanos(1234567890l));
            Assert.assertEquals("2h35m19" + '.' + "234s", DateTime8.printShort(duration, '.'));
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    @Test
    public void testPrint() {
        System.out.println(DateTime8.printUpToSeconds(DateTime8.toLocalTime(Duration.ofMillis(8921266l))));
        System.out.println(DateTime8.printShort(Duration.ofMillis(8921266l), '.'));
        System.out.println(DateTime8.printAlt(Duration.ofMillis(8921266l), '.'));
    }
}
