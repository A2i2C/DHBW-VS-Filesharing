package com.example.filehandler.service;

import com.example.filehandler.dto.FileHandlerFileRequest;
import com.example.filehandler.repository.FileDetailsRepository;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileHandlerService {
    private final MinioClientFactory minioClientFactory;
    private final FileDistributionService fileDistributionService;

    private final FileDetailsRepository fileDetailsRepository;

    public void createBucket(String bucketName)
    {
        List<String> targetServers = fileDistributionService.getAllMinioServers();
        minioClientFactory.initializeMinioClients(targetServers);
        try {
            log.info("Processing bucket creation on {} servers.", targetServers.size());
            for (String server : targetServers) {
                MinioClient minioClient = minioClientFactory.getMinioClient(server);
                boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!isExist) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                }
            }
            log.info("Bucket {} created successfully on all Shards", bucketName);
        } catch (Exception e) {
            log.error("Error occurred while creating Bucket '{}':", bucketName, e);
        }
    }

    public void uploadFile(String bucketName, FileHandlerFileRequest fileHandlerFileRequest) {
        List<String> targetServers = fileDistributionService.getMinioServer();
        List<String> initializedServer = minioClientFactory.initializeMinioClients(targetServers);

        //Get MetaData from File
        String objectName = fileHandlerFileRequest.file().getOriginalFilename();
        String contentType = fileHandlerFileRequest.file().getContentType();
        try {
            for (String server : initializedServer) {
                MinioClient minioClient = minioClientFactory.getMinioClient(server);

                //Upload File
                try (InputStream inputStream = fileHandlerFileRequest.file().getInputStream()) {
                    minioClient.putObject(PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, fileHandlerFileRequest.file().getSize(), -1)
                            .contentType(contentType)
                            .build());
                }
                log.info("File '{}' uploaded successfully to bucket '{}' on server '{}'", objectName, bucketName, server);
            }
      //      saveFileDetails(objectName, targetServers);
        }
        catch (Exception e) {
            log.error("Error occurred while uploading file '{}'", fileHandlerFileRequest.file().getOriginalFilename(), e);
        }
    }

//    private void saveFileDetails(String filename, List<String> targetServers) {
//        //Save File Details to Database
//        boolean minio1 = targetServers.contains("minio1");
//        boolean minio2 = targetServers.contains("minio2");
//        boolean minio3 = targetServers.contains("minio3");
//        log.debug("Saving file details: filename={}, minio1={}, minio2={}, minio3={}, userId={}",
//                filename, minio1, minio2, minio3, "user1");
//
//        fileDetailsRepository.saveFileDetails(filename, minio1, minio2, minio3, 1);
//        log.info("File '{}' details saved successfully to Database", filename);
//    }

//    public void deleteFile(String bucketName, String objectName) {
//        try {
//            MinioClient minioClient = minioClientFactory.createMinioClient();
//            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
//            log.info("File '{}' deleted successfully from bucket '{}'", objectName, bucketName);
//        } catch (Exception e) {
//            log.error("Error occurred while deleting file '{}'", objectName, e);
//        }
//    }
//
//    public void downloadFile(String bucketName, String objectName) {
//        try {
//            MinioClient minioClient = minioClientFactory.createMinioClient();
//            minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
//            log.info("File '{}' downloaded successfully from bucket '{}'", objectName, bucketName);
//        } catch (Exception e) {
//            log.error("Error occurred while downloading file '{}'", objectName, e);
//        }
//    }


}
