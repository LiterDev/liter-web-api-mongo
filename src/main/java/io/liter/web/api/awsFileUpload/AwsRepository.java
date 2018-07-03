package io.liter.web.api.awsFileUpload;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwsRepository extends ReactiveMongoRepository<Aws,String> {



}
