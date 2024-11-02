package com.example.filehandler.service;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileHandlerService {
    public void fileUpload() {
        MinioClient minioClient = connectMinioClient();

        try {
            List<Bucket> bucketList = minioClient.listBuckets();
            System.out.println("Success, list of buckets:n" + bucketList.size());
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        String bucketName = "java-demo-bucket";
        String objectName = "image.png";
        String ContentType = "image/png";
        String fileName = "/tmp/demo/image.png";
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .filename(fileName)
                .contentType(ContentType)
                .build();

        ObjectWriteResponse response = minioClient.uploadObject(uploadObjectArgs);

        System.out.println(response.object() + ": " + response.etag() + ", " + response.versionId());

         */
    }

    private MinioClient connectMinioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("https://play.min.io")
                .credentials("Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG")
                .build();

        return minioClient;
    }
}
