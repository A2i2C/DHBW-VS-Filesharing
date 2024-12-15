package com.example.filehandler.service;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MinioClientFactory {
    private final Map<String, MinioClient> minioClientMap = new HashMap<>();
    private final MinioHealthCheckService minioHealthCheckService;

    public MinioClientFactory(MinioHealthCheckService minioHealthCheckService) {
        this.minioHealthCheckService = minioHealthCheckService;
    }

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
        if (minioHealthCheckService.isServerHealthy(server)) {
            log.info("Minio server {} is healthy", server);
            return minioClientMap.get(server);
        } else {
            log.error("Minio server {} is not healthy", server);
            return null;
        }
    }
}