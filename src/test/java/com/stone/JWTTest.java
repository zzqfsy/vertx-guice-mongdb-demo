package com.zzqfsy;

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;
import org.junit.Test;

import java.security.Key;

/**
 * Created by john on 16-7-6.
 */
public class JWTTest {
    static {
        String payload = "";
    }

    @Test
    public void test() {
        try {
            Key key = new AesKey(ByteUtil.randomBytes(16));
            JsonWebEncryption jwe = new JsonWebEncryption();
            jwe.setPayload("Hello World!");
            jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
            jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
            jwe.setKey(key);
            String serializedJwe = jwe.getCompactSerialization();
            System.out.println("Serialized Encrypted JWE: " + serializedJwe);

            JsonWebEncryption jwe2 = new JsonWebEncryption();
            jwe2.setKey(key);
            jwe2.setCompactSerialization(serializedJwe);
            System.out.println("Payload: " + jwe2.getPayload());
        } catch (JoseException e) {
            e.printStackTrace();
        }
    }
}
