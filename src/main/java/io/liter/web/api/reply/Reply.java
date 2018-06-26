package io.liter.web.api.reply;

import io.liter.web.api.common.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Reply extends BaseEntity {

    @Id
    private String id;

    private String reviewId;

    private String content;

    private int parent;

    private int depth;

    private int seq;

}
