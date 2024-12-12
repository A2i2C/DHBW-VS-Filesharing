package filesharing.userhandler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class FileHandlerService {

    private final RestClient restClient;

    public FileHandlerService(RestClient.Builder restClientBuilder) {
        try {
            this.restClient = restClientBuilder.baseUrl("http://localhost").build();
        } catch (Exception e) {
            log.error("Error occurred while creating rest client", e);
            throw e;
        }
    }

    public void createBucket(String bucketName) {
        try {
            this.restClient.post().uri("/api/file/createBucket").body(bucketName);
        } catch (Exception e) {
            log.error("Error occurred while creating bucket", e);
            throw e;
        }
    }

}
