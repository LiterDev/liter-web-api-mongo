package io.liter.web.api.review;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReviewIdGenerator {

    public String getReviewId() {
        String reviewId = RandomStringUtils.randomAlphabetic(6) + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return reviewId;
    }

}
