package io.liter.web.api.search;

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
public class SearchHistory extends BaseEntity {

    @Id
    private Long id;

    private String userId;

    private String type;

    private String searchValue;

}
