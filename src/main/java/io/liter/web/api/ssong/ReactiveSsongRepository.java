package io.liter.web.api.ssong;

import io.liter.web.api.review.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReactiveSsongRepository extends ReactiveMongoRepository<Ssong,String>{

}
