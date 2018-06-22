package io.liter.web.api.review.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination implements Serializable {

    private Integer page;
    private Integer size;
    private Long total;
}
