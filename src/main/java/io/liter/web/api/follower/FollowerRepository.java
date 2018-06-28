package io.liter.web.api.follower;

import io.liter.web.api.review.Review;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface FollowerRepository extends ReactiveMongoRepository<Follower, ObjectId> {

    Flux<Follower> findAllByUserId(ObjectId userId);
}
