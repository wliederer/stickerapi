package com.free.sticker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private final String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
    private final String accessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
    private final String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
    private final String region = System.getenv("AWS_REGION");

    private S3Client s3Client;

    // Initialize S3 client with environment variables
    public ImageService() {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretKey)))
                .build();
    }

    public String uploadImage(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        logger.info("Starting upload for file: {}", fileName);
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName )
                    .key(fileName)
//                    .acl("public-read")
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            logger.info("Successfully uploaded file: {}", fileName);
            // Construct the file's URL to return
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);

        } catch (S3Exception e) {
            logger.error("Error uploading image to S3: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Error uploading image to S3: " + e.getMessage());
        } catch ( IOException e){
            logger.error("IO Error uploading image to S3: {}", e.getMessage());
            throw new RuntimeException("Error uploading image to S3: " + e.getMessage());
        }
    }
}


