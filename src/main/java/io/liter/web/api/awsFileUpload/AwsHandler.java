package io.liter.web.api.awsFileUpload;

import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.review.ReviewContentType;
import io.liter.web.api.review.ReviewRepository;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component

public class AwsHandler {

    private AwsRepository awsRepository;


    public AwsHandler(AwsRepository awsRepository) {
        this.awsRepository = awsRepository;
    }


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private FollowerRepository followerRepository;

    /*@Value("${aws.bucket.name}")
    private String bucketName;

    @Value("${temp.file.path}")
    private String directory;

    @Value("${aws.bucket.region}")
    private String clientRegion;*/


    private String keyName = "*** Object key ***";
    private String filePath = "*** Path for file to upload ***";

    public Mono<ServerResponse> getAll(ServerRequest request) {

        Flux<Aws> awsFlux = awsRepository.findAll();

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(awsFlux, Aws.class);
    }


    public Mono<ServerResponse> s3Upload(ServerRequest request) {

        Mono<List<FilePart>> imageMono = request.body(BodyExtractors.toParts()).collectList()
                .map(m -> m.stream()
                        .filter(p -> (ReviewContentType.checkCode(p.headers().getContentType()).equals(ReviewContentType.IMAGE.getCode())))
                        .map(p -> ((FilePart) p))
                        .collect(Collectors.toList())
                );

        return ServerResponse.ok().build();

        /*

        return imageMono
                .map(m -> {
                            m.stream()
                                    .forEach(filePart -> {

                                        String clientRegion = "*** Client region ***";
                                        String bucketName = "*** Bucket name ***";
                                        String stringObjKeyName = "*** String object key name ***";
                                        String fileObjKeyName = "*** File object key name ***";
                                        String fileName = filePart.filename();

                                        try {
                                            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                                                    .withRegion(clientRegion)
                                                    .withCredentials(new ProfileCredentialsProvider())
                                                    .build();

                                            // Upload a text string as a new object.
                                            s3Client.putObject(bucketName, stringObjKeyName, "Uploaded String Object");

                                            // Upload a file as a new object with ContentType and title specified.
                                            PutObjectRequest req = new PutObjectRequest(bucketName, fileObjKeyName, new File(fileName));
                                            ObjectMetadata metadata = new ObjectMetadata();
                                            metadata.setContentType("plain/text");
                                            metadata.addUserMetadata("x-amz-meta-title", "someTitle");
                                            req.setMetadata(metadata);
                                            s3Client.putObject(req);
                                        } catch (AmazonServiceException e) {
                                            // The call was transmitted successfully, but Amazon S3 couldn't process
                                            // it, so it returned an error response.
                                            e.printStackTrace();
                                        } catch (SdkClientException e) {
                                            // Amazon S3 couldn't be contacted for a response, or the client
                                            // couldn't parse the response from Amazon S3.
                                            throw new SdkClientException(e);
                                        }

                            m.stream().forEach(filePart1 -> {
                                if (fileName.equals(m.stream())) {
                                }else{

                                }
                            })
                                    });


                            return m;
                        }
                )
                .flatMap(r -> ServerResponse.ok().build())
                .switchIfEmpty(notFound().build());

            */

    }
}


/* public Mono<ServerResponse> s3Upload2(ServerRequest request){

        S3Util s3 = new S3Util();

        List<bucket> list = s3.getBucketList();

        // 첫번째 Bucket
        String bucketName = list.get(0).getName().toString();
        System.out.println("Bucket Name : " + bucketName);

        // Bucket 생성(대문자는 포함되면 안됨.)
        for(int i = 0; i < 3; i++) {
            s3.createBucket("wonier-test-bucket" + i);
        }

        // 폴더 생성
        for(int i = 0; i < 3; i++) {
            s3.createFolder(bucketName, "woniper-test-folder" + i);
        }

        // 파일 업로드
        String fileName = "/Users/woniper/Downloads/aws-java-sdk-1.8.4.zip";
        try {
            s3.fileUpload(bucketName, new File(fileName));}
        catch (FileNotFoundException e){
          throw new FileNotFoundException("a");
        }



    }*/


/*

    public Mono<ServerResponse> s3Upload3(ServerRequest request) {

        ArrayList<File> files = new ArrayList<File>();


        return request.body(BodyExtractors.toParts()).collectList()
                .map(m -> m.stream()
                        .filter(p -> (ReviewContentType.checkCode(p.headers().getContentType()).equals(ReviewContentType.IMAGE.getCode())))
                        .map(p -> ((FilePart) p))
                        .forEach(p -> {
                            files.add(new File(p.filename()));
                        })


                       .collect(Collectors.toList())



                );
*/
/* return imageMono
                .flatMap(m -> {
                    return m.stream()
                            .forEach(filePart -> {*//*

 */
/*

                                {
                                    // convert the file paths to a list of File objects (required by the
                                    // uploadFileList method)
                                    ArrayList<File> files = new ArrayList<File>();
                                    for (String path : file_paths) {
                                        files.add(new File(path));
                                    }

                                    TransferManager xfer_mgr = TransferManagerBuilder.standard().build();
                                    try {
                                        MultipleFileUpload xfer = xfer_mgr.uploadFileList(bucket_name,
                                                key_prefix, new File("."), files);
                                        // loop with Transfer.isDone()
                                        XferMgrProgress.showTransferProgress(xfer);
                                        // or block with Transfer.waitForCompletion()
                                        XferMgrProgress.waitForCompletion(xfer);
                                    } catch (AmazonServiceException e) {

                                        throw new AmazonServiceException("e");
                                    }
                                    xfer_mgr.shutdownNow();
                                }

                                return ServerResponse.ok().body(imageMono);
                            });
                });
*//*

 */
/*
        image.flatMap(f -> {

            // convert the file paths to a list of File objects (required by the
            // uploadFileList method)
            ArrayList<File> files = new ArrayList<File>();
            for (String path : file_paths) {
                files.add(new File(path));
            }

            TransferManager xfer_mgr = TransferManagerBuilder.standard().build();
            try {
                MultipleFileUpload xfer = xfer_mgr.uploadFileList(bucketName, keyName, new File("."), files);
                // loop with Transfer.isDone()
                XferMgrProgress.showTransferProgress(xfer);
                // or block with Transfer.waitForCompletion()
                XferMgrProgress.waitForCompletion(xfer);
            } catch (AmazonServiceException e) {

                throw new AmazonServiceException("e");
            }
            xfer_mgr.shutdownNow();

            return ServerResponse.ok().body((BodyInserter<?, ? super ServerHttpResponse>) imageMono);


        });


        return null;
    }

    public boolean aws(ArrayList<File> files){


        ArrayList<File> files = new ArrayList<File>();
        for (String path : file_paths) {
            files.add(new File(path));
        }

        TransferManager xfer_mgr = TransferManagerBuilder.standard().build();
        try {
            MultipleFileUpload xfer = xfer_mgr.uploadFileList(bucket_name, key_prefix, new File("."), files);
            // loop with Transfer.isDone()
            XferMgrProgress.showTransferProgress(xfer);
            // or block with Transfer.waitForCompletion()
            XferMgrProgress.waitForCompletion(xfer);
        } catch (AmazonServiceException e) {

            return false;
            throw new AmazonServiceException("e");
        }
        xfer_mgr.shutdownNow();

        return true;
    }*/







