package io.liter.web.api.awsFileUpload;/*

package io.liter.web.api.awsFileUpload;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3EncryptionClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${jsa.aws.access_key_id}")
    private String awsId;

    @Value("${jsa.aws.secret_access_key}")
    private String awsKey;

    @Value("${jsa.s3.region}")
    private String region;

    @Bean
    public BasicAWSCredentials credentials() {
        return new BasicAWSCredentials(awsId, awsKey);
    }

    @Bean
    public AmazonS3 s3client() {

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
        AmazonS3 s3Client = AmazonS3EncryptionClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        return s3Client;
    }

    @Bean
    public TransferManager transferManager() {

        TransferManager transferManager = new TransferManager(credentials());
        AmazonS3Client s3Client = new AmazonS3Client(credentials());
        s3Client.setEndpoint("s3-ap-northeast-2.amazonaws.com");

        return transferManager;
    }

}



*/
