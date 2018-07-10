package io.liter.web.api.review;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Repository
public interface ReviewRepository extends ReactiveMongoRepository<Review, ObjectId> {

    Flux<Review> findByUserIdInOrderByCreatedAtDesc(Collection<ObjectId> userId, Pageable pageable);

    Mono<Long> countByUserIdIn(Collection<ObjectId> userId);
    
}
