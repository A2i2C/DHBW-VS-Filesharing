package com.example.filehandler.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

@Slf4j
@Service
public class FileDistributionService {

    private final List<String> minioServers;
    private final ValueOperations<String, String> valueOperations;
    private final MinioClientFactory minioClientFactory;

    // Redis key for the counter
    private static final String COUNTER_KEY = "file_distribution_counter";

    @Autowired
    public FileDistributionService(ValueOperations<String, String> valueOperations, MinioClientFactory minioClientFactory) {
        this.minioClientFactory = minioClientFactory;
        this.minioServers = List.of("shard1-minio", "shard2-minio");
        this.valueOperations = valueOperations;
    }

    public List<String> getMinioServer() {
        //Check if any healthy server is available
        List<String> healthyServers = getHealthyServers();
        if (healthyServers.isEmpty()) {
            log.error("No healthy Minio servers available!");
            throw new IllegalStateException("No healthy Minio servers available!");
        }
        // If only one healthy server is available, return it
        if (healthyServers.size() == 1) {
            log.info("Only one healthy server found: {}", healthyServers.getFirst());
            return List.of(healthyServers.getFirst());
        }

        // If multiple healthy servers are available, distribute the files via round-robin
        // Increment the counter in Redis (it will be thread-safe)
        String currentCounter = valueOperations.get(COUNTER_KEY);
        int counterValue = currentCounter != null ? Integer.parseInt(currentCounter) : 0;
        int index = counterValue % minioServers.size();

        // Update the Redis counter
        valueOperations.set(COUNTER_KEY, String.valueOf(counterValue + 1));

        log.info("File will be distributed to Minio Server: {}", minioServers.get(index));
        return List.of(minioServers.get(index));
    }

    public List<String> getAllMinioServers() {
        List<String> healthyServers = getHealthyServers();
        if (healthyServers.isEmpty()) {
            log.error("No healthy Minio servers available!");
            throw new IllegalStateException("No healthy Minio servers available!");
        }
        return minioServers;
    }

    public List<String> getHealthyServers() {
        return minioServers.stream()
                .filter(minioClientFactory::isServerHealthy)
                .toList();
    }
}


