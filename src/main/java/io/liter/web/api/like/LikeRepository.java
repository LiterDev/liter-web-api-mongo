package io.liter.web.api.like;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LikeRepository extends ReactiveMongoRepository<Like, ObjectId> {

    Mono<Long> countByReviewIdAndLikeId (ObjectId reviewId, ObjectId likeId);
}
