package com.example.filehandler.controller;

import com.example.filehandler.dto.FileHandlerFileRequest;
import com.example.filehandler.service.FileHandlerService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/filehandler")
@RequiredArgsConstructor
public class FileHandlerController {

    private final FileHandlerService fileHandlerService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadFile(@RequestPart("file") MultipartFile file) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        FileHandlerFileRequest uploadFileRequest = new FileHandlerFileRequest(file);
        fileHandlerService.fileUpload(uploadFileRequest);
    }

}
