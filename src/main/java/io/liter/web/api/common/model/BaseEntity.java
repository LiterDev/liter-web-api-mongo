package io.liter.web.api.common.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class BaseEntity implements Serializable {

    private int active;

    private Long createdAt = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();

    private Long updateAt = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
}
