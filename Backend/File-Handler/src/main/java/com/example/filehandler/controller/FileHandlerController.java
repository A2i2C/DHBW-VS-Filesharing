package com.example.filehandler.controller;

import com.example.filehandler.dto.FileHandlerFileRequest;
import com.example.filehandler.service.FileHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileHandlerController {

    private final FileHandlerService fileHandlerService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam String bucketName, @RequestPart("file") MultipartFile file, @RequestParam long userID) {
        try {
            FileHandlerFileRequest uploadFileRequest = new FileHandlerFileRequest(file);
            fileHandlerService.uploadFile(bucketName, uploadFileRequest, userID);
            return ResponseEntity.ok(Map.of("message", "File Uploaded successfully", "fileName", file.getName()));
        } catch (Exception e) {
        log.error("Error occurred while uploading file", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "File upload failed", "error", e.getMessage()));
    }
}

    @PostMapping("/createBucket")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Map<String, String>> createBucket(@RequestParam String bucketName) {
        try {
            fileHandlerService.createBucket(bucketName);
            return ResponseEntity.ok(Map.of("message", "Bucket created successfully", "bucketName", bucketName));
        } catch (Exception e) {
            log.error("Error occurred while creating bucket '{}': {}", bucketName, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to create bucket", "error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Map<String, String>> deleteFile(@RequestParam String bucketName, @RequestParam String filename) {
        try {
            fileHandlerService.deleteFile(bucketName, filename);
            return ResponseEntity.ok(Map.of("message", "File deleted successfully", "fileName", filename));
        } catch (Exception e) {
            log.error("Error occurred while deleting file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to delete file", "error", e.getMessage()));
        }
    }

    @PostMapping("/download")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam String bucketName, @RequestParam String filename) throws Exception {
        byte[] fileContent = fileHandlerService.downloadFile(bucketName, filename);

        // set file name and content type for response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);

        // return file as byte array
        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource(fileContent));
    }

}
