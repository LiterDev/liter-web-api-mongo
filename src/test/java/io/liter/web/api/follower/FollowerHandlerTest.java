package io.liter.web.api.follower;

import com.mongodb.client.result.UpdateResult;
import io.liter.web.api.sample.SampleHandlerTest;
import org.bson.BsonValue;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class FollowerHandlerTest {

    private static Logger log = LoggerFactory.getLogger(FollowerHandlerTest.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Test
    public void testAddToSet() {

        Query query = new Query(Criteria.where("_id").is(new ObjectId("5b3d8efec650c290079b3a8a")));

        Update update = new Update().addToSet("followerId"
                , new ObjectId("5b3cb9f6c650c224af186194"));

        Follower followerMono = mongoTemplate.findAndModify(query, update, Follower.class);

        log.debug("]-----] findAndModify [-----[ {}", followerMono);

    }
}