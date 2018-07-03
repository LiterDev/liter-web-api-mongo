package io.liter.web.api.awsFileUpload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Aws {

    private String id;

    private String title;

    private BigDecimal assets;

    private Long createdAt = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
}


