package org.jhaws.common.net.client5;

import org.apache.hc.client5.http.auth.AuthScheme;
import org.apache.hc.client5.http.auth.AuthSchemeFactory;
import org.apache.hc.client5.http.impl.auth.NTLMScheme;
import org.apache.hc.core5.http.protocol.HttpContext;

public class JCIFSNTLMSchemeFactory implements AuthSchemeFactory {
    @Override
    public AuthScheme create(final HttpContext context) {
        return new NTLMScheme(new JCIFSEngine());
    }
}
