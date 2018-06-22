package io.liter.web.api.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class UUIDGenerator {

    public String getUUID() {
        String uuid = RandomStringUtils.randomAlphabetic(16);
        return uuid;
    }
}
