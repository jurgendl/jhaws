package org.jhaws.logging;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTest {
    static final Logger LOGGER = LoggerFactory.getLogger(LoggingTest.class);


    @Test
    public void test() {
        LOGGER.trace("trace");
        LOGGER.debug("debug");
        LOGGER.info("info");
        LOGGER.warn("warn");
        LOGGER.error("error", new RuntimeException());
    }
}
