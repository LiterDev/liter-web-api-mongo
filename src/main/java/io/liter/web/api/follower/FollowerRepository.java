package io.liter.web.api.follower;

import io.liter.web.api.review.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface FollowerRepository extends ReactiveMongoRepository<Follower, String> {

    Flux<Follower> findAllByUserId(String userId);
}
