package com.example.filehandler.service;

import com.example.filehandler.dto.FileHandlerFileRequest;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileHandlerService {
    private final MinioClientFactory minioClientFactory;

    public void createBuckets(String bucketName)
    {
        try {
            MinioClient minioClient = minioClientFactory.createMinioClient();
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            log.info("Bucket {} created successfully", bucketName);
        } catch (Exception e) {
            log.error("Error occurred while creating Bucket '{}':", bucketName, e);
        }
    }

    public void uploadFile(String bucketName, FileHandlerFileRequest fileHandlerFileRequest) {
        try {
            MinioClient minioClient = minioClientFactory.createMinioClient();

            //Get MetaData from File
            String objectName = fileHandlerFileRequest.file().getOriginalFilename();
            String contentType = fileHandlerFileRequest.file().getContentType();

            //Upload File
            try (InputStream inputStream = fileHandlerFileRequest.file().getInputStream()) {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, fileHandlerFileRequest.file().getSize(), -1)
                        .contentType(contentType)
                        .build());
            }
            log.info("File '{}' uploaded successfully to bucket '{}'", objectName, bucketName);
        }
        catch (Exception e) {
            log.error("Error occurred while uploading file '{}'", fileHandlerFileRequest.file().getOriginalFilename(), e);
        }
    }


}
