package io.liter.web.api.ssong;

import io.liter.web.api.tag.ReviewTag;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SsongRepository extends ReactiveMongoRepository<Ssong, ObjectId> {

}
