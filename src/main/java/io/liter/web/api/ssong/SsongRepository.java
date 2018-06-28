package io.liter.web.api.tag;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewTagRepository extends ReactiveMongoRepository<ReviewTag, String> {

}
