package com.example.filehandler.controller;

import com.example.filehandler.dto.FileHandlerFileRequest;
import com.example.filehandler.service.FileHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam String bucketName, @RequestPart("file") MultipartFile file, @RequestParam long userID) throws Exception {
        FileHandlerFileRequest uploadFileRequest = new FileHandlerFileRequest(file);
        fileHandlerService.uploadFile(bucketName, uploadFileRequest, userID);
        return ResponseEntity.ok(Map.of("message", "File Uploaded successfully", "fileName", file.getName()));
    }

    @PostMapping("/createBucket")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createBucket(@RequestParam String bucketName) throws Exception {
        fileHandlerService.createBucket(bucketName);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteFile(@RequestParam String bucketName, @RequestParam String filename) throws Exception {
        fileHandlerService.deleteFile(bucketName, filename);
    }

    @PostMapping("/download")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void downloadFile(@RequestParam String bucketName, @RequestParam String filename) throws Exception {
        fileHandlerService.downloadFile(bucketName, filename);
    }

}
