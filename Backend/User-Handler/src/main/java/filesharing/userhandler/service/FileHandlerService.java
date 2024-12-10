package filesharing.userhandler.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class FileHandlerService {

    private final RestClient restClient;

    public FileHandlerService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("http://localhost").build();
    }

    public void createBucket(String bucketName) {
        System.out.println("Creating bucket: " + bucketName);
        this.restClient.post().uri("/api/file/createBucket").body(bucketName);
    }

}
