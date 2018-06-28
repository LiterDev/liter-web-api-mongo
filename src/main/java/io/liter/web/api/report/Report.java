package io.liter.web.api.report;

import io.liter.web.api.common.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Report extends BaseEntity {

    @Id
    private ObjectId id;

    private ObjectId userId;

    private ObjectId reviewId;

    private String reportType;

}
