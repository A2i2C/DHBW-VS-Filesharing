package com.example.filehandler.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MinioHealthCheckService {

    private final OkHttpClient httpClient = new OkHttpClient();

    public boolean isServerHealthy(String server) {
        String healthEndpoint = "http://" + server + ":9000/minio/health/live";
        Request request = new Request.Builder().url(healthEndpoint).build();

        try (Response response = httpClient.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }
}
