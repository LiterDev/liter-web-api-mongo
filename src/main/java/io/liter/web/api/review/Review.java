package io.liter.web.api.review;

import io.liter.web.api.common.model.BaseEntity;
import io.liter.web.api.follower.Follower;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Review extends BaseEntity {

    @Id
    private String id;

    private String userId;

    private String collectionId;

    private String title;

    private String content;

    private long reviewCount;

    private long likeCount;

    private long linkCount;

    private long replyCount;

    private BigDecimal rewardLitercube;

    private Integer rewardActive; //리뷰보상여부

    @DBRef
    private Set<Follower> follower;
}
