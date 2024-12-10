package com.example.filehandler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class FileDistributionService {

    private final List<String> minioServers;
    private final AtomicInteger counter = new AtomicInteger(0); // Atomic Integer was used to increment the counter in a thread-safe manner

    public FileDistributionService() {
        this.minioServers = List.of("shard1-minio", "shard2-minio"); // List of Minio Servers, can be extended to any number of servers
    }

    public List<String> getMinioServer() {
        int serverCount = minioServers.size();

        //Calculate the two servers to which the file will be distributed
        int index = counter.getAndIncrement() % serverCount;
        log.info("File will be distributed to Minio Server: {}", minioServers.get(index));

        return List.of(minioServers.get(index));
    }

    public List<String> getAllMinioServers() {
        return minioServers;
    }
}


