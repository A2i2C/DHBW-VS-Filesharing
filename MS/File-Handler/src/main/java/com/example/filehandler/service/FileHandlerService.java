package com.example.filehandler.service;

import com.example.filehandler.dto.FileHandlerFileRequest;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class FileHandlerService {
    public void fileUpload(FileHandlerFileRequest uploadFileRequest) throws IOException, MinioException, NoSuchAlgorithmException, InvalidKeyException {
        MinioClient minioClient = connectMinioClient();

        String bucketName = "java-demo-bucket";
        String objectName = uploadFileRequest.file().getOriginalFilename();
        String contentType = uploadFileRequest.file().getContentType();

        // Check if the bucket exists
        boolean isBucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        // Create the bucket if it doesn't exist
        if (!isBucketExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            System.out.println("Bucket created: " + bucketName);
        }

        // Upload the file using InputStream
        try (InputStream inputStream = uploadFileRequest.file().getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, uploadFileRequest.file().getSize(), -1)
                            .contentType(contentType)
                            .build()
            );
            System.out.println("File uploaded successfully: " + objectName);
        }
    }

    private MinioClient connectMinioClient() {
        return MinioClient.builder()
                .endpoint("http://minio1:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
    }
}
