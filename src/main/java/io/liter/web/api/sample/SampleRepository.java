package io.liter.web.api.sample;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends ReactiveMongoRepository<Sample, ObjectId> {

}
