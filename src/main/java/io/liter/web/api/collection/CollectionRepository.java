package io.liter.web.api.collection;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CollectionRepository extends ReactiveMongoRepository<MediaCollection, String> {

}
