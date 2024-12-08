package com.example.filehandler.controller;

import com.example.filehandler.dto.FileHandlerFileRequest;
import com.example.filehandler.service.FileHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileHandlerController {

    private final FileHandlerService fileHandlerService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadFile(@RequestPart("file") MultipartFile file, @RequestParam String bucketName) throws Exception {
        FileHandlerFileRequest uploadFileRequest = new FileHandlerFileRequest(file);
        fileHandlerService.uploadFile(bucketName, uploadFileRequest);
    }

    @PostMapping("/createBucket")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createBucket(@RequestParam String bucketName) throws Exception {
        fileHandlerService.createBuckets(bucketName);
    }

}
