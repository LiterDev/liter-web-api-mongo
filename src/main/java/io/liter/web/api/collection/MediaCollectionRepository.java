package io.liter.web.api.collection;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaCollectionRepository extends ReactiveMongoRepository<MediaCollection, String> {

}
