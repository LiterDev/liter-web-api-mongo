package io.liter.web.api.awsFileUpload;/*

package io.liter.web.api.sampleJ;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class S3Util {

    private String accessKey = "put your access Key";
    private String secretKey = "put your secret Key";

    private AmazonS3 conn;

    public S3Util() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        this.conn = new AmazonS3Client(credentials, clientConfig);
        conn.setEndpoint("s3-ap-northeast-1.amazonaws.com");
    }



    // Bucket List
    public List<bucket> getBucketList() {
        return conn.listBuckets();
    }

    // Bucket 생성
    public Bucket createBucket(String bucketName) {
        return conn.createBucket(bucketName);
    }

    // 폴더 생성 (폴더는 파일명 뒤에 "/"를 붙여야한다.)
    public void createFolder(String bucketName, String folderName) {
        conn.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
    }

    public void fileUpload(String bucketName, File file) throws FileNotFoundException {
        conn.putObject(bucketName, file.getName(), new FileInputStream(file), new ObjectMetadata());
    }


}

*/
