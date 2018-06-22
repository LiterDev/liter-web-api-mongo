package io.liter.web.api.ssong;

import io.liter.web.api.review.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class Lookup {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void lookupOperation(){
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("Review")
                .localField("followerId")
                .foreignField("userId")
                .as("followers");

        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("userId").is("1")) , lookupOperation);


        List<Review> results = mongoTemplate.aggregate(aggregation, "Follower", Review.class).getMappedResults();
    }


    /*

    @Autowired
    private MongoTemplate mongoTemplate;


    public void lookupOperation(){
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("Department")
                .localField("dept_id")
                .foreignField("_id")
                .as("departments");

        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("_id").is("1")) , lookupOperation);
        List<EmpDeptResult> results = mongoTemplate.aggregate(aggregation, "Employee", EmpDeptResult.class).getMappedResults();
    }
    */
}
