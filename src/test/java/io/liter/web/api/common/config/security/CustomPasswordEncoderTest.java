package io.liter.web.api.common.config.security;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.Base64;

public class CustomPasswordEncoderTest {
    private static Logger log = LoggerFactory.getLogger(CustomPasswordEncoderTest.class);

    @Test
    public void encode() {
        CharSequence rawPassword = "test";
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);

        String hashed = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(12, random));
        log.debug("]-----] sample [-----[ {}", hashed);
        //String decodedString  = new String(Base64.getDecoder().decode(rawPassword.toString()));
        //log.debug("]-----] decodedString [-----[ {}", decodedString);
        log.debug("]-----] sample [-----[ {}", BCrypt.checkpw(String.valueOf(rawPassword), hashed));
        //$2a$16$2CbpPT.52w6P.6ik2CMIhuwV8WS7PapIokJsmheTR5wzdAgjd0CVS
        //$2a$10$wPNSP1EcP8MK7rL5bHPDuugFQ5O8UDx3KHj7dJK3ci4ZZdiUXl9mK
        //$2a$12$/GjBDmhjbuSd1t.uwXfq1ed0jrUSpSJ2j79R.J6IMCrmtMv1W9TX2
        //$2a$12$/x9/U.b8ah/G8hHqqWnmqu3avLqBYXABsgMB/Z1GtegkHh2YpbtVO
        //$2a$12$F32NKR33LT/xK7Z4e/9LqOsNK5cf6UnkUnEnkJytpmik01nIFdFVS
        //$2a$12$8ALYXcbI90kLlgSBRF1Hw.y.mvDy2/PRCRGnjkz/sh7JklVkKkZQm
    }

    @Test
    public void matches() {
        CharSequence rawPassword = "jdev";
        String encodedPassword = "$2a$12$F32NKR33LT/xK7Z4e/9LqOsNK5cf6UnkUnEnkJytpmik01nIFdFVS";
        String decodedString  = new String(Base64.getDecoder().decode(rawPassword.toString()));
        BCrypt.checkpw(decodedString, encodedPassword);
        log.debug("]-----] sample [-----[ {}", BCrypt.checkpw(decodedString, encodedPassword));
    }
}