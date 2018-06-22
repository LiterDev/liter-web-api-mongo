package io.liter.web.api.review;

import org.springframework.http.MediaType;

public enum ReviewContentType {

    IMAGE (0, "0101"),
    CONTENT (1, "0102"),
    MOVIE (2, "0103");

    private int key;

    private String code;

    ReviewContentType(int key, String code) {
        this.key = key;
        this.code = code;
    }

    public int getKey() {
        return this.key;
    }

    public String getCode() {
        return this.code;
    }

    public static ReviewContentType getItemByCode(String code) {
        ReviewContentType[] items = ReviewContentType.values();
        for (ReviewContentType item : items) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String checkCode(MediaType mediaType) {
        if (mediaType == null) {
            return ReviewContentType.CONTENT.getCode();
        } else if (mediaType.equals(MediaType.IMAGE_GIF) || mediaType.equals(MediaType.IMAGE_JPEG) || mediaType.equals(MediaType.IMAGE_PNG)) {
            return ReviewContentType.IMAGE.getCode();
        } else {
            return ReviewContentType.MOVIE.getCode();
        }
    }
}
