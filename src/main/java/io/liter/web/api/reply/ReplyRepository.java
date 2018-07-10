package io.liter.web.api.reply;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends ReactiveMongoRepository<Reply, ObjectId> {

}
