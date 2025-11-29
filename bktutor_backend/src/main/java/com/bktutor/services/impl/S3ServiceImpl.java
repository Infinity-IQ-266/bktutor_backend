package com.bktutor.services.impl;

import com.bktutor.common.dtos.S3ObjectResponse;
import com.bktutor.services.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    private final String bucketName;

    public S3ServiceImpl(S3Client s3Client, @Value("${aws.bucket.name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String folder = "Bktutor/";
        String key = folder + fileName;

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes()));

        return key;
    }

    @Override
    public S3ObjectResponse downloadFile(String key) {
        ResponseBytes<GetObjectResponse> objectAsBytes =
                s3Client.getObjectAsBytes(
                        GetObjectRequest.builder()
                                .bucket(bucketName)
                                .key(key)
                                .build()
                );

        return new S3ObjectResponse(
                objectAsBytes.asByteArray(),
                objectAsBytes.response().contentType()
        );
    }

}
