package com.example.filehandler.service;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MinioClientFactory {
    private final Map<String, MinioClient> minioClientMap = new HashMap<>();
    private final OkHttpClient httpClient = new OkHttpClient();

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

    public boolean isServerHealthy(String server) {
        try {
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("http://" + server + ":9000/minio/health/ready")
                    .build();
            okhttp3.Response response = httpClient.newCall(request).execute();
            return response.isSuccessful();
        } catch (Exception e) {
            log.error("Error occurred while checking health of Minio server '{}':", server, e);
            return false;
        }
    }
}