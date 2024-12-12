package com.example.filehandler.service;

import io.minio.MinioClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MinioClientFactory {
    private final Map<String, MinioClient> minioClientMap = new HashMap<>();

    public List<String> initializeMinioClients(List<String> targetServers) {
        targetServers.forEach(server -> {
            if (!minioClientMap.containsKey(server)) {
                minioClientMap.put(server, createDistributedMinioClient("http://" + server + ":9000"));
            }
        });
        return targetServers;
    }

    private MinioClient createDistributedMinioClient(String endpoint) {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials("minioadmin", "minioadmin")
                .build();
    }

    public MinioClient getMinioClient(String server) {
        return minioClientMap.get(server);
    }

}