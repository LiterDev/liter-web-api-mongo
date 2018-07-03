package io.liter.web.api.follower;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FollowerRepository extends ReactiveMongoRepository<Follower, ObjectId> {

    Mono<Follower> findByUserId(ObjectId userId);
}
