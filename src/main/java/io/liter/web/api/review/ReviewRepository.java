package io.liter.web.api.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {

    Flux<Review> findByUserId(String userId, Pageable pageable);

    Mono<Long> countByUserId(String userId);

}
