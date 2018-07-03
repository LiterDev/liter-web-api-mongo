package io.liter.web.api.review;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface ReviewRepository extends ReactiveMongoRepository<Review, ObjectId> {

    Flux<Review> findByUserIdIn(Collection<ObjectId> userId, Pageable pageable);

    Mono<Long> countByUserIdIn(Collection<ObjectId> userId);

    Flux<Review> findByUserId(ObjectId userId, Pageable pageable);
    Mono<Long> countByUserId(ObjectId userId);

}
