package com.example.filehandler.service;

import com.example.filehandler.dto.FileHandlerFileRequest;
import com.example.filehandler.model.FileDetails;
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

    public void uploadFile(String bucketName, FileHandlerFileRequest fileHandlerFileRequest, Long userId) {
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
        }
        catch (Exception e) {
            log.error("Error occurred while uploading file '{}'", fileHandlerFileRequest.file().getOriginalFilename(), e);
        }
        saveFileDetails(objectName, targetServers, userId);
        log.info("File Details saved successfully to database");
    }

    private void saveFileDetails(String filename, List<String> targetServers, Long userID) {
        boolean shard1 = targetServers.contains("shard1-minio");
        boolean shard2 = targetServers.contains("shard2-minio");


        FileDetails fileDetails = new FileDetails();
        fileDetails.setFilename(filename);
        fileDetails.setShardeins(shard1);
        fileDetails.setShardzwei(shard2);
        fileDetails.setUserId(userID);

        fileDetailsRepository.save(fileDetails);
    }

    public void deleteFile(String bucketName, String fileName) {
            String shard;
            if (fileDetailsRepository.findShardEinsByFilename(fileName)) {
                shard = "shard1-minio";
            } else {
                shard = "shard2-minio";
            }
            try {
                MinioClient minioClient = minioClientFactory.getMinioClient(shard);
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
                log.info("File '{}' deleted successfully from bucket '{}', in server '{}", fileName, bucketName, shard);
            } catch (Exception e) {
                log.error("Error occurred while deleting file '{}'", fileName, e);
            }
        log.info("Deleting file details from database");
        long userId = fileDetailsRepository.findUserIdByFilename(fileName);
        fileDetailsRepository.deleteByUserId(userId);
    }

    public byte[] downloadFile(String bucketName, String fileName) throws Exception {
        String shard = null;
        int maxRetries = 10;
        int retryCount = 0;

        while (fileName == null && retryCount < maxRetries) {
            log.info("File name is null, retrying to download file");
            Thread.sleep(1000);
            retryCount++;
        }

        if (fileDetailsRepository.findShardEinsByFilename(fileName)) {
            shard = "shard1-minio";
        } else {
            shard = "shard2-minio";
        }

        // Minio Client Setup und Dateiabruf
        MinioClient minioClient = minioClientFactory.getMinioClient(shard);
        InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());

        // Datei in ByteArray umwandeln
        byte[] fileContent = stream.readAllBytes();
        stream.close();

        // Rückgabe des Byte-Arrays und nicht der ResponseEntity
        return fileContent;
    }
}
