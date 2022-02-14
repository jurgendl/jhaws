package org.jhaws.common.net.client5;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.apache.hc.client5.http.impl.auth.NTLMEngine;
import org.apache.hc.client5.http.impl.auth.NTLMEngineException;

import jcifs.ntlmssp.NtlmFlags;
import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;

public class JCIFSEngine implements NTLMEngine {
    private static final int TYPE_1_FLAGS = NtlmFlags.NTLMSSP_NEGOTIATE_56 | NtlmFlags.NTLMSSP_NEGOTIATE_128 | NtlmFlags.NTLMSSP_NEGOTIATE_NTLM2 | NtlmFlags.NTLMSSP_NEGOTIATE_ALWAYS_SIGN | NtlmFlags.NTLMSSP_REQUEST_TARGET;

    @Override
    public String generateType1Msg(final String domain, final String workstation) throws NTLMEngineException {
        final Type1Message type1Message = new Type1Message(TYPE_1_FLAGS, domain, workstation);
        return base64EncodeToString(type1Message.toByteArray());
    }

    private static final Encoder BASE64_ENCODER = Base64.getEncoder();

    private static String base64EncodeToString(byte[] data) {
        try {
            return new String(base64Encode(data), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static byte[] base64Encode(byte[] data) {
        return BASE64_ENCODER.encode(data);
    }

    @Override
    public String generateType3Msg(String username, char[] password, String domain, String workstation, String challenge) throws NTLMEngineException {
        Type2Message type2Message;
        try {
            type2Message = new Type2Message(Base64.getDecoder().decode(challenge));
        } catch (final IOException exception) {
            throw new NTLMEngineException("Invalid NTLM type 2 message", exception);
        }
        final int type2Flags = type2Message.getFlags();
        final int type3Flags = type2Flags & (0xffffffff ^ (NtlmFlags.NTLMSSP_TARGET_TYPE_DOMAIN | NtlmFlags.NTLMSSP_TARGET_TYPE_SERVER));
        final Type3Message type3Message = new Type3Message(type2Message, new String(password), domain, username, workstation, type3Flags);
        return base64EncodeToString(type3Message.toByteArray());
    }
}