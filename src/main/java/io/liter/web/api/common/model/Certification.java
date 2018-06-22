package io.liter.web.api.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Certification extends BaseEntity{

    @Id
    private Long id;

    private String userId;

    private String type;

    private String content;

    private String requestKey;

    private String responseKey;

    private Long requestAt;

    private Long responseAt;

    private Long CertificationYn;

}
