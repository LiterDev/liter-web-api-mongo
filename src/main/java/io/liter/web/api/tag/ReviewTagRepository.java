package io.liter.web.api.tag;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewTagRepository extends ReactiveMongoRepository<ReviewTag, ObjectId> {

}
