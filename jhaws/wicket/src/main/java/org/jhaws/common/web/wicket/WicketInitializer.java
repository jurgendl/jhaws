package org.jhaws.common.web.wicket;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WicketInitializer implements IInitializer {
    private static final Logger logger = LoggerFactory.getLogger(WicketInitializer.class);

    @Override
    public void init(Application application) {
        WebHelper.run(this::warmUp);
    }

    @Override
    public void destroy(Application application) {
        //
    }

    protected void warmUp() {
        try {
            logger.info("warmup form css - start");
            // wicket specific warmup (once at startup)
            logger.info("warmup form css - end");
        } catch (Exception ex) {
            logger.error("warmup form css - {}", ex);
        }
    }
}
