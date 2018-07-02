package io.liter.web.api.ssong;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SsongRepository extends ReactiveMongoRepository<Ssong, ObjectId> {

}
