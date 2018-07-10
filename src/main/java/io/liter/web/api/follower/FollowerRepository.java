package io.liter.web.api.follower;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FollowerRepository extends ReactiveMongoRepository<Follower, ObjectId> {

    Mono<Follower> findByUserId(ObjectId userId);

    Mono<Follower> findByUserIdInAndFollowerIdIn(ObjectId userId, ObjectId followerId);

}
