package com.example.filehandler.service;

import com.example.filehandler.dto.FileHandlerFileRequest;
import com.example.filehandler.model.FileDetails;
import com.example.filehandler.repository.FileDetailsRepository;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        List<String> targetServers = fileDistributionService.getMinioServer(); //Get Minio Server
        //Initialize Minio Server with http://server:9000
        List<String> initializedServer = minioClientFactory.initializeMinioClients(targetServers);

        //Get MetaData from File
        String objectName = fileHandlerFileRequest.file().getOriginalFilename();
        String contentType = fileHandlerFileRequest.file().getContentType();
        String filename  = fileDetailsRepository.findFilenameByFilenameAndBucketname(bucketName, objectName);

        if (Objects.equals(filename, objectName)) {
            log.info("File '{}' already exists in bucket '{}'", objectName, bucketName);
            throw new IllegalStateException("File already exists in bucket");
        }
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
            saveFileDetails(objectName, targetServers, userId, bucketName);
            log.info("File Details saved successfully to database");
        }
        catch (Exception e) {
            log.error("Error occurred while uploading file '{}'", fileHandlerFileRequest.file().getOriginalFilename(), e);
        }
    }

    private void saveFileDetails(String filename, List<String> targetServers, Long userID, String bucketName) {
        boolean shard1 = targetServers.contains("shard1-minio");
        boolean shard2 = targetServers.contains("shard2-minio");
        LocalDateTime now = LocalDateTime.now();
        int yearweek = Integer.parseInt(now.format(DateTimeFormatter.ofPattern("yyyyww")));

        FileDetails fileDetails = new FileDetails();
        fileDetails.setBucketname(bucketName);
        fileDetails.setFilename(filename);
        fileDetails.setShardeins(shard1);
        fileDetails.setShardzwei(shard2);
        fileDetails.setYearweek(yearweek);
        fileDetails.setUserId(userID);
        fileDetailsRepository.save(fileDetails);

    }

    public void deleteFile(String bucketName, String fileName) {
        String shard;
        shard = fileDetailsRepository.findShardEinsByFilename(fileName) ? "shard1-minio" : "shard2-minio";
            try {
                MinioClient minioClient = minioClientFactory.getMinioClient(shard);
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
                log.info("File '{}' deleted successfully from bucket '{}', in server '{}", fileName, bucketName, shard);
                long fileId = fileDetailsRepository.findFileIdByFilename(fileName, bucketName);
                fileDetailsRepository.deleteByFileId(fileId);
            } catch (Exception e) {
                log.error("Error occurred while deleting file '{}'", fileName, e);
            }
        log.info("File '{}' deleted successfully from database", fileName);
    }

    public List<String> getAllFilesFromBucket(String bucketName) {
        List<String> fileNames = new ArrayList<>();
        List<String> targetServers = fileDistributionService.getAllMinioServers();
        List<String> initializedServer = minioClientFactory.initializeMinioClients(targetServers);

        for (String server : initializedServer) {
            try {
                MinioClient minioClient = minioClientFactory.getMinioClient(server);
                log.info("Listing files in bucket '{}' on server '{}'", bucketName, server);
                Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
                for (Result<Item> result : results) {
                    Item item = result.get();
                    log.info("File '{}' found in bucket '{}', in server '{}", item.objectName(), bucketName, server);
                    fileNames.add(item.objectName());
                }
            } catch (Exception e) {
                log.error("Error occurred while listing files in bucket '{}'", bucketName, e);
            }
        }
        return fileNames;
    }

    public byte[] downloadFile(String bucketName, String fileName) throws Exception {
        String shard;
        shard = fileDetailsRepository.findShardEinsByFilename(fileName) ? "shard1-minio" : "shard2-minio";

        MinioClient minioClient = minioClientFactory.getMinioClient(shard);
        InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());

        byte[] fileContent = stream.readAllBytes();
        stream.close();

        log.info("File '{}' downloaded successfully from bucket '{}', in server '{}", fileName, bucketName, shard);
        return fileContent;
    }
}
