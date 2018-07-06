package io.liter.web.api.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, ObjectId> {

    Mono<User> findByUsername(String username);

    Mono<Long> countByIdIn(Collection<ObjectId> userId);

}
