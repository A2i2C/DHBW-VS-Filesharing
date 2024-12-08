package com.example.filehandler.service;

import io.minio.MinioClient;
import org.springframework.stereotype.Component;

@Component
public class MinioClientFactory {
    public MinioClient createMinioClient() {
        return MinioClient.builder()
                .endpoint("http://minio1:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
    }
}